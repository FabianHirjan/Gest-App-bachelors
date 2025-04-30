package hirjanfabian.bachelors.mapper;

import hirjanfabian.bachelors.dto.CarModelDTO;
import hirjanfabian.bachelors.entities.CarMakes;
import hirjanfabian.bachelors.entities.CarModels;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-04-30T15:10:42+0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 23.0.2 (Oracle Corporation)"
)
@Component
public class ModelMapperImpl implements ModelMapper {

    @Override
    public CarModelDTO toDto(CarModels entity) {
        if ( entity == null ) {
            return null;
        }

        CarModelDTO carModelDTO = new CarModelDTO();

        carModelDTO.setMakeId( entityCarMakeId( entity ) );
        carModelDTO.setId( entity.getId() );
        carModelDTO.setModel( entity.getModel() );

        return carModelDTO;
    }

    @Override
    public CarModels toEntity(CarModelDTO dto) {
        if ( dto == null ) {
            return null;
        }

        CarModels carModels = new CarModels();

        carModels.setId( dto.getId() );
        carModels.setModel( dto.getModel() );

        return carModels;
    }

    private Long entityCarMakeId(CarModels carModels) {
        if ( carModels == null ) {
            return null;
        }
        CarMakes carMake = carModels.getCarMake();
        if ( carMake == null ) {
            return null;
        }
        Long id = carMake.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }
}
