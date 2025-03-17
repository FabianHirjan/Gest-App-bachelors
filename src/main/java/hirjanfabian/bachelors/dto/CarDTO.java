package hirjanfabian.bachelors.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public class CarDTO {
    private Long id;
    private CarMakeDTO carMake;
    private CarModelDTO carModel;
    private String licensePlate;
    private long mileage;
    private String vin;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSSSSS")
    private LocalDateTime lastInspection;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSSSSS")
    private LocalDateTime lastOilChange;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSSSSS")
    private LocalDateTime lastTireChange;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSSSSS")
    private LocalDateTime insuranceExpiration;

    // Getters and Setters
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

    public CarMakeDTO getCarMake() {
        return carMake;
    }

    public void setCarMake(CarMakeDTO carMake) {
        this.carMake = carMake;
    }

    public CarModelDTO getCarModel() {
        return carModel;
    }

    public void setCarModel(CarModelDTO carModel) {
        this.carModel = carModel;
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

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }
}