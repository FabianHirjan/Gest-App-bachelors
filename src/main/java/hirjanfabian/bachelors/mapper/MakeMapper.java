package hirjanfabian.bachelors.mapper;

import hirjanfabian.bachelors.dto.CarMakeDTO;
import hirjanfabian.bachelors.entities.CarMakes;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = ModelMapper.class)
public interface MakeMapper {
    MakeMapper INSTANCE = Mappers.getMapper(MakeMapper.class);

    @Mapping(target = "models", source = "models")
    CarMakeDTO toCarMakeDTO(CarMakes make);

    CarMakes toCarMake(CarMakeDTO dto);
}