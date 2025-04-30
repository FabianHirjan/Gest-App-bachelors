package hirjanfabian.bachelors.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CarModelDTO {
    private Long id;
    private String model;

    // We intentionally expose only the make id, not the full DTO, to break recursion.
    private Long makeId;
}