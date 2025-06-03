package hirjanfabian.bachelors.services;

import hirjanfabian.bachelors.entities.Car;
import hirjanfabian.bachelors.entities.DailyActivity;
import hirjanfabian.bachelors.entities.User;
import hirjanfabian.bachelors.repositories.CarRepository;
import hirjanfabian.bachelors.repositories.DailyActivityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Business layer for {@link DailyActivity} persistence and approval workflow.
 * <p>
 *   • <strong>create</strong>: persists a new activity <em>pending approval</em>.<br>
 *   • <strong>approve</strong>: flips the flag and updates the associated car’s
 *     mileage in one atomic transaction.
 * </p>
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class DailyActivityService {

    private final DailyActivityRepository dailyActivityRepository;
    private final CarRepository           carRepository;

    /*────────────────── READ ──────────────────*/

    public List<DailyActivity> findAllActivities() {
        return dailyActivityRepository.findAll();
    }

    public List<DailyActivity> findUserActivities(User user) {
        return dailyActivityRepository.findAll()
                .stream()
                .filter(a -> a.getUser().getId().equals(user.getId()))
                .collect(Collectors.toList());
    }

    /*────────────────── CREATE ──────────────────*/

    public DailyActivity createDailyActivity(DailyActivity activity) {
        if (activity == null || activity.getUser() == null) {
            throw new IllegalArgumentException("Activity and user must not be null");
        }

        // Fallback to user's car if none explicitly set
        if (activity.getCar() == null && activity.getUser().getCar() != null) {
            activity.setCar(activity.getUser().getCar());
        }

        activity.setApproved(false);
        return dailyActivityRepository.save(activity);
    }

    /*────────────────── APPROVE ──────────────────*/

    public DailyActivity approveActivity(Long activityId) {
        if (activityId == null || activityId <= 0) return null;

        DailyActivity activity = dailyActivityRepository.findById(activityId).orElse(null);
        if (activity == null || activity.isApproved()) return null;

        activity.setApproved(true);

        Car car = activity.getCar();
        if (car != null) {
            car.setMileage(car.getMileage() + activity.getKilometers());
            carRepository.save(car);
        } else {
            log.warn("Activity {} has no car associated; mileage not updated.", activityId);
        }

        return dailyActivityRepository.save(activity);
    }
}
