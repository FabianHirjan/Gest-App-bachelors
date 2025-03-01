package hirjanfabian.gestapp.business;

import hirjanfabian.gestapp.entities.Car;
import hirjanfabian.gestapp.entities.Logs;
import hirjanfabian.gestapp.repositories.CarRepository;
import hirjanfabian.gestapp.repositories.LogsRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service class responsible for handling business logic related to car operations.
 * Provides methods for creating, retrieving, updating, and deleting cars,
 * as well as tracking logs for various operations.
 */
@Service
public class CarService {
    private final CarRepository carRepository;
    private final LogsRepository logsRepository;

    public CarService(CarRepository carRepository, LogsRepository logsRepository) {
        this.carRepository = carRepository;
        this.logsRepository = logsRepository;
    }

    /**
     * Retrieves a list of all cars from the repository.
     *
     * @return a list of all cars
     */
    public List<Car> getAllCars() {
        return carRepository.findAll();
    }

    /**
     * Updates an existing car in the repository with new information and logs the update action.
     *
     * @param id The unique identifier of the car to be updated.
     * @param updatedCar An instance of {@code Car} containing the updated properties.
     * @return An {@code Optional} containing the updated {@code Car} if the update was successful, or an empty {@code Optional} if the car was not found.
     */
    @Transactional
    public Optional<Car> updateCar(Long id, Car updatedCar) {
        return carRepository.findById(id)
                .map(car -> {
                    updateCarProperties(car, updatedCar);
                    logsRepository.save(new Logs("Car " + car.getId() + " was updated"));
                    return carRepository.save(car);
                });
    }

    /**
     * Updates the properties of the specified {@code car} with the non-null properties
     * of the given {@code updatedCar}.
     *
     * @param car The original {@code Car} object to be updated.
     * @param updatedCar The {@code Car} object containing the new property values.
     */
    private void updateCarProperties(Car car, Car updatedCar) {
        if (updatedCar.getType() != null) {
            car.setType(updatedCar.getType());
        }
        if (updatedCar.getLicensePlate() != null) {
            car.setLicensePlate(updatedCar.getLicensePlate());
        }
        if (updatedCar.getMileage() != null) {
            car.setMileage(updatedCar.getMileage());
        }
        if (updatedCar.getLastOilChange() != null) {
            car.setLastOilChange(updatedCar.getLastOilChange());
        }
        if (updatedCar.getInsuranceExpirationDate() != null) {
            car.setInsuranceExpirationDate(updatedCar.getInsuranceExpirationDate());
        }
        if (updatedCar.getItpExpirationDate() != null) {
            car.setItpExpirationDate(updatedCar.getItpExpirationDate());
        }
        if (updatedCar.getDriver() != null) {
            car.setDriver(updatedCar.getDriver());
        }
    }

    /**
     * Creates a new car in the repository and logs the creation event.
     * Validates that the car to be created does not already have an ID
     * and that its driver, if provided, has a valid ID.
     *
     * @param car The {@code Car} object to be created.
     * @return An {@code Optional} containing the created {@code Car} if successful,
     *         or an empty {@code Optional} if the validation fails.
     */
    @Transactional
    public Optional<Car> createCar(Car car) {
        if (car.getId() != null || car.getDriver() != null && car.getDriver().getId() == null) {
            return Optional.empty();
        }

        Car savedCar = carRepository.save(car);

        logsRepository.save(new Logs("Car with license plate " + car.getLicensePlate() + " was created."));

        return Optional.of(savedCar);
    }

    /**
     * Deletes a car from the repository based on the provided ID.
     *
     * @param id the unique identifier of the car to be deleted
     * @return true if the car was successfully deleted, false if the car with the provided ID was not found or if the ID is null
     */
    @Transactional
    public boolean deleteCar(Long id) {
        if (id == null) {
            logDeletionFailure(id, "Provided ID is null");
            return false;
        }

        if (carRepository.existsById(id)) {
            carRepository.deleteById(id);
            logDeletionSuccess(id);
            return true;
        }

        logDeletionFailure(id, "Car not found");
        return false;
    }

    /**
     * Logs a message indicating that a car with the specified ID was successfully deleted.
     *
     * @param id the unique identifier of the car that was deleted
     */
    private void logDeletionSuccess(Long id) {
        logsRepository.save(new Logs("Car " + id + " was deleted"));
    }

    /**
     * Logs a failed car deletion attempt with the specified ID and reason.
     *
     * @param id The ID of the car that failed to delete.
     * @param reason The reason explaining why the deletion failed.
     */
    private void logDeletionFailure(Long id, String reason) {
        logsRepository.save(new Logs("Failed to delete car with ID " + id + ": " + reason));
    }

    /**
     * Finds a car by its unique identifier.
     *
     * @param carID the unique identifier of the car to find
     * @return an {@code Optional} containing the car if found, or an empty {@code Optional} if no car is found
     */
    public Optional<Car> findById(Long carID) {
        if (carID == null) {
            return Optional.empty();
        }
        Optional<Car> car = carRepository.findById(carID);
        if (car.isEmpty()) {
            logsRepository.save(new Logs("Car with ID " + carID + " not found"));
        }
        return car;
    }
}
