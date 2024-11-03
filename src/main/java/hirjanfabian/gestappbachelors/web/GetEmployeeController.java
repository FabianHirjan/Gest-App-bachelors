package hirjanfabian.gestappbachelors.web;

import hirjanfabian.gestappbachelors.data.Employee;
import hirjanfabian.gestappbachelors.data.EmployeeRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/employees")
public class GetEmployeeController {
    private final EmployeeRepository employeeRepository;

    public GetEmployeeController(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @GetMapping
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }
}