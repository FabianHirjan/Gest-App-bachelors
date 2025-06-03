package hirjanfabian.bachelors.entities;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;


@Entity @Table(name = "car_models")
@Getter @Setter
public class CarModels {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String model;

    @ManyToOne
    @JoinColumn(name = "car_make_id", nullable = false)
    @JsonBackReference("make-model")
    private CarMakes carMake;

    @Override
    public String toString() {
        return model;
    }
}
