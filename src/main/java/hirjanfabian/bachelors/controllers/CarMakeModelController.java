package hirjanfabian.bachelors.controllers;

import hirjanfabian.bachelors.dto.CarMakeDTO;
import hirjanfabian.bachelors.entities.CarMakes;
import hirjanfabian.bachelors.mapper.MakeMapper;
import hirjanfabian.bachelors.services.MakesService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@RestController
@RequestMapping("/api/car")
public class CarMakeModelController {

    private final MakesService makesService;

    public CarMakeModelController(MakesService makesService) {
        this.makesService = makesService;
    }

    @GetMapping("/makes-with-models")
    public ResponseEntity<List<CarMakeDTO>> getMakesWithModels() {
        List<CarMakeDTO> allMakes = makesService.findAllMakesWithModels().stream()
                .map(MakeMapper::toCarMakeDTO)
                .toList();
        return ResponseEntity.ok(allMakes);
    }
}
