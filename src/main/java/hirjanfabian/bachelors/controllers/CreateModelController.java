package hirjanfabian.bachelors.controllers;


import hirjanfabian.bachelors.dto.CarMakeDTO;
import hirjanfabian.bachelors.dto.CarModelDTO;
import hirjanfabian.bachelors.entities.CarMakes;
import hirjanfabian.bachelors.entities.CarModels;
import hirjanfabian.bachelors.mapper.MakeMapper;
import hirjanfabian.bachelors.mapper.ModelMapper;
import hirjanfabian.bachelors.services.MakesService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/model")
public class CreateModelController {
    private MakesService makesService;

    public CreateModelController(MakesService makesService) {
        this.makesService = makesService;
    }

    @PostMapping
    public ResponseEntity<CarMakeDTO> createModel(@RequestBody CarModelDTO carModel) {
        CarMakes carMake = makesService.findMakeById(carModel.getCarMake().getId());
        CarModels model = ModelMapper.toCarModel(carModel);
        model.setCarMake(carMake);
        makesService.createModel(model);
        return ResponseEntity.ok().build();
    }
}
