package hirjanfabian.bachelors.entities;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Entity
@Getter
@Setter
@Table(name = "complaints")
public class Complaints {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String reason;

    private String description;

    @ManyToOne
    @JoinColumn(name = "complaining_user_id", nullable = false)
    private User complainingUser;

    @ManyToOne
    @JoinColumn(name = "target_user_id", nullable = false)
    private User targetUser;
}
