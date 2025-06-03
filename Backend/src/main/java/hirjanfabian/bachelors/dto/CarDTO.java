package hirjanfabian.bachelors.dto;

import lombok.Getter;
import lombok.Setter;
@Getter @Setter
public class CarDTO {

    private Long id;
    private String licensePlate;
    private long mileage;
    private String vin;
    private String lastInspection;
    private String lastOilChange;
    private String lastTireChange;
    private String insuranceExpiration;
    private CarMakeDTO carMake;
    private CarModelDTO carModel;
    private UserDTO driver;
    private Integer year;
    private Double estimatedPrice;

    /* ─────── NEW ─────── */
    private Boolean insuranceOverdue;
    private Boolean insuranceDueSoon;
    private Boolean inspectionOverdue;
    private Boolean inspectionDueSoon;
    private Boolean oilOverdue;
    private Boolean oilDueSoon;
    private Boolean tireOverdue;
    private Boolean tireDueSoon;

    /** Helper: există cel puțin un flag critic */
    public boolean isAnyOverdue() {
        return Boolean.TRUE.equals(insuranceOverdue)
                || Boolean.TRUE.equals(inspectionOverdue)
                || Boolean.TRUE.equals(oilOverdue)
                || Boolean.TRUE.equals(tireOverdue);
    }
}
