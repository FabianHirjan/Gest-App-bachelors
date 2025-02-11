package hirjanfabian.gestapp.entities;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "dailyActivity")
public class DailyActivity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String summary;

    private String description;
    private Date date;
    @ManyToOne
    @JoinColumn(name = "car_id")
    private Car car;
    private String roadmap;
    private long startKms;
    private long doneKms;
    private long totalKms;
    private int fuelConsumption;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Transient
    @JsonProperty("driverId")
    public Long getDriverId() {
        if (this.car != null && this.car.getDriver() != null) {
            return this.car.getDriver().getId();
        }
        return null;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public String getRoadmap() {
        return roadmap;
    }

    public void setRoadmap(String roadmap) {
        this.roadmap = roadmap;
    }

    public long getStartKms() {
        return startKms;
    }

    public void setStartKms(long startKms) {
        this.startKms = startKms;
    }

    public long getDoneKms() {
        return doneKms;
    }

    public void setDoneKms(long doneKms) {
        this.doneKms = doneKms;
    }

    public long getTotalKms() {
        return totalKms;
    }

    public void setTotalKms(long totalKms) {
        this.totalKms = totalKms;
    }

    public int getFuelConsumption() {
        return fuelConsumption;
    }

    public void setFuelConsumption(int fuelConsumption) {
        this.fuelConsumption = fuelConsumption;
    }
}
