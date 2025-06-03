package hirjanfabian.bachelors.mapper;

import hirjanfabian.bachelors.dto.UserDTO;
import hirjanfabian.bachelors.entities.User;

public class UserMapper {

    public static User toEntity(UserDTO dto) {
        if (dto == null) {
            return null;
        }
        User entity = new User();

        entity.setUsername(dto.getUsername());
        entity.setFirstName(dto.getFirstName());
        entity.setLastName(dto.getLastName());
        entity.setEmail(dto.getEmail());
        entity.setPassword(dto.getPassword());  // dacă primeşti hash din DTO
        entity.setRole(dto.getRole());
        return entity;
    }

    public static UserDTO toDTO(User user) {
        if (user == null) {
            return null;
        }
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setEmail(user.getEmail());
        // Dacă nu vrei să trimiţi parola în JSON, comentează sau ignoră linia de mai jos:
        // dto.setPassword(user.getPassword());
        dto.setRole(user.getRole());
        return dto;
    }
}
