package hirjanfabian.gestapp.entities;


import jakarta.persistence.*;

@Entity
@Table(name = "logs")
public class Logs {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String message;

    public Logs(String s) {
        this.message = s;
    }

    public Logs() {

    }
}
