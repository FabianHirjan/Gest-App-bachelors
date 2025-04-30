package hirjanfabian.bachelors.dto;

import java.util.Date;

public record ChatMessageDTO(
    Long senderId,
    Long receiverId,
    String message,
    Date sentDate
) {}
