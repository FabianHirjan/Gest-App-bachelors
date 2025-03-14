package hirjanfabian.bachelors.dto;


import hirjanfabian.bachelors.entities.CarMakes;

public class CarModelDTO {
    private Long id;
    private String model;
    private CarMakes carMake;

    public CarMakes getCarMake() {
        return carMake;
    }

    public void setCarMake(CarMakes carMake) {
        this.carMake = carMake;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }


}
