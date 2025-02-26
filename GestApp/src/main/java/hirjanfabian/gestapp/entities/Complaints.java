package hirjanfabian.gestapp.entities;


import jakarta.persistence.*;

import java.util.Optional;

@Entity
@Table(name = "complaints")
public class Complaints{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "complaint_against")
    private User complaintAgainst;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getComplaintAgainst() {
        return complaintAgainst;
    }

    public void setComplaintAgainst(User complaintAgainst) {
        this.complaintAgainst = complaintAgainst;
    }
}
