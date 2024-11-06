package hirjanfabian.gestapp.business;


import hirjanfabian.gestapp.entities.Car;
import hirjanfabian.gestapp.entities.User;
import hirjanfabian.gestapp.repositories.CarRepository;
import hirjanfabian.gestapp.repositories.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class AssignDriverToCarService {
    private final CarRepository carRepository;
    private final UserRepository userRepository;

    public AssignDriverToCarService(CarRepository carRepository, UserRepository userRepository) {
        this.carRepository = carRepository;
        this.userRepository = userRepository;
    }

    public Car assignCarToDriver(Car car, User user) {
        return carRepository.findById(car.getId())
                .map(existingCar -> {
                    userRepository.findById(user.getId()).ifPresent(existingUser -> {
                        existingCar.setDriver(existingUser);
                        carRepository.save(existingCar);
                    });
                    return existingCar;
                })
                .orElse(null);
    }


}