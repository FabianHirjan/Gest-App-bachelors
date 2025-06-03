package hirjanfabian.bachelors.ui.security;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.server.VaadinSession;
import lombok.Getter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.Optional;

/** Gestionază JWT‑ul şi apelul /api/auth/login. Unic pe fiecare UI. */
public class SessionService {

    private static final String JWT_KEY  = "jwt";
    private static final String USER_KEY = "username";

    @Getter private final RestTemplate rest = new RestTemplate();

    /* singleton per UI */
    private SessionService() {}
    public static SessionService get() {
        VaadinSession sess = VaadinSession.getCurrent();
        if (sess == null) throw new IllegalStateException("No VaadinSession");
        SessionService svc = sess.getAttribute(SessionService.class);
        if (svc == null) {
            svc = new SessionService();
            sess.setAttribute(SessionService.class, svc);
        }
        return svc;
    }


    // ── getter/setter sesiune ──
    public Optional<String> getJwt()       { return Optional.ofNullable((String) VaadinSession.getCurrent().getAttribute(JWT_KEY)); }
    public Optional<String> getUsername()  { return Optional.ofNullable((String) VaadinSession.getCurrent().getAttribute(USER_KEY)); }

    public void saveSession(String jwt, String username) {
        VaadinSession.getCurrent().setAttribute(JWT_KEY,  jwt);
        VaadinSession.getCurrent().setAttribute(USER_KEY, username);
    }
    public void clear() { VaadinSession.getCurrent().close(); }

    // ── apel login spre backend ──
    public boolean login(String user, String pass) {
        try {
            ResponseEntity<Map> resp = rest.postForEntity(
                    "http://localhost:8080/api/auth/login",
                    Map.of("username", user, "password", pass), Map.class);
            if (resp.getStatusCode().is2xxSuccessful() && resp.getBody() != null) {
                saveSession((String) resp.getBody().get("token"), user);
                return true;
            }
        } catch (Exception ignored) {}
        return false;
    }
}