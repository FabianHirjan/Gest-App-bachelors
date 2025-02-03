package hirjanfabian.gestapp.entities;


import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "dailyActivity")
public class DailyActivity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String summary;

    private String description;
    private Date date;
    @OneToOne
    @JoinColumn(name = "car_id", referencedColumnName = "id")
    private User user;
    private String roadmap;
    private int startKms;
    private int doneKms;
    private int totalKms;
    private int fuelConsumption;

}
