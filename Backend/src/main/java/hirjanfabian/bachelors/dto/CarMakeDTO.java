package hirjanfabian.bachelors.dto;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class CarMakeDTO {
    private Long id;
    private String make;

    /** A *thin* projection of models to avoid circular JSON graphs. */
    private List<CarModelDTO> models;
}