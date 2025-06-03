package hirjanfabian.bachelors.dto;


import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ReceiptAnalysis {
    private String description;
    private double liters;
    private double pricePerLiter;
}