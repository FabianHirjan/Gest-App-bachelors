package hirjanfabian.bachelors.entities;

import jakarta.persistence.*;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;


// CarMakes.java
@Entity @Table(name = "car_makes")
@Getter @Setter
public class CarMakes {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String make;

    @OneToMany(mappedBy = "carMake",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    @JsonManagedReference("make-model")
    private List<CarModels> models;
}
