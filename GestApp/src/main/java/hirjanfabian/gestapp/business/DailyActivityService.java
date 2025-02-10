package hirjanfabian.gestapp.business;


import hirjanfabian.gestapp.entities.DailyActivity;
import hirjanfabian.gestapp.repositories.DailyActivityRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DailyActivityService {

    private final DailyActivityRepository dailyActivityRepository;

    public DailyActivityService(DailyActivityRepository dailyActivityRepository) {
        this.dailyActivityRepository = dailyActivityRepository;
    }


    public List<DailyActivity> getDailyActivities() {
        return dailyActivityRepository.findAll();
    }

    public DailyActivity getDailyActivityById(Long id) {
        return dailyActivityRepository.findById(id).orElse(null);
    }

    public DailyActivity saveDailyActivity(DailyActivity dailyActivity) {
        return dailyActivityRepository.save(dailyActivity);
    }
}
