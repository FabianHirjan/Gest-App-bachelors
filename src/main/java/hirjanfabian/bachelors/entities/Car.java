package hirjanfabian.bachelors.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.Date;
@Entity
@Table(name = "cars")
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "car_make_id", nullable = false)
    private CarMakes carMake;

    @ManyToOne
    @JoinColumn(name = "car_model_id", nullable = false)
    private CarModels carModel;

    @OneToOne(optional = true)
    @JoinColumn(name = "user_id", nullable = true)
    private User user;


    private String licensePlate;
    private long mileage;

    @JsonProperty("vin")
    private String VIN;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSSSSS")
    private LocalDateTime lastInspection;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSSSSS")
    private LocalDateTime lastOilChange;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSSSSS")
    private LocalDateTime lastTireChange;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSSSSS")
    private LocalDateTime insuranceExpiration;


    public LocalDateTime getLastInspection() {
        return lastInspection;
    }

    public void setLastInspection(LocalDateTime lastInspection) {
        this.lastInspection = lastInspection;
    }

    public LocalDateTime getLastOilChange() {
        return lastOilChange;
    }

    public void setLastOilChange(LocalDateTime lastOilChange) {
        this.lastOilChange = lastOilChange;
    }

    public LocalDateTime getLastTireChange() {
        return lastTireChange;
    }

    public void setLastTireChange(LocalDateTime lastTireChange) {
        this.lastTireChange = lastTireChange;
    }

    public LocalDateTime getInsuranceExpiration() {
        return insuranceExpiration;
    }

    public void setInsuranceExpiration(LocalDateTime insuranceExpiration) {
        this.insuranceExpiration = insuranceExpiration;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CarMakes getCarMake() {
        return carMake;
    }

    public void setCarMake(CarMakes carMake) {
        this.carMake = carMake;
    }

    public CarModels getCarModel() {
        return carModel;
    }

    public void setCarModel(CarModels carModel) {
        this.carModel = carModel;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public long getMileage() {
        return mileage;
    }

    public void setMileage(long mileage) {
        this.mileage = mileage;
    }

    public String getVIN() {
        return VIN;
    }

    public void setVIN(String VIN) {
        this.VIN = VIN;
    }
}
