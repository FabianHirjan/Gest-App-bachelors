package hirjanfabian.gestappbachelors.business;

import hirjanfabian.gestappbachelors.data.Car;
import hirjanfabian.gestappbachelors.data.CarRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.NoSuchElementException;

@Service
public class CarService {
    private final CarRepository carRepository;

    public CarService(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    // Methods
    public Iterable<Car> findAll() {
        return carRepository.findAll();
    }

    public void createCar(String make, String model, int yearOfProduction, int mileage, String registrationPlate, LocalDate lastInspection, boolean insuranceAvailable, String insuranceExpirationDate) {
        Car newCar = new Car();
        newCar.setMake(make);
        newCar.setModel(model);
        newCar.setYearOfProduction(yearOfProduction);
        newCar.setMileage(mileage);
        newCar.setRegistrationPlate(registrationPlate);
        newCar.setLastInspection(lastInspection);
        newCar.setInsuranceAvailable(insuranceAvailable);
        newCar.setInsuranceExpirationDate(LocalDate.parse(insuranceExpirationDate));
        carRepository.save(newCar);
    }

    public void deleteCar(Long id) {
        carRepository.deleteById(id);
    }

    public void changeCarMileage(Long id, int newMileage) {
        Car car = carRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Car not found with id: " + id));
        if(newMileage < car.getMileage()) {
            throw new IllegalArgumentException("New mileage cannot be less than the current mileage!");
        }
        car.setMileage(newMileage);
        carRepository.save(car);
    }

    public Car findById(Long carId) {
        return carRepository.findById(carId).orElseThrow(() -> new NoSuchElementException("Car not found with id: " + carId));
    }
}