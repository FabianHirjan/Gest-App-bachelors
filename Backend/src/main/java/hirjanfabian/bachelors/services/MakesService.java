package hirjanfabian.bachelors.services;

import hirjanfabian.bachelors.dto.CarMakeDTO;
import hirjanfabian.bachelors.entities.CarMakes;
import hirjanfabian.bachelors.entities.CarModels;
import hirjanfabian.bachelors.repositories.CarMakesRepository;
import hirjanfabian.bachelors.repositories.ModelsRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.EntityGraph;
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

    public CarMakes createMake(CarMakes carMake) {
        carMakesRepository.save(carMake);
        return carMake;
    }

    @Transactional
    public List<CarMakes> findAllMakes() {
        return carMakesRepository.findAllWithModels();   // fetch-join pe „models”
    }

    public List<CarModels> findAllModels() {
        return modelsRepository.findAllWithMake();      // fetch-join carMake
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

    public CarMakeDTO findMakeByName(String make) {
        CarMakes carMake = carMakesRepository.findByMake(make);
        CarMakeDTO carMakeDTO = new CarMakeDTO();
        carMakeDTO.setId(carMake.getId());
        carMakeDTO.setMake(carMake.getMake());
        return carMakeDTO;
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

    @EntityGraph(attributePaths = {"models"})
    public List<CarMakes> findAllMakesWithModels() {
        return carMakesRepository.findAll();
    }
}
