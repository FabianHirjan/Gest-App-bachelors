package hirjanfabian.bachelors.mapper;

import hirjanfabian.bachelors.dto.CarModelDTO;
import hirjanfabian.bachelors.entities.CarModels;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Maps between {@link CarModels} JPA entities and {@link CarModelDTO} transport objects.
 */
@Mapper(componentModel = "spring", uses = { MakeMapper.class })
public interface ModelMapper {

    /**
     * Pull the foreign‑key id off the nested entity and write it into {@code makeId},
     * keeping the DTO graph flat.
     */
    @Mapping(source = "carMake.id", target = "makeId")
    CarModelDTO toDto(CarModels entity);

    /**
     * The service layer should attach the parent Make; we ignore it here to avoid cycles.
     */
    @Mapping(target = "carMake", ignore = true)
    CarModels toEntity(CarModelDTO dto);
}