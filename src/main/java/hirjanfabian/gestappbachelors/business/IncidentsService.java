package hirjanfabian.gestappbachelors.business;
import hirjanfabian.gestappbachelors.data.Incidents;
import hirjanfabian.gestappbachelors.data.IncidentsRepository;
import org.springframework.stereotype.Service;


@Service
public class IncidentsService {
    private final IncidentsRepository incidentsRepository;

    public IncidentsService(IncidentsRepository incidentsRepository) {
        this.incidentsRepository = incidentsRepository;
    }

    public Iterable<Incidents> findAll() {
        return incidentsRepository.findAll();
    }

    public void save(Incidents incidents) {
        incidentsRepository.save(incidents);
    }

    public void deleteIncidents(Long id) {
        incidentsRepository.deleteById(id);
    }


    public void updateIncidents(Incidents incidents) {
        incidentsRepository.save(incidents);
    }

    public void deleteIncidents(Incidents incidents) {
        incidentsRepository.delete(incidents);
    }

    public void deleteAllIncidents() {
        incidentsRepository.deleteAll();
    }

    public void updateAllIncidents(Iterable<Incidents> incidents) {
        for(Incidents incident : incidents) {
            incident.setRepairCost((int) (incident.getRepairCost() + 0.10 * incident.getRepairCost()));
        }
        incidentsRepository.saveAll(incidents);
    }
}
