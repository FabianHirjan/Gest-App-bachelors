package hirjanfabian.gestapp.business;


import hirjanfabian.gestapp.entities.Car;
import hirjanfabian.gestapp.entities.Logs;
import hirjanfabian.gestapp.entities.User;
import hirjanfabian.gestapp.repositories.CarRepository;
import hirjanfabian.gestapp.repositories.LogsRepository;
import hirjanfabian.gestapp.repositories.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class AssignDriverToCarService {
    private final CarRepository carRepository;
    private final UserRepository userRepository;
    private final LogsRepository logsRepository;

    public AssignDriverToCarService(CarRepository carRepository, UserRepository userRepository, LogsRepository logsRepository) {
        this.carRepository = carRepository;
        this.userRepository = userRepository;
        this.logsRepository = logsRepository;
    }

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

    public Car unasignCarFromDriver(Car car) {
        User user = new User();
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