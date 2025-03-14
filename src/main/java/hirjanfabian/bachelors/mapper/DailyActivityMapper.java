package hirjanfabian.bachelors.mapper;

import hirjanfabian.bachelors.dto.DailyActivityDTO;
import hirjanfabian.bachelors.entities.DailyActivity;

public class DailyActivityMapper {

    public static DailyActivity toEntity(DailyActivityDTO dto) {
        if (dto == null) {
            return null;
        }
        DailyActivity entity = new DailyActivity();
        // Nu setăm id-ul – e generat la salvare
        entity.setDescription(dto.getDescription());
        entity.setKilometers(dto.getKilometers());
        entity.setFuelConsumption(dto.getFuelConsumption());
        entity.setDate(dto.getDate());
        // Câmpul approved se setează în service (de exemplu, false la creare)
        return entity;
    }

    public static DailyActivityDTO toDTO(DailyActivity entity) {
        if (entity == null) {
            return null;
        }
        DailyActivityDTO dto = new DailyActivityDTO();
        dto.setId(entity.getId());
        dto.setDescription(entity.getDescription());
        dto.setKilometers(entity.getKilometers());
        dto.setFuelConsumption(entity.getFuelConsumption());
        dto.setDate(entity.getDate());
        dto.setApproved(entity.isApproved());
        return dto;
    }
}
