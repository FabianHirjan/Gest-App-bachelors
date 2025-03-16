package hirjanfabian.bachelors.dto;


import java.util.List;

public class CarMakeDTO {
    private Long id;
    private String make;

    // Dacă nu ai nevoie de lista de modele în acest context,
    // poți să o omitezi pentru a evita recursivitatea.
    private List<CarModelDTO> models;

    public List<CarModelDTO> getModels() {
        return models;
    }

    public void setModels(List<CarModelDTO> models) {
        this.models = models;
    }
    // Getters și Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }
}
