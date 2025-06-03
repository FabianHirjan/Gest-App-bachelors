package hirjanfabian.bachelors.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class CarMakeDTO {
    private Long id;
    private String make;

    @JsonIgnore
    private List<CarModelDTO> models;
}