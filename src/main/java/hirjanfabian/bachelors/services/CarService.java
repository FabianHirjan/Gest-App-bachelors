package hirjanfabian.bachelors.services;

import hirjanfabian.bachelors.entities.Car;
import hirjanfabian.bachelors.entities.User;
import hirjanfabian.bachelors.repositories.CarRepository;
import hirjanfabian.bachelors.repositories.UserRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class CarService {
    private final CarRepository carRepository;
    private final UserRepository userRepository;

    public CarService(CarRepository carRepository, UserRepository userRepository) {
        this.carRepository = carRepository;
        this.userRepository = userRepository;
    }

    @Async
    @CacheEvict(value = "unassignedCarsCache", allEntries = true)
    public void createCar(Car car) {
        carRepository.save(car);
    }

    @Cacheable(value = "carByUsernameCache", key = "#username")
    public Car getCarByUsernameSync(String username) {
        User user = userRepository.findByUsername(username);
        return user.getCar();
    }

    @Async
    public CompletableFuture<Car> getCarByUsername(String username) {
        Car car = getCarByUsernameSync(username);
        return CompletableFuture.completedFuture(car);
    }


    @Async
    @Caching(evict = {
            @CacheEvict(value = "carByUsernameCache", allEntries = true),
            @CacheEvict(value = "unassignedCarsCache", allEntries = true)
    })
    public void saveCar(Car car) {
        carRepository.save(car);
    }

    @Cacheable(value = "unassignedCarsCache")
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

    @Caching(evict = {
            @CacheEvict(value = "carByUsernameCache", allEntries = true),
            @CacheEvict(value = "unassignedCarsCache", allEntries = true)
    })
    public void deleteCar(Long carId) {
        carRepository.deleteById(carId);
    }
}
