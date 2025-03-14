package hirjanfabian.bachelors.services;

import hirjanfabian.bachelors.entities.Car;
import hirjanfabian.bachelors.entities.DailyActivity;
import hirjanfabian.bachelors.entities.User;
import hirjanfabian.bachelors.repositories.CarRepository;
import hirjanfabian.bachelors.repositories.DailyActivityRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DailyActivityService {
    private final DailyActivityRepository dailyActivityRepository;
    private final CarRepository carRepository;

    public DailyActivityService(DailyActivityRepository dailyActivityRepository, CarRepository carRepository) {
        this.dailyActivityRepository = dailyActivityRepository;
        this.carRepository = carRepository;
    }

    public List<DailyActivity> findAllActivities() {
        return dailyActivityRepository.findAll();
    }


    public DailyActivity createDailyActivity(DailyActivity activity) {
        Car car = activity.getUser().getCar();
        activity.setCar(car);
        activity.setApproved(false);
        return dailyActivityRepository.save(activity);
    }

    public List<DailyActivity> findUserActivities(User user) {
        return dailyActivityRepository.findAll()
                .stream()
                .filter(dailyActivity -> dailyActivity.getUser().equals(user))
                .collect(Collectors.toList());
    }

    public DailyActivity approveActivity(Long activityId) {
        DailyActivity activity = dailyActivityRepository.findById(activityId).orElse(null);
        if (activity != null && !activity.isApproved()) {
            activity.setApproved(true);
            Car car = activity.getCar();
            car.setMileage(car.getMileage() + activity.getKilometers());
            carRepository.save(car);
            return dailyActivityRepository.save(activity);
        }
        return null;
    }
}