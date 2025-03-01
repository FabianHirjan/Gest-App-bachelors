package hirjanfabian.gestapp.business;

import hirjanfabian.gestapp.entities.Complaints;
import hirjanfabian.gestapp.repositories.ComplaintRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ComplaintsService {
    private final ComplaintRepository complaintsRepository;

    public ComplaintsService(ComplaintRepository complaintsRepository) {
        this.complaintsRepository = complaintsRepository;
    }

    /**
     * Retrieves all complaints from the repository.
     *
     * @return a list of all complaints
     */
    public List<Complaints> getAllComplaints() {
        return complaintsRepository.findAll();
    }

    /**
     * Retrieves a complaint by its unique identifier.
     *
     * @param id the unique identifier of the complaint to retrieve
     * @return an {@code Optional} containing the complaint if found, or an empty {@code Optional} if no complaint exists with the given id
     */
    public Optional<Complaints> getComplaintById(Long id) {
        return complaintsRepository.findById(id);
    }

    /**
     * Saves a given complaint to the repository if the complaint is valid.
     * A complaint is considered valid if it has a non-null title and description.
     *
     * @param complaint the complaint to be saved. Must not be null and should have a non-null title and description.
     * @return an {@code Optional} containing the saved complaint if valid, or {@code Optional.empty()} if the complaint is invalid.
     */
    public Optional<Complaints> saveComplaint(Complaints complaint) {
        if (complaint == null || complaint.getTitle() == null || complaint.getDescription() == null) {
            return Optional.empty();
        }
        return Optional.of(complaintsRepository.save(complaint));
    }

    /**
     * Deletes a complaint based on the provided complaint ID. If the complaint with the specified ID exists,
     * it will be deleted from the repository and the method will return true. If the complaint does not exist,
     * the method will return false.
     *
     * @param id the unique identifier of the complaint to be deleted
     * @return true if the complaint was successfully deleted, false otherwise
     */
    public boolean deleteComplaint(Long id) {
        if (complaintsRepository.existsById(id)) {
            complaintsRepository.deleteById(id);
            return true;
        }
        return false;
    }
}