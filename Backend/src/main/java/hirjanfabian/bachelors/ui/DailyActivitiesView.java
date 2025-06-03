// ─────────────────────────────────────────────────────────────────────────────
//  DailyActivitiesView.java  –  refactor pentru SessionService + BaseProtectedView
// ─────────────────────────────────────────────────────────────────────────────
package hirjanfabian.bachelors.ui;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import hirjanfabian.bachelors.dto.DailyActivityDTO;
import hirjanfabian.bachelors.dto.UserDTO;
import hirjanfabian.bachelors.ui.base.BaseProtectedView;
import org.springframework.http.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Afișează activitățile zilnice, cu filtrare după câmpul <em>approved</em>
 * și posibilitatea de aprobare rapidă.
 */
@Route("activities")
public class DailyActivitiesView extends BaseProtectedView {

    private final Grid<DailyActivityDTO> grid = new Grid<>(DailyActivityDTO.class, false);
    private final ComboBox<String>       approvedFilter = new ComboBox<>("Approved?");
    private       List<DailyActivityDTO> activities     = Collections.emptyList();

    public DailyActivitiesView() {
        super("activities");

        configureGrid();

        approvedFilter.setItems("All", "Approved", "Not Approved");
        approvedFilter.setValue("All");
        approvedFilter.addValueChangeListener(e -> applyFilter());

        VerticalLayout content = new VerticalLayout(approvedFilter, grid);
        content.setSizeFull(); content.setPadding(true); content.setSpacing(true);

        setMainContent(content);

        loadActivities();
    }

    /*────────────────────────────────────────────
     * Grid
     *───────────────────────────────────────────*/
    private void configureGrid() {
        grid.addClassName("activities-grid");
        grid.setHeight("600px");

        grid.addColumn(DailyActivityDTO::getDescription).setHeader("Description");
        grid.addColumn(DailyActivityDTO::getKilometers).setHeader("Km");
        grid.addColumn(DailyActivityDTO::getFuelConsumption).setHeader("Fuel (l/100 km)");
        grid.addColumn(DailyActivityDTO::getDate).setHeader("Date");
        grid.addColumn(DailyActivityDTO::isApproved).setHeader("Approved");
        grid.addColumn(DailyActivityDTO::getPostedBy).setHeader("Posted by");

        grid.addColumn(dto -> String.format("%s %s (%s)",
                        dto.getCarModel(), dto.getCarBrand(), dto.getCarRegistration()))
                .setHeader("Car");

        grid.addComponentColumn(dto -> {
            Button approve = new Button("Approve", e -> approve(dto.getId()));
            approve.setEnabled(!dto.isApproved());

            Button view = new Button("View",   e -> openDetailsDialog(dto));
            HorizontalLayout acts = new HorizontalLayout(view, approve);
            acts.setSpacing(true);
            return acts;
        }).setHeader("Actions");
    }

    /*────────────────────────────────────────────
     * Filter + dialog
     *───────────────────────────────────────────*/
    private void applyFilter() {
        if (activities == null) return;
        String v = approvedFilter.getValue();

        List<DailyActivityDTO> filtered = switch (v) {
            case "Approved"     -> activities.stream()
                    .filter(DailyActivityDTO::isApproved)
                    .collect(Collectors.toList());
            case "Not Approved" -> activities.stream()
                    .filter(a -> !a.isApproved())
                    .collect(Collectors.toList());
            default             -> activities;
        };
        grid.setItems(filtered);
    }

    private void openDetailsDialog(DailyActivityDTO d) {
        Dialog dlg = new Dialog();
        dlg.setHeaderTitle("Activity details");

        FormLayout f = new FormLayout();
        f.addFormItem(new H2(d.getDescription()),                     "Description");
        f.addFormItem(new H2(String.valueOf(d.getKilometers())),      "Kilometers");
        f.addFormItem(new H2(String.valueOf(d.getFuelConsumption())), "Fuel Consumption");
        f.addFormItem(new H2(String.valueOf(d.getDate())),            "Date");
        f.addFormItem(new H2(d.isApproved() ? "Yes" : "No"),          "Approved");
        f.addFormItem(new H2(d.getPostedBy()),                        "Posted by");
        f.addFormItem(new H2(String.format("%s %s (%s)",
                d.getCarModel(), d.getCarBrand(), d.getCarRegistration())), "Car");

        dlg.add(f);
        dlg.setWidth("420px");
        dlg.getFooter().add(new Button("Close", e -> dlg.close()));
        dlg.open();
    }

    /*────────────────────────────────────────────
     * REST helpers + CRUD
     *───────────────────────────────────────────*/
    private <T> ResponseEntity<T> req(String path,
                                      HttpMethod m,
                                      Object body,
                                      Class<T> resp) {
        HttpHeaders h = new HttpHeaders();
        h.setBearerAuth(jwt());
        h.setContentType(MediaType.APPLICATION_JSON);
        return rest.exchange("http://localhost:8080" + path,
                m, new HttpEntity<>(body, h), resp);
    }

    private void loadActivities() {
        try {
            ResponseEntity<DailyActivityDTO[]> r =
                    req("/api/dailyactivities", HttpMethod.GET, null, DailyActivityDTO[].class);
            if (r.getStatusCode().is2xxSuccessful() && r.getBody() != null) {
                activities = Arrays.asList(r.getBody());
                applyFilter();
            }
        } catch (Exception e) {
            Notification.show("Error loading activities: " + e.getMessage());
        }
    }

    private void approve(Long id) {
        try {
            ResponseEntity<Void> r =
                    req("/api/dailyactivities/approve/" + id, HttpMethod.POST, null, Void.class);
            if (r.getStatusCode().is2xxSuccessful()) {
                Notification.show("Approved!");
                loadActivities();
            } else Notification.show("Could not approve: " + r.getStatusCode());
        } catch (Exception e) {
            Notification.show("Error: " + e.getMessage());
        }
    }
}
