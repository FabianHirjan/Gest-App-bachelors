package hirjanfabian.bachelors.mapper;

import hirjanfabian.bachelors.dto.CarMakeDTO;
import hirjanfabian.bachelors.entities.CarMakes;

import java.util.Collections;

public class MakeMapper {

    public static CarMakeDTO toCarMakeDTO(CarMakes make) {
        if (make == null) {
            return null;
        }

        CarMakeDTO dto = new CarMakeDTO();
        dto.setId(make.getId());
        dto.setMake(make.getMake());
        dto.setModels(
                make.getModels() != null
                        ? make.getModels().stream().map(ModelMapper::toCarModel).toList()
                        : Collections.emptyList()
        );

        return dto;
    }

    public static CarMakes toCarMake(CarMakeDTO dto) {
        if (dto == null) {
            return null;
        }

        CarMakes make = new CarMakes();
        make.setId(dto.getId());
        make.setMake(dto.getMake());

        return make;
    }
}
