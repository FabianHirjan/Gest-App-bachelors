package hirjanfabian.gestapp.entities;


import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "cars")
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String type;
    private String licensePlate;
    private Long mileage;

    private Date lastOilChange;

    private Date insuranceExpirationDate;

    private Date itpExpirationDate;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "driver_id", referencedColumnName = "id")
    private User driver;



}
