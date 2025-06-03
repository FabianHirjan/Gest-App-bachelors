package hirjanfabian.bachelors.mapper;

import hirjanfabian.bachelors.dto.CarDTO;
import hirjanfabian.bachelors.entities.Car;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(
        componentModel = "spring",
        uses = { MakeMapper.class, ModelMapper.class, UserMapper.class, DateMapper.class }
)
public interface CarMapper {

    /* ============= Entity → DTO ============= */

    @Mapping(target = "vin",  source = "VIN")
    @Mapping(target = "insuranceExpiration",
            source = "insuranceExpiration",
            qualifiedByName = "dateToString")
    @Mapping(target = "driver", source = "user")
    @Mapping(target = "lastInspection",
            source = "lastInspection",
            qualifiedByName = "dateToString")
    @Mapping(target = "lastOilChange",
            source = "lastOilChange",
            qualifiedByName = "dateToString")
    @Mapping(target = "lastTireChange",
            source = "lastTireChange",
            qualifiedByName = "dateToString")

    /* ─────── NEW flags ─────── */
    @Mapping(target = "insuranceOverdue",  source = "insuranceOverdue")
    @Mapping(target = "insuranceDueSoon",  source = "insuranceDueSoon")
    @Mapping(target = "inspectionOverdue", source = "inspectionOverdue")
    @Mapping(target = "inspectionDueSoon", source = "inspectionDueSoon")
    @Mapping(target = "oilOverdue",        source = "oilOverdue")
    @Mapping(target = "oilDueSoon",        source = "oilDueSoon")
    @Mapping(target = "tireOverdue",       source = "tireOverdue")
    @Mapping(target = "tireDueSoon",       source = "tireDueSoon")
    CarDTO toDto(Car entity);

    /* ============= DTO → Entity ============= */

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

    /* ─────── IGNORĂM flag-urile la persistare ─────── */
    @Mapping(target = "insuranceOverdue",  ignore = true)
    @Mapping(target = "insuranceDueSoon",  ignore = true)
    @Mapping(target = "inspectionOverdue", ignore = true)
    @Mapping(target = "inspectionDueSoon", ignore = true)
    @Mapping(target = "oilOverdue",        ignore = true)
    @Mapping(target = "oilDueSoon",        ignore = true)
    @Mapping(target = "tireOverdue",       ignore = true)
    @Mapping(target = "tireDueSoon",       ignore = true)
    Car toEntity(CarDTO dto);

    /* deja aveai: */
    List<CarDTO> toDtoList(List<Car> entities);
}
