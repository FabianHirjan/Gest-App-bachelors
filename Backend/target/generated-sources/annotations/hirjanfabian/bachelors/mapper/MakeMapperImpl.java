package hirjanfabian.bachelors.mapper;

import hirjanfabian.bachelors.dto.CarMakeDTO;
import hirjanfabian.bachelors.dto.CarModelDTO;
import hirjanfabian.bachelors.entities.CarMakes;
import hirjanfabian.bachelors.entities.CarModels;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-04-30T15:10:42+0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 23.0.2 (Oracle Corporation)"
)
@Component
public class MakeMapperImpl implements MakeMapper {

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public CarMakeDTO toDto(CarMakes entity) {
        if ( entity == null ) {
            return null;
        }

        CarMakeDTO carMakeDTO = new CarMakeDTO();

        carMakeDTO.setId( entity.getId() );
        carMakeDTO.setMake( entity.getMake() );

        return carMakeDTO;
    }

    @Override
    public CarMakes toEntity(CarMakeDTO dto) {
        if ( dto == null ) {
            return null;
        }

        CarMakes carMakes = new CarMakes();

        carMakes.setId( dto.getId() );
        carMakes.setMake( dto.getMake() );
        carMakes.setModels( carModelDTOListToCarModelsList( dto.getModels() ) );

        return carMakes;
    }

    protected List<CarModels> carModelDTOListToCarModelsList(List<CarModelDTO> list) {
        if ( list == null ) {
            return null;
        }

        List<CarModels> list1 = new ArrayList<CarModels>( list.size() );
        for ( CarModelDTO carModelDTO : list ) {
            list1.add( modelMapper.toEntity( carModelDTO ) );
        }

        return list1;
    }
}
