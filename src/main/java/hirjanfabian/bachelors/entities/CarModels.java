package hirjanfabian.bachelors.entities;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "car_models")
public class CarModels {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String model;

    @ManyToOne
    @JoinColumn(name = "car_make_id", nullable = false)
    @JsonBackReference(value = "make-model")
    private CarMakes carMake;

    // Constructori
    public CarModels() {
    }

    public CarModels(Long id, String model, CarMakes carMake) {
        this.id = id;
        this.model = model;
        this.carMake = carMake;
    }

    // Getters și setters
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

    public CarMakes getCarMake() {
        return carMake;
    }

    public void setCarMake(CarMakes carMake) {
        this.carMake = carMake;
    }
}
