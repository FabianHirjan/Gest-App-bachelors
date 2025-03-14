package hirjanfabian.bachelors.mapper;


import hirjanfabian.bachelors.dto.CarDTO;
import hirjanfabian.bachelors.dto.CarMakeDTO;
import hirjanfabian.bachelors.dto.CarModelDTO;
import hirjanfabian.bachelors.entities.Car;

public class CarMapper {

    public static CarDTO toCarDTO(Car car) {
        if (car == null) {
            return null;
        }

        CarDTO dto = new CarDTO();
        dto.setId(car.getId());
        dto.setLicensePlate(car.getLicensePlate());
        dto.setMileage(car.getMileage());
        dto.setVin(car.getVIN());

        // Mapare pentru CarMake
        CarMakeDTO makeDTO = new CarMakeDTO();
        makeDTO.setId(car.getCarMake().getId());
        makeDTO.setMake(car.getCarMake().getMake());
        dto.setCarMake(makeDTO);

        // Mapare pentru CarModel
        CarModelDTO modelDTO = new CarModelDTO();
        modelDTO.setId(car.getCarModel().getId());
        modelDTO.setModel(car.getCarModel().getModel());
        dto.setCarModel(modelDTO);

        return dto;
    }
}
