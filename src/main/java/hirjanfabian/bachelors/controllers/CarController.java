package hirjanfabian.bachelors.controllers;

import hirjanfabian.bachelors.dto.CarMakeDTO;
import hirjanfabian.bachelors.dto.CarModelDTO;
import hirjanfabian.bachelors.entities.CarMakes;
import hirjanfabian.bachelors.entities.CarModels;
import hirjanfabian.bachelors.mapper.MakeMapper;
import hirjanfabian.bachelors.mapper.ModelMapper;
import hirjanfabian.bachelors.services.MakesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/cars")
@RequiredArgsConstructor
public class CarController {

    private final MakesService makesService;
    private final MakeMapper makeMapper = MakeMapper.INSTANCE; // MapStruct instance
    private final ModelMapper modelMapper = ModelMapper.INSTANCE; // MapStruct instance

    @GetMapping("/makes-with-models")
    public ResponseEntity<List<CarMakeDTO>> getMakesWithModels() {
        var makes = makesService.findAllMakesWithModels();
        var makeDTOs = makes.stream()
                .map(makeMapper::toCarMakeDTO)
                .toList();
        return ResponseEntity.ok(makeDTOs);
    }

    // GET /api/car/makes
    @GetMapping("/makes")
    public ResponseEntity<List<CarMakeDTO>> getAllMakes() {
        var makes = Optional.ofNullable(makesService.findAllMakesWithModels())
                .orElseGet(List::of); // Empty list if null
        var makeDTOs = makes.stream()
                .map(makeMapper::toCarMakeDTO)
                .toList();
        return ResponseEntity.ok(makeDTOs);
    }

    // POST /api/car/makes
    @PostMapping("/makes")
    public ResponseEntity<CarMakeDTO> createMake(@RequestBody CarMakeDTO carMakeDTO) {
        if (carMakeDTO == null || carMakeDTO.getMake() == null) {
            throw new IllegalArgumentException("Make data is required");
        }
        if (makesService.findMakeByName(carMakeDTO.getMake()) != null) {
            throw new IllegalStateException("Make already exists");
        }
        var carMake = makeMapper.toCarMake(carMakeDTO);
        var createdMake = makesService.createMake(carMake);
        var createdMakeDTO = makeMapper.toCarMakeDTO(createdMake);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdMakeDTO);
    }

    // GET /api/car/models
    @GetMapping("/models")
    public ResponseEntity<List<CarModelDTO>> getAllModels() {
        var models = makesService.findAllMakesWithModels()
                .stream()
                .flatMap(make -> make.getModels() != null ? make.getModels().stream() : List.<CarModels>of().stream())
                .map(modelMapper::toCarModelDTO)
                .toList();
        return ResponseEntity.ok(models);
    }

    // POST /api/car/models
    @PostMapping("/models")
    public ResponseEntity<CarModelDTO> createModel(@RequestBody CarModelDTO carModelDTO) {
        if (carModelDTO == null || carModelDTO.getCarMake() == null || carModelDTO.getCarMake().getId() == null) {
            throw new IllegalArgumentException("Model data and valid make ID are required");
        }
        var carMake = makesService.findMakeById(carModelDTO.getCarMake().getId());
        var carModel = modelMapper.toCarModel(carModelDTO);
        carModel.setCarMake(carMake);
        makesService.createModel(carModel);
        var createdModelDTO = modelMapper.toCarModelDTO(carModel);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdModelDTO);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgument(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<String> handleIllegalState(IllegalStateException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }
}