package hirjanfabian.bachelors.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Named;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Mapper(componentModel = "spring")
public interface DateMapper {

    String PATTERN = "yyyy-MM-dd HH:mm:ss.SSSSSS";
    DateTimeFormatter FMT = DateTimeFormatter.ofPattern(PATTERN);

    /** LocalDateTime → String */
    @Named("dateToString")
    default String dateToString(LocalDateTime date) {
        return date == null ? null : FMT.format(date);
    }

    /** String → LocalDateTime */
    @Named("stringToDate")
    default LocalDateTime stringToDate(String text) {
        return (text == null || text.isBlank()) ? null
                : LocalDateTime.parse(text, FMT);
    }
}
