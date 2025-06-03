// ─────────────────────────────────────────────────────────────────────────────
//  UsersView.java – refactor pentru SessionService + BaseProtectedView
// ─────────────────────────────────────────────────────────────────────────────
package hirjanfabian.bachelors.ui;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import hirjanfabian.bachelors.dto.UserDTO;
import hirjanfabian.bachelors.ui.base.BaseProtectedView;
import org.springframework.http.*;

import java.util.*;

@Route("users")
public class UsersView extends BaseProtectedView {

    private final Grid<UserDTO> grid = new Grid<>(UserDTO.class, false);
    private       List<UserDTO> users = Collections.emptyList();

    public UsersView() {
        super("users");

        configureGrid();

        Button add = new Button("Register user", e -> openRegisterDialog());
        add.addClassName("add-user");

        VerticalLayout content = new VerticalLayout(add, grid);
        content.setSizeFull(); content.setPadding(true); content.setSpacing(true);

        setMainContent(content);

        loadUsers();
    }

    /*────────────────────────────────────────────
     * Grid
     *───────────────────────────────────────────*/
    private void configureGrid() {
        grid.addClassName("users-grid");
        grid.setHeight("400px");

        grid.addColumn(UserDTO::getUsername) .setHeader("Username");
        grid.addColumn(UserDTO::getEmail)    .setHeader("Email");
        grid.addColumn(UserDTO::getFirstName).setHeader("First name");
        grid.addColumn(UserDTO::getLastName) .setHeader("Last name");
        grid.addColumn(UserDTO::getRole)     .setHeader("Role");

        grid.addComponentColumn(u -> {
            Button del = new Button("Delete", e -> deleteUser(u));
            del.addClassName("action-button");
            return del;
        }).setHeader("Actions");
    }

    /*────────────────────────────────────────────
     * Register dialog
     *───────────────────────────────────────────*/
    private void openRegisterDialog() {
        Dialog dlg = new Dialog();
        dlg.setHeaderTitle("Register user");

        TextField  user = new TextField("Username");
        TextField  pass = new TextField("Password");
        TextField  mail = new TextField("Email");
        TextField  fn   = new TextField("First name");
        TextField  ln   = new TextField("Last name");
        ComboBox<String> role = new ComboBox<>("Role", "USER", "ADMIN");

        Button ok = new Button("Save", e -> {
            if (user.isEmpty() || pass.isEmpty() || mail.isEmpty() || role.isEmpty()) {
                Notification.show("Completează câmpurile obligatorii!");
                return;
            }
            UserDTO dto = new UserDTO();
            dto.setUsername(user.getValue());
            dto.setPassword(pass.getValue());
            dto.setEmail(mail.getValue());
            dto.setFirstName(fn.getValue());
            dto.setLastName(ln.getValue());
            dto.setRole(role.getValue());
            register(dto);
            dlg.close();
        });
        dlg.add(new VerticalLayout(user, pass, mail, fn, ln, role, ok));
        dlg.open();
    }

    /*────────────────────────────────────────────
     * HTTP helper
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

    /*────────────────────────────────────────────
     * CRUD
     *───────────────────────────────────────────*/
    private void loadUsers() {
        try {
            ResponseEntity<UserDTO[]> r =
                    req("/api/users", HttpMethod.GET, null, UserDTO[].class);
            if (r.getStatusCode().is2xxSuccessful() && r.getBody() != null) {
                users = Arrays.asList(r.getBody());
                grid.setItems(users);
            } else Notification.show("Eroare la listare: " + r.getStatusCode());
        } catch (Exception e) {
            Notification.show("Eroare: " + e.getMessage());
        }
    }

    private void register(UserDTO dto) {
        try {
            ResponseEntity<Void> r =
                    req("/api/register", HttpMethod.POST, dto, Void.class);
            if (r.getStatusCode().is2xxSuccessful()) {
                Notification.show("Utilizator creat!");
                loadUsers();
            } else Notification.show("Eroare la creare: " + r.getStatusCode());
        } catch (Exception e) {
            Notification.show("Eroare: " + e.getMessage());
        }
    }

    private void deleteUser(UserDTO u) {
        try {
            ResponseEntity<Void> r =
                    req("/api/users/" + u.getId(), HttpMethod.DELETE, null, Void.class);
            if (r.getStatusCode().is2xxSuccessful()) {
                Notification.show("Șters!");
                loadUsers();
            } else Notification.show("Eroare la ștergere: " + r.getStatusCode());
        } catch (Exception e) {
            Notification.show("Eroare: " + e.getMessage());
        }
    }
}
