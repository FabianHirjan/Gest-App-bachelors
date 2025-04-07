package hirjanfabian.bachelors.mapper;

import hirjanfabian.bachelors.dto.CarMakeDTO;
import hirjanfabian.bachelors.dto.CarModelDTO;
import hirjanfabian.bachelors.entities.CarMakes;
import hirjanfabian.bachelors.entities.CarModels;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-04-06T17:13:35+0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 23.0.2 (Oracle Corporation)"
)
public class MakeMapperImpl implements MakeMapper {

    private final ModelMapper modelMapper = ModelMapper.INSTANCE;

    @Override
    public CarMakeDTO toCarMakeDTO(CarMakes make) {
        if ( make == null ) {
            return null;
        }

        CarMakeDTO carMakeDTO = new CarMakeDTO();

        carMakeDTO.setModels( carModelsListToCarModelDTOList( make.getModels() ) );
        carMakeDTO.setId( make.getId() );
        carMakeDTO.setMake( make.getMake() );

        return carMakeDTO;
    }

    @Override
    public CarMakes toCarMake(CarMakeDTO dto) {
        if ( dto == null ) {
            return null;
        }

        CarMakes carMakes = new CarMakes();

        carMakes.setId( dto.getId() );
        carMakes.setMake( dto.getMake() );
        carMakes.setModels( carModelDTOListToCarModelsList( dto.getModels() ) );

        return carMakes;
    }

    protected List<CarModelDTO> carModelsListToCarModelDTOList(List<CarModels> list) {
        if ( list == null ) {
            return null;
        }

        List<CarModelDTO> list1 = new ArrayList<CarModelDTO>( list.size() );
        for ( CarModels carModels : list ) {
            list1.add( modelMapper.toCarModelDTO( carModels ) );
        }

        return list1;
    }

    protected List<CarModels> carModelDTOListToCarModelsList(List<CarModelDTO> list) {
        if ( list == null ) {
            return null;
        }

        List<CarModels> list1 = new ArrayList<CarModels>( list.size() );
        for ( CarModelDTO carModelDTO : list ) {
            list1.add( modelMapper.toCarModel( carModelDTO ) );
        }

        return list1;
    }
}
