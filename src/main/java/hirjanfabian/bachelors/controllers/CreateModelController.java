package hirjanfabian.bachelors.controllers;


import hirjanfabian.bachelors.entities.CarModels;
import hirjanfabian.bachelors.services.MakesService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/createModel")
public class CreateModelController {
    private MakesService makesService;

    public CreateModelController(MakesService makesService) {
        this.makesService = makesService;
    }

    @PostMapping
    public ResponseEntity<CarModels> createModel(@RequestBody CarModels carModel) {
        makesService.createModel(carModel);
        return ResponseEntity.ok().build();
    }
}
