package hirjanfabian.bachelors.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "cars")
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "car_make_id", nullable = false)
    private CarMakes carMake;

    @ManyToOne
    @JoinColumn(name = "car_model_id", nullable = false)
    private CarModels carModel;

    @OneToOne(optional = true)
    @JoinColumn(name = "user_id", nullable = true)
    private User user;


    private String licensePlate;
    private long mileage;

    @JsonProperty("vin")
    private String VIN;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSSSSS")
    private LocalDateTime lastInspection;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSSSSS")
    private LocalDateTime lastOilChange;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSSSSS")
    private LocalDateTime lastTireChange;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSSSSS")
    private LocalDateTime insuranceExpiration;

    private Double estimatedPrice;

    private int year;


    @Transient private boolean insuranceOverdue;
    @Transient private boolean insuranceDueSoon;
    @Transient private boolean inspectionOverdue;
    @Transient private boolean inspectionDueSoon;
    @Transient private boolean oilOverdue;
    @Transient private boolean oilDueSoon;
    @Transient private boolean tireOverdue;
    @Transient private boolean tireDueSoon;

}
