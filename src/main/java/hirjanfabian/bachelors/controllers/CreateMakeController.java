package hirjanfabian.bachelors.controllers;


import hirjanfabian.bachelors.dto.CarMakeDTO;
import hirjanfabian.bachelors.dto.CarModelDTO;
import hirjanfabian.bachelors.entities.CarMakes;
import hirjanfabian.bachelors.mapper.MakeMapper;
import hirjanfabian.bachelors.services.MakesService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/make")
public class CreateMakeController {
    private final MakesService makesService;

    public CreateMakeController(MakesService makesService) {
        this.makesService = makesService;
    }

    @PostMapping
    public ResponseEntity<CarMakeDTO> createMake(@RequestBody CarMakeDTO carMake) {
        if(makesService.findMakeByName(carMake.getMake())!=null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        CarMakes createdMakeEntity = makesService.createMake(MakeMapper.toCarMake(carMake));
        CarMakeDTO createdMakeDTO = MakeMapper.toCarMakeDTO(createdMakeEntity);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdMakeDTO);
    }
}

