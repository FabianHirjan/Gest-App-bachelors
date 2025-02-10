package hirjanfabian.gestapp.entities;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "cars")
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String type;
    private String licensePlate;
    private Long mileage;

    private Date lastOilChange;

    private Date insuranceExpirationDate;

    private Date itpExpirationDate;

    @OneToOne
    @JoinColumn(name = "driver_id", referencedColumnName = "id")
    @JsonBackReference
    private User driver;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public Long getMileage() {
        return mileage;
    }

    public void setMileage(Long mileage) {
        this.mileage = mileage;
    }

    public Date getLastOilChange() {
        return lastOilChange;
    }

    public void setLastOilChange(Date lastOilChange) {
        this.lastOilChange = lastOilChange;
    }

    public Date getInsuranceExpirationDate() {
        return insuranceExpirationDate;
    }

    public void setInsuranceExpirationDate(Date insuranceExpirationDate) {
        this.insuranceExpirationDate = insuranceExpirationDate;
    }

    public Date getItpExpirationDate() {
        return itpExpirationDate;
    }

    public void setItpExpirationDate(Date itpExpirationDate) {
        this.itpExpirationDate = itpExpirationDate;
    }

    public User getDriver() {
        return driver;
    }

    public void setDriver(User driver) {
        this.driver = driver;
    }
}
