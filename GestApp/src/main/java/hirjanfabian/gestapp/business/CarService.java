package hirjanfabian.gestapp.business;

import hirjanfabian.gestapp.entities.Car;
import hirjanfabian.gestapp.entities.User;
import hirjanfabian.gestapp.exceptions.ResourceNotFoundException;
import hirjanfabian.gestapp.repositories.CarRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarService {
    private final CarRepository carRepository;

    public CarService(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    public List<Car> all() {
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
                    return carRepository.save(car);
                })
                .orElseThrow(() -> new ResourceNotFoundException("Car not found with id " + id));
    }

    public Car createCar(Car car) {
        return carRepository.save(car);
    }

    public User getUserOfCar(Long id) {
        return carRepository.findById(id)
                .map(Car::getDriver)
                .orElseThrow(() -> new ResourceNotFoundException("Car not found with id " + id));
    }

    public void deleteCar(Long id){
        carRepository.deleteById(id);
    }


}