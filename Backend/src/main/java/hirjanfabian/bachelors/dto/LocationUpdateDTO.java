package hirjanfabian.bachelors.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class LocationUpdateDTO {

    private Double latitude;
    private Double longitude;
    private String username; // Added to identify the user in the all-locations modal
}