package hirjanfabian.bachelors.mapper;

import hirjanfabian.bachelors.dto.CarDTO;
import hirjanfabian.bachelors.entities.Car;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring",
        uses = { MakeMapper.class,
                ModelMapper.class,
                UserMapper.class,
                DateMapper.class })
public interface CarMapper {

    /* ---------- entity ➜ DTO ---------- */
    @Mapping(target = "vin",  source = "VIN")

    @Mapping(target = "insuranceExpiration",
            source = "insuranceExpiration",
            qualifiedByName = "dateToString")
    @Mapping(target = "lastInspection",
            source = "lastInspection",
            qualifiedByName = "dateToString")
    @Mapping(target = "lastOilChange",
            source = "lastOilChange",
            qualifiedByName = "dateToString")
    @Mapping(target = "lastTireChange",
            source = "lastTireChange",
            qualifiedByName = "dateToString")
    CarDTO toDto(Car entity);

    /* ---------- DTO ➜ entity ---------- */
    @Mapping(target = "VIN", source = "vin")

    @Mapping(target = "insuranceExpiration",
            source = "insuranceExpiration",
            qualifiedByName = "stringToDate")
    @Mapping(target = "lastInspection",
            source = "lastInspection",
            qualifiedByName = "stringToDate")
    @Mapping(target = "lastOilChange",
            source = "lastOilChange",
            qualifiedByName = "stringToDate")
    @Mapping(target = "lastTireChange",
            source = "lastTireChange",
            qualifiedByName = "stringToDate")
    Car toEntity(CarDTO dto);
}
