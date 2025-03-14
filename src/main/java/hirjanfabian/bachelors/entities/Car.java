package hirjanfabian.bachelors.entities;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonProperty;

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

    @OneToOne(optional = true, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", nullable = true)
    private User user;

    private String licensePlate;
    private long mileage;

    @JsonProperty("vin")
    private String VIN;

    private Date lastInspection;

    private Date lastOilChange;

    private Date lastTireChange;


    private Date insuranceExpiration;


    public Date getLastInspection() {
        return lastInspection;
    }

    public void setLastInspection(Date lastInspection) {
        this.lastInspection = lastInspection;
    }

    public Date getLastOilChange() {
        return lastOilChange;
    }

    public void setLastOilChange(Date lastOilChange) {
        this.lastOilChange = lastOilChange;
    }

    public Date getLastTireChange() {
        return lastTireChange;
    }

    public void setLastTireChange(Date lastTireChange) {
        this.lastTireChange = lastTireChange;
    }

    public Date getInsuranceExpiration() {
        return insuranceExpiration;
    }

    public void setInsuranceExpiration(Date insuranceExpiration) {
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
