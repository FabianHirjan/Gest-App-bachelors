package hirjanfabian.gestapp.business;


import hirjanfabian.gestapp.entities.Car;
import hirjanfabian.gestapp.entities.Logs;
import hirjanfabian.gestapp.entities.User;
import hirjanfabian.gestapp.repositories.CarRepository;
import hirjanfabian.gestapp.repositories.LogsRepository;
import hirjanfabian.gestapp.repositories.UserRepository;
import org.springframework.stereotype.Service;

/**
 * Service class responsible for assigning and unassigning drivers to cars.
 * It provides methods to associate a {@link User} as a driver for a {@link Car}
 * and to remove the driver association from a car.
 *
 * This service interacts with the following repositories:
 * - {@link CarRepository} for handling database operations related to {@link Car}.
 * - {@link UserRepository} for handling database operations related to {@link User}.
 * - {@link LogsRepository} for recording assignment or unassignment events.
 */
@Service
public class AssignDriverToCarService {
    private final CarRepository carRepository;
    private final UserRepository userRepository;
    private final LogsRepository logsRepository;

    /**
     * Constructs an instance of the AssignDriverToCarService class.
     * This service is responsible for assigning and unassigning drivers to cars.
     *
     * @param carRepository the repository interface for handling database operations related to cars
     * @param userRepository the repository interface for handling database operations related to users
     * @param logsRepository the repository interface for recording logs of assignment or unassignment events
     */
    public AssignDriverToCarService(CarRepository carRepository, UserRepository userRepository, LogsRepository logsRepository) {
        this.carRepository = carRepository;
        this.userRepository = userRepository;
        this.logsRepository = logsRepository;
    }

    /**
     * Assigns a car to a user by associating the given user as the driver of the specified car.
     * This involves updating the car's driver information in the database and logging the assignment.
     *
     * @param car the car to be assigned to a user; its ID must correspond to an existing car in the database
     * @param user the user to be assigned as the driver; their ID must correspond to an existing user in the database
     * @return the updated car with the user assigned as its driver,
     *         or {@code null} if the car does not exist in the database
     */
    public Car assignCarToDriver(Car car, User user) {
        return carRepository.findById(car.getId())
                .map(existingCar -> {
                    userRepository.findById(user.getId()).ifPresent(existingUser -> {
                        existingCar.setDriver(existingUser);
                        carRepository.save(existingCar);
                    });
                    logsRepository.save(new Logs("Car " + car.getId() + " was assigned to user " + user.getId()));
                    return existingCar;
                })
                .orElse(null);
    }

    /**
     * Unassigns a driver from the specified car by setting its driver to null.
     * This method updates the car's driver information in the database
     * and logs the unassignment event.
     *
     * @param car the car from which the driver will be unassigned; its ID must correspond to an existing car in the database
     * @return the updated car with no driver assigned, or {@code null} if the car does not exist in the database
     */
    public Car unasignCarFromDriver(Car car) {
        return carRepository.findById(car.getId())
                .map(existingCar -> {
                    existingCar.setDriver(null);
                    carRepository.save(existingCar);
                    logsRepository.save(new Logs("Car " + car.getId() + " was unassigned"));
                    return existingCar;
                })
                .orElse(null);
    }
}