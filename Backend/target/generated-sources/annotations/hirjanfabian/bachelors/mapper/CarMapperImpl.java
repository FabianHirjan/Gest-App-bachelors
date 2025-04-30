package hirjanfabian.bachelors.mapper;

import hirjanfabian.bachelors.dto.CarDTO;
import hirjanfabian.bachelors.entities.Car;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-04-30T15:06:03+0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 23.0.2 (Oracle Corporation)"
)
@Component
public class CarMapperImpl implements CarMapper {

    @Autowired
    private MakeMapper makeMapper;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private DateMapper dateMapper;

    @Override
    public CarDTO toDto(Car entity) {
        if ( entity == null ) {
            return null;
        }

        CarDTO carDTO = new CarDTO();

        carDTO.setVin( entity.getVIN() );
        carDTO.setInsuranceExpiration( dateMapper.dateToString( entity.getInsuranceExpiration() ) );
        carDTO.setLastInspection( dateMapper.dateToString( entity.getLastInspection() ) );
        carDTO.setLastOilChange( dateMapper.dateToString( entity.getLastOilChange() ) );
        carDTO.setLastTireChange( dateMapper.dateToString( entity.getLastTireChange() ) );
        carDTO.setId( entity.getId() );
        carDTO.setLicensePlate( entity.getLicensePlate() );
        carDTO.setMileage( entity.getMileage() );
        carDTO.setCarMake( makeMapper.toDto( entity.getCarMake() ) );
        carDTO.setCarModel( modelMapper.toDto( entity.getCarModel() ) );

        return carDTO;
    }

    @Override
    public Car toEntity(CarDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Car car = new Car();

        car.setVIN( dto.getVin() );
        car.setInsuranceExpiration( dateMapper.stringToDate( dto.getInsuranceExpiration() ) );
        car.setLastInspection( dateMapper.stringToDate( dto.getLastInspection() ) );
        car.setLastOilChange( dateMapper.stringToDate( dto.getLastOilChange() ) );
        car.setLastTireChange( dateMapper.stringToDate( dto.getLastTireChange() ) );
        car.setId( dto.getId() );
        car.setCarMake( makeMapper.toEntity( dto.getCarMake() ) );
        car.setCarModel( modelMapper.toEntity( dto.getCarModel() ) );
        car.setLicensePlate( dto.getLicensePlate() );
        car.setMileage( dto.getMileage() );

        return car;
    }
}
