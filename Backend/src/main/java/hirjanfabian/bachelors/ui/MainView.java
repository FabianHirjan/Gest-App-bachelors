// ─────────────────────────────────────────────────────────────────────────────
//  MainView.java – root route („/”) care afișează modalul LoginDialog
// ─────────────────────────────────────────────────────────────────────────────
package hirjanfabian.bachelors.ui;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import hirjanfabian.bachelors.ui.security.LoginDialog;
import hirjanfabian.bachelors.ui.security.SessionService;

/**
 * Ruta „/”.  Dacă există deja JWT în sesiune → redirect către <code>index</code>,
 * altfel deschide {@link LoginDialog}.  Nu conţine alt UI.
 */
@Route("")
public class MainView extends VerticalLayout implements BeforeEnterObserver {

    @Override
    public void beforeEnter(BeforeEnterEvent e) {
        SessionService sess = SessionService.get();

        if (sess.getJwt().isPresent()) {
            e.forwardTo("index");
        } else {
            // afişează modalul şi, la succes, navighează la dashboard
            new LoginDialog(sess, () -> e.getUI().navigate("index")).open();
        }
    }
}
