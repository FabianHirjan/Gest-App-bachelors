package hirjanfabian.bachelors.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;


@Getter
@Setter
public class DailyActivityDTO {
    // –– deja existente ––
    private Long id;
    private String description;
    private long kilometers;
    private double fuelConsumption;
    private LocalDate date;
    private boolean approved;

    // –– câmpuri de afișare ––
    private String postedBy;        // username / numele celui care a creat activitatea
    private Long   carId;
    private String carBrand;
    private String carModel;
    private String carRegistration;
}
