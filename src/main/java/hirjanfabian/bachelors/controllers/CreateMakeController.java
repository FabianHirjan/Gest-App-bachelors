package hirjanfabian.bachelors.controllers;


import hirjanfabian.bachelors.entities.CarMakes;
import hirjanfabian.bachelors.services.MakesService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/make")
public class CreateMakeController{
    private MakesService makesService;

    public CreateMakeController(MakesService makesService) {
        this.makesService = makesService;
    }
    @PostMapping
    public ResponseEntity<CarMakes> createMake(CarMakes carMake) {
        makesService.createMake(carMake);
        return ResponseEntity.ok().build();
    }
}
