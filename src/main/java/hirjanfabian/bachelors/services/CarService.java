package hirjanfabian.bachelors.services;

import hirjanfabian.bachelors.entities.Car;
import hirjanfabian.bachelors.entities.User;
import hirjanfabian.bachelors.respositories.CarRepository;
import hirjanfabian.bachelors.respositories.UserRepository;
import org.springframework.stereotype.Service;
import java.util.stream.*;

import java.util.List;

@Service
public class CarService {
    private final CarRepository carRepository;
    private final UserRepository userRepository;

    public CarService(CarRepository carRepository, UserRepository userRepository) {
        this.carRepository = carRepository;
        this.userRepository = userRepository;
    }

    public void createCar(Car car) {
        carRepository.save(car);
    }

    public void assignCarToUser(Car car, String username) {
        User user = userRepository.findByUsername(username);
        car.setUser(user);
        carRepository.save(car);
    }

    public Car getCarByUsername(String username) {
        User user = userRepository.findByUsername(username);
        return user.getCar();
    }

    public void saveCar(Car car) {
        carRepository.save(car);
    }

    public List<Car> getUnassignedCars() {
        List<Car> cars = carRepository.findAll();
        return cars.stream()
                .filter(car -> car.getUser() == null)
                .collect(Collectors.toList());
    }
}