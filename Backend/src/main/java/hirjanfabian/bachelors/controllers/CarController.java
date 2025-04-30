// src/main/java/hirjanfabian/bachelors/controllers/CarController.java
package hirjanfabian.bachelors.controllers;

import hirjanfabian.bachelors.dto.CarMakeDTO;
import hirjanfabian.bachelors.dto.CarModelDTO;
import hirjanfabian.bachelors.mapper.MakeMapper;
import hirjanfabian.bachelors.mapper.ModelMapper;
import hirjanfabian.bachelors.services.MakesService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cars")
@RequiredArgsConstructor
public class CarController {

    private final MakesService makesService;
    private final MakeMapper   makeMapper;
    private final ModelMapper  modelMapper;

    @GetMapping("/makes")
    public List<CarMakeDTO> getAllMakes() {
        return makesService.findAllMakes()
                .stream()
                .map(makeMapper::toDto)
                .toList();
    }

    @GetMapping("/models")
    public List<CarModelDTO> getAllModels() {
        return makesService.findAllModels()
                .stream()
                .map(modelMapper::toDto)
                .toList();
    }
}
