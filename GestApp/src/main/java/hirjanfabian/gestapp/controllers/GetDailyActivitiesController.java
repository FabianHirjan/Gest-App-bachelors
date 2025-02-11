package hirjanfabian.gestapp.controllers;


import hirjanfabian.gestapp.business.DailyActivityService;
import hirjanfabian.gestapp.entities.DailyActivity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class GetDailyActivitiesController {
    public DailyActivityService dailyActivityService;

    public GetDailyActivitiesController(DailyActivityService dailyActivityService) {
        this.dailyActivityService = dailyActivityService;
    }

    @GetMapping("/api/dac")
    public ResponseEntity<List<DailyActivity>> getDailyActivities() {
        return ResponseEntity.ok(dailyActivityService.getDailyActivities());
    }


}
