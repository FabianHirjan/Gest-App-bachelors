package hirjanfabian.bachelors.controllers;


import hirjanfabian.bachelors.dto.CarMakeDTO;
import hirjanfabian.bachelors.services.MakesService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/car/makes")
public class GetMakesController {
    private MakesService makesService;

    public GetMakesController(MakesService makesService) {
        this.makesService = makesService;
    }

    @GetMapping
    public ResponseEntity<List<CarMakeDTO>> getMakes() {
        return ResponseEntity.ok(makesService.getMakes());
    }
}
