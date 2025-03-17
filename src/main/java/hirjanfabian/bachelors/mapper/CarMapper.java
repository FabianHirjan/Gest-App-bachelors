package hirjanfabian.bachelors.mapper;

import hirjanfabian.bachelors.dto.CarDTO;
import hirjanfabian.bachelors.dto.CarMakeDTO;
import hirjanfabian.bachelors.dto.CarModelDTO;
import hirjanfabian.bachelors.entities.Car;
import hirjanfabian.bachelors.entities.CarMakes;
import hirjanfabian.bachelors.entities.CarModels;

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
        dto.setInsuranceExpiration(car.getInsuranceExpiration());
        dto.setLastInspection(car.getLastInspection());
        dto.setLastTireChange(car.getLastTireChange());
        dto.setLastOilChange(car.getLastOilChange());

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

    public static Car toCar(CarDTO dto) {
        if (dto == null) {
            return null;
        }

        Car car = new Car();
        car.setId(dto.getId());
        car.setLicensePlate(dto.getLicensePlate());
        car.setMileage(dto.getMileage());
        car.setVIN(dto.getVin());
        car.setInsuranceExpiration(dto.getInsuranceExpiration());
        car.setLastInspection(dto.getLastInspection());
        car.setLastTireChange(dto.getLastTireChange());
        car.setLastOilChange(dto.getLastOilChange());

        // Mapare pentru CarMake
        CarMakes carMake = new CarMakes();
        carMake.setId(dto.getCarMake().getId());
        carMake.setMake(dto.getCarMake().getMake());
        car.setCarMake(carMake);

        // Mapare pentru CarModel
        CarModels carModel = new CarModels();
        carModel.setId(dto.getCarModel().getId());
        carModel.setModel(dto.getCarModel().getModel());
        car.setCarModel(carModel);

        return car;
    }
}