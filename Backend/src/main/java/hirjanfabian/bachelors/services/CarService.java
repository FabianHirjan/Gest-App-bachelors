package hirjanfabian.bachelors.services;

import hirjanfabian.bachelors.entities.Car;
import hirjanfabian.bachelors.entities.User;
import hirjanfabian.bachelors.repositories.CarRepository;
import hirjanfabian.bachelors.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Service layer encapsulating CRUD, maintenance helpers, and AI-enabled price estimation for {@link Car} entities.
 * <p>
 * <strong>Threading model</strong>: All public methods whose execution could block the caller are exposed via
 * {@code @Async CompletableFuture<…>} wrappers. Synchronous helpers (suffix {@code Sync}) are kept private to the
 * service. This preserves the original structure while allowing non-blocking I/O at the controller layer.
 * <p>
 * <strong>Validation</strong>: All entry-points defensively validate arguments with {@code Objects.requireNonNull} and
 * basic semantic checks (e.g. non-blank license plates, positive mileage) to fail fast and reduce null-pointer risk.
 */
@Service
@RequiredArgsConstructor
public class CarService {

    /*──────────── DEPENDENŢE ────────────*/
    private final CarRepository carRepository;
    private final UserRepository userRepository;
    private final RestTemplate restTemplate = new RestTemplate(); // Pentru apeluri REST

    private static final Logger logger = LoggerFactory.getLogger(CarService.class);
    private static final int OIL_CHANGE_INTERVAL_KM = 50_000;
    private static final Pattern LICENSE_PLATE_PATTERN =
            Pattern.compile("^[A-Z]{1,2}-?[0-9]{2,3}-?[A-Z]{1,3}$", Pattern.CASE_INSENSITIVE);

    /*─────────────────  C  •  R  •  U  •  D  ─────────────────*/

    /**
     * Persists the provided {@link Car} asynchronously.
     *
     * @param car entity to save (must be non-null)
     * @return a future completed with the managed instance
     * @throws IllegalArgumentException if {@code car} is {@code null}
     */
    @Async
    public CompletableFuture<Car> createCar(Car car) {
        Objects.requireNonNull(car, "car must not be null");
        validateCar(car);
        return CompletableFuture.completedFuture(carRepository.save(car))
                .thenApply(savedCar -> {
                    injectEstimatedPrice(savedCar);
                    return carRepository.save(savedCar);
                });
    }

    /**
     * Retrieves a car synchronously by the owner username.
     *
     * @param username owner username; must be non-blank
     * @return managed Car or {@code null} if none found
     */
    public Car getCarByUsernameSync(String username) {
        if (!StringUtils.hasText(username)) return null;
        User user = userRepository.findByUsername(username);
        return user != null ? user.getCar() : null;
    }

    /**
     * Asynchronous façade for {@link #getCarByUsernameSync(String)}.
     */
    @Async
    public CompletableFuture<Car> getCarByUsername(String username) {
        return CompletableFuture.completedFuture(getCarByUsernameSync(username));
    }

    /**
     * Retrieves a car by license plate.
     *
     * @param licensePlate formatted license plate (Romanian style)
     * @return a future with the {@link Car} or {@code null}
     */
    @Async
    public CompletableFuture<Car> getCarByLicensePlateSync(String licensePlate) {
        if (!isValidPlate(licensePlate)) {
            return CompletableFuture.completedFuture(null);
        }
        return CompletableFuture.completedFuture(carRepository.findByLicensePlate(licensePlate));
    }

    /**
     * Saves modifications to the given {@link Car}. If the entity is detached it will be merged.
     *
     * @param car managed instance (non-null)
     */
    @Async
    public void saveCar(Car car) {
        Objects.requireNonNull(car, "car must not be null");
        validateCar(car);
        carRepository.save(car);
    }

    /*──────────── LISTE ────────────*/

    /**
     * Returns cars that are not linked to any {@link User} (synchronous).
     */
    public List<Car> getUnassignedCarsSync() {
        return carRepository.findAll()
                .stream()
                .filter(c -> c.getUser() == null)
                .collect(Collectors.toList());
    }

    /**
     * Asynchronous façade for {@link #getUnassignedCarsSync()}.
     */
    @Async
    public CompletableFuture<List<Car>> getUnassignedCars() {
        return CompletableFuture.completedFuture(getUnassignedCarsSync());
    }

    /**
     * Returns <em>all</em> cars while enriching them with an AI-based price estimate.
     */
    @Async
    public CompletableFuture<List<Car>> getAllCars() {
        List<Car> cars = carRepository.findAll();
        cars.forEach(this::injectEstimatedPrice);
        return CompletableFuture.completedFuture(cars);
    }

    /*──────────── MENTENANŢĂ ────────────*/

    @Async public void markInsurance(Long id) { updateDate(id, Field.INSURANCE); }
    @Async public void markOilChange(Long id) { updateDate(id, Field.OIL_CHANGE); }
    @Async public void markTireChange(Long id) { updateDate(id, Field.TIRE_CHANGE); }
    @Async public void markMaintenance(Long id) { updateDate(id, Field.INSPECTION); }

