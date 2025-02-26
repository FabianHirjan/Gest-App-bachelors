package hirjanfabian.gestapp.business;

import hirjanfabian.gestapp.entities.Car;
import hirjanfabian.gestapp.entities.Logs;
import hirjanfabian.gestapp.exceptions.ResourceNotFoundException;
import hirjanfabian.gestapp.repositories.CarRepository;
import hirjanfabian.gestapp.repositories.LogsRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarService {
    private final CarRepository carRepository;
    private final LogsRepository logsRepository;

    public CarService(CarRepository carRepository, LogsRepository logsRepository) {
        this.carRepository = carRepository;
        this.logsRepository = logsRepository;
    }

    public List<Car> getAllCars() {
        return carRepository.findAll();
    }

    public Car updateCar(Long id, Car updatedCar) {
        return carRepository.findById(id)
                .map(car -> {
                    car.setType(updatedCar.getType());
                    car.setLicensePlate(updatedCar.getLicensePlate());
                    car.setMileage(updatedCar.getMileage());
                    car.setLastOilChange(updatedCar.getLastOilChange());
                    car.setInsuranceExpirationDate(updatedCar.getInsuranceExpirationDate());
                    car.setItpExpirationDate(updatedCar.getItpExpirationDate());
                    car.setDriver(updatedCar.getDriver());
                    logsRepository.save(new Logs("Car " + car.getId() + " was updated "));
                    return carRepository.save(car);
                }

                )
                .orElseThrow(() -> new ResourceNotFoundException("Car not found with id " + id));
    }

    public Car createCar(Car car) {
        return carRepository.save(car);
    }

    public boolean deleteCar(Long id){
        logsRepository.save(new Logs("Car " + id + " was deleted "));
        carRepository.deleteById(id);
        return true;
    }

    public Car findById(Long carID) {
        return carRepository.findById(carID)
                .orElseThrow(() -> new EntityNotFoundException("Mașina cu id-ul " + carID + " nu a fost găsită"));
    }

}