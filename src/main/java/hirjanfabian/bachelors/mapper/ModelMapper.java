package hirjanfabian.bachelors.mapper;

import hirjanfabian.bachelors.dto.CarModelDTO;
import hirjanfabian.bachelors.entities.CarModels;
import hirjanfabian.bachelors.utils.CycleAvoidingMappingContext;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.mapstruct.Context;

@Mapper(uses = MakeMapper.class)
public interface ModelMapper {
    ModelMapper INSTANCE = Mappers.getMapper(ModelMapper.class);

    @Mapping(target = "carMake", source = "carMake")
    CarModelDTO toCarModelDTO(CarModels entity, @Context CycleAvoidingMappingContext context);

    CarModels toCarModel(CarModelDTO dto, @Context CycleAvoidingMappingContext context);

    default CarModelDTO toCarModelDTO(CarModels entity) {
        return toCarModelDTO(entity, new CycleAvoidingMappingContext());
    }

    default CarModels toCarModel(CarModelDTO dto) {
        return toCarModel(dto, new CycleAvoidingMappingContext());
    }
}