package hirjanfabian.bachelors.mapper;

import hirjanfabian.bachelors.dto.CarMakeDTO;
import hirjanfabian.bachelors.entities.CarMakes;

public class MakeMapper {

    public static CarMakeDTO toCarMakeDTO(CarMakes make) {
        if (make == null) {
            return null;
        }

        CarMakeDTO dto = new CarMakeDTO();
        dto.setId(make.getId());
        dto.setMake(make.getMake());
        dto.setModels(make.getModels()
                .stream()
                .map(ModelMapper::toCarModel)
                .toList());
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
