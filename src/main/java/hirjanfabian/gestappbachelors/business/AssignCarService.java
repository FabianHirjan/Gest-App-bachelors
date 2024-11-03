package hirjanfabian.gestappbachelors.business;

import hirjanfabian.gestappbachelors.data.Car;
import hirjanfabian.gestappbachelors.data.CarRepository;
import hirjanfabian.gestappbachelors.data.Employee;
import hirjanfabian.gestappbachelors.data.EmployeeRepository;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class AssignCarService {
    private final CarRepository carRepository;
    private final EmployeeRepository employeeRepository;

    public AssignCarService(CarRepository carRepository, EmployeeRepository employeeRepository) {
        this.carRepository = carRepository;
        this.employeeRepository = employeeRepository;
    }

    public void assignCar(Long carId, Long employeeId) {
        Car car = carRepository.findById(carId).orElseThrow(() -> new NoSuchElementException("Car not found with id: " + carId));
        Employee employee = employeeRepository.findById(employeeId).orElseThrow(() -> new NoSuchElementException("Employee not found with id: " + employeeId));
        employee.setCarAssigned(car);
        employeeRepository.save(employee);
    }
}