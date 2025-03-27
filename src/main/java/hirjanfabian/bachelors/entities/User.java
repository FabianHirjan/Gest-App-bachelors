package hirjanfabian.bachelors.entities;
import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String password;
    private String role;
    private String email;
    private String firstName;
    private String lastName;

    private Double lastLatitude;
    private Double lastLongitude;
    private LocalDateTime lastLocationTimestamp;



    @OneToOne(cascade = CascadeType.ALL, optional = true)
    @JoinColumn(name = "car_id", referencedColumnName = "id", nullable = true)
    @JsonIgnore
    private Car car;

}
