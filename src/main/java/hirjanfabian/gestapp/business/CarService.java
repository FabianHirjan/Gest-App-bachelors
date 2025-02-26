package hirjanfabian.gestapp.business;

import hirjanfabian.gestapp.entities.Car;
import hirjanfabian.gestapp.entities.Logs;
import hirjanfabian.gestapp.repositories.CarRepository;
import hirjanfabian.gestapp.repositories.LogsRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    public Optional<Car> updateCar(Long id, Car updatedCar) {
        return carRepository.findById(id)
                .map(car -> {
                    updateCarProperties(car, updatedCar);
                    logsRepository.save(new Logs("Car " + car.getId() + " was updated"));
                    return carRepository.save(car);
                });
    }

    private void updateCarProperties(Car car, Car updatedCar) {
        car.setType(updatedCar.getType());
        car.setLicensePlate(updatedCar.getLicensePlate());
        car.setMileage(updatedCar.getMileage());
        car.setLastOilChange(updatedCar.getLastOilChange());
        car.setInsuranceExpirationDate(updatedCar.getInsuranceExpirationDate());
        car.setItpExpirationDate(updatedCar.getItpExpirationDate());
        car.setDriver(updatedCar.getDriver());
    }

    public Car createCar(Car car) {
        return carRepository.save(car);
    }

    public boolean deleteCar(Long id) {
        Optional<Car> car = carRepository.findById(id);
        if (car.isPresent()) {
            carRepository.delete(car.get());
            logsRepository.save(new Logs("Car " + id + " was deleted"));
            return true;
        }
        return false;
    }

    public Optional<Car> findById(Long carID) {
        return carRepository.findById(carID);
    }
}
