package hirjanfabian.bachelors.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CarDTO {
    private Long id;
    private String licensePlate;
    private long mileage;
    private String vin;
    private String lastInspection;
    private String lastOilChange;
    private String lastTireChange;
    private String insuranceExpiration;
    private CarMakeDTO carMake;
    private CarModelDTO carModel;
    private UserDTO driver;
}