package hirjanfabian.bachelors.dto;


public class CarMakeDTO {
    private Long id;
    private String make;

    // Dacă nu ai nevoie de lista de modele în acest context,
    // poți să o omitezi pentru a evita recursivitatea.

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