    /*──────────── ŞTERGERE ────────────*/

    /** Deletes a {@link Car} by id. */
    public void deleteCar(Long carId) {
        if (carId == null || carId <= 0) return;
        carRepository.deleteById(carId);
    }

    /*──────────── Update ────────────*/

    /**
     * Updates a car's license plate. The new plate is validated for correct format.
     */
    public void updateLicensePlate(Long carId, String newPlate) {
        if (carId == null || !isValidPlate(newPlate)) return;
        carRepository.findById(carId).ifPresent(car -> {
            car.setLicensePlate(newPlate);
            carRepository.save(car);
        });
    }

    /** Finds a car by primary key. */
    public Optional<Car> findCarById(Long id) {
        return id == null ? Optional.empty() : carRepository.findById(id);
    }

    /*──────────── HELPERS ────────────*/

    /** Internal enum used to reduce boilerplate in maintenance markers. */
    private enum Field { INSURANCE, OIL_CHANGE, TIRE_CHANGE, INSPECTION }

    /**
     * Generic helper that updates one maintenance-related timestamp of a {@link Car}.
     */
    private void updateDate(Long carId, Field what) {
        if (carId == null || what == null) return;
        carRepository.findById(carId).ifPresent(car -> {
            LocalDateTime now = LocalDateTime.now();
            switch (what) {
                case INSURANCE -> car.setInsuranceExpiration(now.plusYears(1));
                case OIL_CHANGE -> car.setLastOilChange(now);
                case TIRE_CHANGE -> car.setLastTireChange(now);
                case INSPECTION -> car.setLastInspection(now);
            }
            carRepository.save(car);
        });
    }

    /**
     * Invokes the xAI Grok API via /api/price/estimate to enrich a {@link Car} with an estimated price.
     */
    private void injectEstimatedPrice(Car car) {
        if (car == null || car.getCarMake() == null || car.getCarModel() == null) return;

        try {
            String url = String.format("http://localhost:8080/api/price/estimate?make=%s&model=%s&year=%d&kilometers=%d",
                    car.getCarMake().getMake(), car.getCarModel().getModel(), car.getYear(), car.getMileage());
            logger.info("Apelând /api/price/estimate pentru mașina {}: {}", car.getLicensePlate(), url);
            ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                Double price = (Double) response.getBody().get("estimatedPrice");
                if (price != null) {
                    car.setEstimatedPrice(price);
                    logger.info("Preț estimat pentru {}: {} EUR", car.getLicensePlate(), price);
                } else {
                    logger.warn("Prețul estimat lipsește în răspuns pentru {}", car.getLicensePlate());
                }
            } else {
                logger.warn("Răspuns neașteptat de la /api/price/estimate pentru {}: {}", car.getLicensePlate(), response.getStatusCode());
            }
        } catch (Exception e) {
            logger.error("Eroare la estimarea prețului pentru {}: {}", car.getLicensePlate(), e.getMessage(), e);
        }

        computeAlertFlags(car);
    }

    /* NEW helper */
    private void computeAlertFlags(Car car) {
        if (car == null) return;
        LocalDateTime now = LocalDateTime.now();

        car.setInsuranceOverdue(
                car.getInsuranceExpiration() != null &&
                        car.getInsuranceExpiration().isBefore(now));

        car.setInsuranceDueSoon(
                !car.isInsuranceOverdue() &&
                        car.getInsuranceExpiration() != null &&
                        car.getInsuranceExpiration().isBefore(now.plusDays(30)));

        car.setInspectionOverdue(
                car.getLastInspection() != null &&
                        car.getLastInspection().isBefore(now.minusYears(1)));

        car.setInspectionDueSoon(
                !car.isInspectionOverdue() &&
                        car.getLastInspection() != null &&
                        car.getLastInspection().isBefore(now.minusYears(1).plusDays(30)));

        car.setOilOverdue(
                car.getLastOilChange() != null &&
                        car.getLastOilChange().isBefore(now.minusMonths(6)));

        car.setOilDueSoon(
                !car.isOilOverdue() &&
                        car.getLastOilChange() != null &&
                        car.getLastOilChange().isBefore(now.minusMonths(6).plusDays(30)));

        car.setTireOverdue(
                car.getLastTireChange() != null &&
                        car.getLastTireChange().isBefore(now.minusYears(1)));

        car.setTireDueSoon(
                !car.isTireOverdue() &&
                        car.getLastTireChange() != null &&
                        car.getLastTireChange().isBefore(now.minusYears(1).plusDays(30)));
    }

    /*──────────── VALIDATION ────────────*/

    /** Basic business-rule validation; can be replaced by Bean Validation. */
    private void validateCar(Car car) {
        if (car.getYear() <= 1900) {
            throw new IllegalArgumentException("Invalid year: " + car.getYear());
        }
        if (!isValidPlate(car.getLicensePlate())) {
            throw new IllegalArgumentException("Invalid license plate: " + car.getLicensePlate());
        }
    }

    private boolean isValidPlate(String plate) {
        return StringUtils.hasText(plate) && LICENSE_PLATE_PATTERN.matcher(plate).matches();
    }
}