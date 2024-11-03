package hirjanfabian.gestappbachelors.data;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "CARS")
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int mileage;
    private String make;
    private String model;
    private int yearOfProduction;
    private String registrationPlate;
    private LocalDate lastInspection;
    private boolean insuranceAvailable;
    private LocalDate insuranceExpirationDate;

    @OneToOne(mappedBy = "carAssigned")
    private Employee driver;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getMileage() {
        return mileage;
    }

    public void setMileage(int mileage) {
        this.mileage = mileage;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getYearOfProduction() {
        return yearOfProduction;
    }

    public void setYearOfProduction(int yearOfProduction) {
        this.yearOfProduction = yearOfProduction;
    }

    public String getRegistrationPlate() {
        return registrationPlate;
    }

    public void setRegistrationPlate(String registrationPlate) {
        this.registrationPlate = registrationPlate;
    }

    public LocalDate getLastInspection() {
        return lastInspection;
    }

    public void setLastInspection(LocalDate lastInspection) {
        this.lastInspection = lastInspection;
    }

    public boolean isInsuranceAvailable() {
        return insuranceAvailable;
    }

    public void setInsuranceAvailable(boolean insuranceAvailable) {
        this.insuranceAvailable = insuranceAvailable;
    }

    public LocalDate getInsuranceExpirationDate() {
        return insuranceExpirationDate;
    }

    public void setInsuranceExpirationDate(LocalDate insuranceExpirationDate) {
        this.insuranceExpirationDate = insuranceExpirationDate;
    }

    public Employee getDriver() {
        return driver;
    }

    public void setDriver(Employee driver) {
        this.driver = driver;
    }
}