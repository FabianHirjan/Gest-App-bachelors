package hirjanfabian.gestapp.business;


import hirjanfabian.gestapp.entities.DailyActivity;
import hirjanfabian.gestapp.entities.Logs;
import hirjanfabian.gestapp.repositories.DailyActivityRepository;
import hirjanfabian.gestapp.repositories.LogsRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DailyActivityService {

    private final DailyActivityRepository dailyActivityRepository;
    private final LogsRepository logsRepository;

    public DailyActivityService(DailyActivityRepository dailyActivityRepository, LogsRepository logsRepository) {
        this.dailyActivityRepository = dailyActivityRepository;
        this.logsRepository = logsRepository;
    }

    public List<DailyActivity> getDailyActivities() {
        return dailyActivityRepository.findAll();
    }
}
