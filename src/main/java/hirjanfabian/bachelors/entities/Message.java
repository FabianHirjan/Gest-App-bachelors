package hirjanfabian.bachelors.entities;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Getter
@Setter
@Table(name = "messages")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String message;

    @OneToOne(cascade = CascadeType.ALL)
    private User sender;


    @OneToOne(cascade = CascadeType.ALL)
    private User reciever;

    private Date sentDate;

}
