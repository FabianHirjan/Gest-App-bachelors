package hirjanfabian.bachelors.controllers;

import hirjanfabian.bachelors.dto.CarMakeDTO;
import hirjanfabian.bachelors.dto.CarModelDTO;
import hirjanfabian.bachelors.entities.CarMakes;
import hirjanfabian.bachelors.entities.CarModels;
import hirjanfabian.bachelors.mapper.MakeMapper;
import hirjanfabian.bachelors.mapper.ModelMapper;
import hirjanfabian.bachelors.services.MakesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/car")
public class MakesController {

    private static final Logger logger = LoggerFactory.getLogger(MakesController.class);

    private final MakesService makesService;
    private final MakeMapper makeMapper;
    private final ModelMapper modelMapper;

    public MakesController(MakesService makesService, MakeMapper makeMapper, ModelMapper modelMapper) {
        this.makesService = makesService;
        this.makeMapper = makeMapper;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/makes")
    public ResponseEntity<List<CarMakeDTO>> getMakes() {
        logger.info("GET /api/car/makes");
        List<CarMakes> makes = makesService.findAllMakes();
        return ResponseEntity.ok(makes.stream()
                .map(makeMapper::toDto)
                .toList());
    }

    @GetMapping("/models")
    public ResponseEntity<List<CarModelDTO>> getModels(@RequestParam(value = "makeId", required = false) Long makeId) {
        logger.info("GET /api/car/models?makeId={}", makeId);
        List<CarModels> models = makeId != null
                ? makesService.findModelsByMakeId(makeId)
                : makesService.findAllModels();
        return ResponseEntity.ok(models.stream()
                .map(modelMapper::toDto)
                .toList());
    }
}