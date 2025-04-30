package hirjanfabian.bachelors.mapper;

import hirjanfabian.bachelors.dto.CarMakeDTO;
import hirjanfabian.bachelors.entities.CarMakes;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Maps between {@link CarMakes} JPA entities and {@link CarMakeDTO} transport objects.
 * <p>
 * ⚠  We deliberately **do not inject** the complementary mapper here; MapStruct resolves the
 * method dependency at *compile* time via the {@code uses} attribute, so Spring never finds
 * itself in a circular bean‑initialisation loop.
 */
@Mapper(componentModel = "spring", uses = { ModelMapper.class })
public interface MakeMapper {

    // We ignore the lazily‑loaded list in the default projection to avoid closing‑session issues.
    @Mapping(target = "models", ignore = true)
    CarMakeDTO toDto(CarMakes entity);

    CarMakes toEntity(CarMakeDTO dto);
}