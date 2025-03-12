package hirjanfabian.bachelors.entities;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonProperty;

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

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String licensePlate;
    private long mileage;

    // Pentru a mapa corect "vin" din JSON, deși numele câmpului este VIN
    @JsonProperty("vin")
    private String VIN;

    // Getters și setters
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
