package hirjanfabian.bachelors.mapper;

import hirjanfabian.bachelors.dto.CarModelDTO;
import hirjanfabian.bachelors.entities.CarModels;

public class ModelMapper {
    public static CarModels toCarModel(CarModelDTO dto) {
        if (dto == null) {
            return null;
        }
        CarModels entity = new CarModels();
        entity.setModel(dto.getModel());
        return entity;
    }

    public static CarModelDTO toCarModel(CarModels entity) {
        if (entity == null) {
            return null;
        }
        CarModelDTO dto = new CarModelDTO();
        dto.setId(entity.getId());
        dto.setModel(entity.getModel());
        return dto;
    }
}
