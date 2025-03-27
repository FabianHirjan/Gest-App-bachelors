package hirjanfabian.bachelors.mapper;

import hirjanfabian.bachelors.dto.CarDTO;
import hirjanfabian.bachelors.dto.CarMakeDTO;
import hirjanfabian.bachelors.dto.CarModelDTO;
import hirjanfabian.bachelors.dto.UserDTO;
import hirjanfabian.bachelors.entities.Car;
import hirjanfabian.bachelors.entities.CarMakes;
import hirjanfabian.bachelors.entities.CarModels;
import hirjanfabian.bachelors.entities.User;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CarMapper {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS");

    public static CarDTO toCarDTO(Car car) {
        if (car == null) {
            return null;
        }

        CarDTO dto = new CarDTO();
        dto.setId((long) car.getId().intValue()); // Convert Long to int (beware of truncation if IDs exceed Integer.MAX_VALUE)
        dto.setLicensePlate(car.getLicensePlate());
        dto.setMileage((int) car.getMileage()); // Convert long to int (beware of truncation)
        dto.setVin(car.getVIN());
        dto.setInsuranceExpiration(car.getInsuranceExpiration() != null ? car.getInsuranceExpiration().format(DATE_TIME_FORMATTER) : null);
        dto.setLastInspection(car.getLastInspection() != null ? car.getLastInspection().format(DATE_TIME_FORMATTER) : null);
        dto.setLastTireChange(car.getLastTireChange() != null ? car.getLastTireChange().format(DATE_TIME_FORMATTER) : null);
        dto.setLastOilChange(car.getLastOilChange() != null ? car.getLastOilChange().format(DATE_TIME_FORMATTER) : null);

        CarMakeDTO makeDTO = new CarMakeDTO();
        makeDTO.setId(car.getCarMake().getId());
        makeDTO.setMake(car.getCarMake().getMake());
        dto.setCarMake(makeDTO);

        CarModelDTO modelDTO = new CarModelDTO();
        modelDTO.setId(car.getCarModel().getId());
        modelDTO.setModel(car.getCarModel().getModel());
        dto.setCarModel(modelDTO);

        // Map the user (driver) if present
        if (car.getUser() != null) {
            UserDTO driverDTO = new UserDTO();
            driverDTO.setId(car.getUser().getId());
            driverDTO.setUsername(car.getUser().getUsername());
            driverDTO.setRole(car.getUser().getRole());
            driverDTO.setEmail(car.getUser().getEmail());
            driverDTO.setFirstName(car.getUser().getFirstName());
            driverDTO.setLastName(car.getUser().getLastName());
            dto.setDriver(driverDTO);
        }

        return dto;
    }

    public static Car toCar(CarDTO dto) {
        if (dto == null) {
            return null;
        }

        Car car = new Car();
        car.setId((long) dto.getId()); // Convert int to Long
        car.setLicensePlate(dto.getLicensePlate());
        car.setMileage(dto.getMileage()); // int to long is safe
        car.setVIN(dto.getVin());
        car.setInsuranceExpiration(dto.getInsuranceExpiration() != null ? LocalDateTime.parse(dto.getInsuranceExpiration(), DATE_TIME_FORMATTER) : null);
        car.setLastInspection(dto.getLastInspection() != null ? LocalDateTime.parse(dto.getLastInspection(), DATE_TIME_FORMATTER) : null);
        car.setLastTireChange(dto.getLastTireChange() != null ? LocalDateTime.parse(dto.getLastTireChange(), DATE_TIME_FORMATTER) : null);
        car.setLastOilChange(dto.getLastOilChange() != null ? LocalDateTime.parse(dto.getLastOilChange(), DATE_TIME_FORMATTER) : null);

        // Map CarMake
        CarMakes carMake = new CarMakes();
        carMake.setId(dto.getCarMake().getId());
        carMake.setMake(dto.getCarMake().getMake());
        car.setCarMake(carMake);

        // Map CarModel
        CarModels carModel = new CarModels();
        carModel.setId(dto.getCarModel().getId());
        carModel.setModel(dto.getCarModel().getModel());
        car.setCarModel(carModel);

        // Map driver (user) if present
        if (dto.getDriver() != null) {
            User user = new User();
            user.setId(dto.getDriver().getId());
            user.setUsername(dto.getDriver().getUsername());
            user.setRole(dto.getDriver().getRole());
            user.setEmail(dto.getDriver().getEmail());
            user.setFirstName(dto.getDriver().getFirstName());
            user.setLastName(dto.getDriver().getLastName());
            car.setUser(user);
        }

        return car;
    }
}