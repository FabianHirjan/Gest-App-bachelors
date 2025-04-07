package hirjanfabian.bachelors.mapper;

import hirjanfabian.bachelors.dto.CarModelDTO;
import hirjanfabian.bachelors.entities.CarModels;
import hirjanfabian.bachelors.utils.CycleAvoidingMappingContext;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-04-06T17:13:35+0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 23.0.2 (Oracle Corporation)"
)
public class ModelMapperImpl implements ModelMapper {

    private final MakeMapper makeMapper = MakeMapper.INSTANCE;

    @Override
    public CarModelDTO toCarModelDTO(CarModels entity, CycleAvoidingMappingContext context) {
        if ( entity == null ) {
            return null;
        }

        CarModelDTO carModelDTO = new CarModelDTO();

        carModelDTO.setCarMake( makeMapper.toCarMakeDTO( entity.getCarMake() ) );
        carModelDTO.setId( entity.getId() );
        carModelDTO.setModel( entity.getModel() );

        return carModelDTO;
    }

    @Override
    public CarModels toCarModel(CarModelDTO dto, CycleAvoidingMappingContext context) {
        if ( dto == null ) {
            return null;
        }

        CarModels carModels = new CarModels();

        carModels.setId( dto.getId() );
        carModels.setModel( dto.getModel() );
        carModels.setCarMake( makeMapper.toCarMake( dto.getCarMake() ) );

        return carModels;
    }
}
