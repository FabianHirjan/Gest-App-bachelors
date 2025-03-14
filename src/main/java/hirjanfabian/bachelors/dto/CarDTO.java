package hirjanfabian.bachelors.dto;


public class CarDTO {
    private Long id;
    private CarMakeDTO carMake;
    private CarModelDTO carModel;
    private String licensePlate;
    private long mileage;
    private String vin;

    // Getters și Setters

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
