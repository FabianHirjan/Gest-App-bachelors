package hirjanfabian.gestapp.controllers;


import hirjanfabian.gestapp.business.DailyActivityService;
import hirjanfabian.gestapp.business.KmService;
import hirjanfabian.gestapp.entities.DailyActivity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController

@RequestMapping("/api/dac")
public class CreateDailyActivityController {
    private final KmService kmService;

    public CreateDailyActivityController(KmService kmService) {
        this.kmService = kmService;
    }

    @PostMapping
    public ResponseEntity<DailyActivity> createDailyActivity(@RequestBody final DailyActivity dailyActivity) {
        DailyActivity DAC = kmService.createDailyActivity(dailyActivity);
        return ResponseEntity.ok(DAC);
    }
}
