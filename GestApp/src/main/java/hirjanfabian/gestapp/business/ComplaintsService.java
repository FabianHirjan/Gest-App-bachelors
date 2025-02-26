package hirjanfabian.gestapp.business;

import hirjanfabian.gestapp.entities.Complaints;
import hirjanfabian.gestapp.repositories.ComplaintRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ComplaintsService {
    private final ComplaintRepository complaintsRepository;

    public ComplaintsService(ComplaintRepository complaintsRepository) {
        this.complaintsRepository = complaintsRepository;
    }

    public List<Complaints> getAllComplaints() {
        return complaintsRepository.findAll();
    }

    public Complaints getComplaintById(Long id) {
        return complaintsRepository.findById(id).orElse(null);
    }

    public Complaints saveComplaint(Complaints complaint) {
        return complaintsRepository.save(complaint);
    }

    public void deleteComplaint(Long id) {
        complaintsRepository.deleteById(id);
    }
}
