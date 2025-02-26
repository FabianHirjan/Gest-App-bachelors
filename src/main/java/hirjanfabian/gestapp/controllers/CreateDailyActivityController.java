package hirjanfabian.gestapp.controllers;

import hirjanfabian.gestapp.business.KmService;
import hirjanfabian.gestapp.business.UserService;
import hirjanfabian.gestapp.entities.Car;
import hirjanfabian.gestapp.entities.DailyActivity;
import hirjanfabian.gestapp.entities.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/dac")
public class CreateDailyActivityController {
    private final KmService kmService;
    private final UserService userService;

    public CreateDailyActivityController(KmService kmService, UserService userService) {
        this.kmService = kmService;
        this.userService = userService;
    }

    @GetMapping("/view")
    public String showDailyActivityForm(Model model, Authentication authentication) {
        model.addAttribute("dac", new DailyActivity());
        return "dac"; // (dac.html)
    }

    @PostMapping
    @ResponseBody
    public ResponseEntity<DailyActivity> createDailyActivity(@RequestBody DailyActivity dailyActivity, Authentication authentication) {
        User loggedUser = userService.getUserByUsername(authentication.getName());
        Car car = userService.getCarForUser(loggedUser.getId());
        if (car == null) {
            return ResponseEntity.badRequest().body(null);
        }
        dailyActivity.setCar(car);
        DailyActivity dac = kmService.createDailyActivity(dailyActivity);
        return ResponseEntity.ok(dac);
    }

}
