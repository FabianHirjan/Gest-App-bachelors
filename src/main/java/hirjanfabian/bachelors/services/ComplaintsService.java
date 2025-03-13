package hirjanfabian.bachelors.services;


import hirjanfabian.bachelors.entities.Complaints;
import hirjanfabian.bachelors.repositories.ComplaintsRepository;
import org.springframework.stereotype.Service;

@Service
public class ComplaintsService {
private final ComplaintsRepository complaintsRepository;

    public ComplaintsService(ComplaintsRepository complaintsRepository) {
        this.complaintsRepository = complaintsRepository;
    }

    public void createComplaint(Complaints complaints) {
        complaintsRepository.save(complaints);
    }
}
