package hirjanfabian.gestappbachelors.web;

import hirjanfabian.gestappbachelors.business.EmployeeService;
import hirjanfabian.gestappbachelors.data.Employee;
import hirjanfabian.gestappbachelors.data.Car;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/employees")
public class AddEmployeeController {
    private final EmployeeService employeeService;

    public AddEmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping
    public void addEmployee(
            @RequestParam boolean isSystemAdministrator,
            @RequestParam String firstName,
            @RequestParam String lastName,
            @RequestParam(required = false) Car carAssigned,
            @RequestParam String email,
            @RequestParam String phoneNumber,
            @RequestParam LocalDate dateJoined,
            @RequestParam String address,
            @RequestParam String cnp) {

        Employee employee = new Employee();
        employee.setSystemAdministrator(isSystemAdministrator);
        employee.setFirstName(firstName);
        employee.setLastName(lastName);
        employee.setCarAssigned(carAssigned);
        employee.setEmail(email);
        employee.setPhoneNumber(phoneNumber);
        employee.setDateJoined(dateJoined);
        employee.setAddress(address);
        employee.setCnp(cnp);

        employeeService.save(employee);
    }
}