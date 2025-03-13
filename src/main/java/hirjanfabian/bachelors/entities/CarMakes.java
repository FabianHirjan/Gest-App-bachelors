package hirjanfabian.bachelors.entities;

import jakarta.persistence.*;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonManagedReference;


@Entity
@Table(name = "car_makes")

public class CarMakes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String make;

    @OneToMany(mappedBy = "carMake", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference(value = "make-model")
    private List<CarModels> models;

    public CarMakes() {
    }

    public CarMakes(Long id, String make) {
        this.id = id;
        this.make = make;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public List<CarModels> getModels() {
        return models;
    }

    public void setModels(List<CarModels> models) {
        this.models = models;
    }
}
