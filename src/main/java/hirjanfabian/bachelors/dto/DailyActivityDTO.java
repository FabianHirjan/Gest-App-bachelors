package hirjanfabian.bachelors.dto;

import java.time.LocalDate;

public class DailyActivityDTO {
    private Long id;
    private String description;
    private long kilometers;
    private double fuelConsumption;
    private LocalDate date;
    private boolean approved; // Dacă dorești să-l returnezi în răspuns

    // Getters și Setters

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public long getKilometers() {
        return kilometers;
    }
    public void setKilometers(long kilometers) {
        this.kilometers = kilometers;
    }
    public double getFuelConsumption() {
        return fuelConsumption;
    }
    public void setFuelConsumption(double fuelConsumption) {
        this.fuelConsumption = fuelConsumption;
    }
    public LocalDate getDate() {
        return date;
    }
    public void setDate(LocalDate date) {
        this.date = date;
    }
    public boolean isApproved() {
        return approved;
    }
    public void setApproved(boolean approved) {
        this.approved = approved;
    }
}
