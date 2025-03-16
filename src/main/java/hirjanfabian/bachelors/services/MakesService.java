package hirjanfabian.bachelors.services;

import hirjanfabian.bachelors.dto.CarMakeDTO;
import hirjanfabian.bachelors.entities.CarMakes;
import hirjanfabian.bachelors.entities.CarModels;
import hirjanfabian.bachelors.repositories.CarMakesRepository;
import hirjanfabian.bachelors.repositories.ModelsRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MakesService {
    private final ModelsRepository modelsRepository;
    private final CarMakesRepository carMakesRepository;

    public MakesService(ModelsRepository modelsRepository, CarMakesRepository carMakesRepository) {
        this.modelsRepository = modelsRepository;
        this.carMakesRepository = carMakesRepository;
    }

    public void createMake(CarMakes carMake) {
        carMakesRepository.save(carMake);
    }

    public void createModel(CarModels carModel) {
        modelsRepository.save(carModel);
    }

    public boolean deleteMake(CarMakes carMake) {
        carMakesRepository.delete(carMake);
        return true;
    }

    public boolean deleteModel(CarModels carModel) {
        modelsRepository.delete(carModel);
        return true;
    }

    public CarMakes findMakeById(Long id) {
        return carMakesRepository.findById(id).orElse(null);
    }

    public CarModels findModelById(Long id) {
        return modelsRepository.findById(id).orElse(null);
    }

    public List<CarMakeDTO> getMakes() {
        return carMakesRepository.findAll().stream()
                .map(carMake -> {
                    CarMakeDTO carMakeDTO = new CarMakeDTO();
                    carMakeDTO.setId(carMake.getId());
                    carMakeDTO.setMake(carMake.getMake());
                    return carMakeDTO;
                })
                .toList();
    }

    public List<CarMakes> findAllMakesWithModels() {
        return carMakesRepository.findAll();
    }
}
