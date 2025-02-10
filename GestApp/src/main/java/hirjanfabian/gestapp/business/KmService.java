package hirjanfabian.gestapp.business;


import hirjanfabian.gestapp.entities.Car;
import hirjanfabian.gestapp.entities.DailyActivity;
import hirjanfabian.gestapp.entities.User;
import hirjanfabian.gestapp.repositories.CarRepository;
import hirjanfabian.gestapp.repositories.DailyActivityRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class KmService {
    private final CarRepository carRepository;
    private final DailyActivityRepository dailyActivityRepository;

    public KmService(CarRepository carRepository, DailyActivityRepository dailyActivityRepository) {
        this.carRepository = carRepository;
        this.dailyActivityRepository = dailyActivityRepository;
    }

    public DailyActivity createDailyActivity(DailyActivity dailyActivity) {
        DailyActivity dailyActivitySaved = dailyActivityRepository.save(dailyActivity);

        Long km = dailyActivitySaved.getTotalKms();

        Car carFromActivity = dailyActivitySaved.getCar();

        Optional<Car> optionalCar = carRepository.findById(carFromActivity.getId());
        if (optionalCar.isPresent()) {
            Car fullCar = optionalCar.get();
            fullCar.setMileage(km);
            carRepository.save(fullCar);
        } else {
            throw new RuntimeException("Car not found for id: " + carFromActivity.getId());
        }

        return dailyActivitySaved;
    }

}
