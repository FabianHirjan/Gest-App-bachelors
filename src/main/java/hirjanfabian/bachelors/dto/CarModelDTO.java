package hirjanfabian.bachelors.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CarModelDTO {
    private Long id;
    private String model;
    private CarMakeDTO carMake;
}