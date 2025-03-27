package hirjanfabian.bachelors.services;

import hirjanfabian.bachelors.entities.Car;
import hirjanfabian.bachelors.entities.User;
import hirjanfabian.bachelors.repositories.CarRepository;
import hirjanfabian.bachelors.repositories.UserRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class CarService {
    private final CarRepository carRepository;
    private final UserRepository userRepository;
    private final Integer oilChangeInterval = 50000;

    public CarService(CarRepository carRepository, UserRepository userRepository) {
        this.carRepository = carRepository;
        this.userRepository = userRepository;
    }

    @Async
    public CompletableFuture<Car> createCar(Car car) {
        carRepository.save(car);
        return CompletableFuture.completedFuture(car);
    }

    public Car getCarByUsernameSync(String username) {
        User user = userRepository.findByUsername(username);
        return user.getCar();
    }

    @Async
    public CompletableFuture<Car> getCarByLicensePlateSync(String licensePlate) {
        return CompletableFuture.completedFuture(carRepository.findByLicensePlate(licensePlate));
    }

    @Async
    public CompletableFuture<Car> getCarByUsername(String username) {
        Car car = getCarByUsernameSync(username);
        return CompletableFuture.completedFuture(car);
    }





    @Async
    public void saveCar(Car car) {
        carRepository.save(car);
    }

    public List<Car> getUnassignedCarsSync() {
        List<Car> cars = carRepository.findAll();
        return cars.stream()
                .filter(car -> car.getUser() == null)
                .collect(Collectors.toList());
    }

    @Async
    public CompletableFuture<List<Car>> getUnassignedCars() {
        List<Car> unassignedCars = getUnassignedCarsSync();
        return CompletableFuture.completedFuture(unassignedCars);
    }


    @Async
    public CompletableFuture<List<Car>> getAllCars() {
        List<Car> cars = carRepository.findAll();
        return CompletableFuture.completedFuture(cars);
    }

    @Async
    public void markInsurance(Long carId) {
        Car car = carRepository.findById(carId).orElse(null);
        if (car != null) {
            LocalDateTime newExpiration = LocalDateTime.now().plusYears(1);
            car.setInsuranceExpiration(newExpiration);
            carRepository.save(car);
        }
    }

    @Async
    public void markOilChange(Long carId) {
        Car car = carRepository.findById(carId).orElse(null);
        if (car != null) {
            car.setLastOilChange(LocalDateTime.now());
            carRepository.save(car);
        }
    }

    @Async
    public void markTireChange(Long carId) {
        Car car = carRepository.findById(carId).orElse(null);
        if (car != null) {
            car.setLastTireChange(LocalDateTime.now());
            carRepository.save(car);
        }
    }

    @Async
    public void markMaintenance(Long carId) {
        Car car = carRepository.findById(carId).orElse(null);
        if (car != null) {
            car.setLastInspection(LocalDateTime.now());
            carRepository.save(car);
        }
    }



    public void deleteCar(Long carId) {
        carRepository.deleteById(carId);
    }
}
