// ─────────────────────────────────────────────────────────────────────────────
//  DashboardView.java – versiune refactorizată pentru SessionService
// ─────────────────────────────────────────────────────────────────────────────
package hirjanfabian.bachelors.ui;

import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import hirjanfabian.bachelors.ui.base.BaseProtectedView;

/**
 * Pagina principală după autentificare. Nu conține logică complexă –
 * doar salut + linkuri side-nav.
 */
@Route("index")
public class DashboardView extends BaseProtectedView {

    public DashboardView() {
        super("app/index");

        VerticalLayout main = new VerticalLayout(
                new Span("Welcome to the Admin Dashboard")
        );
        main.setSizeFull();
        main.setPadding(true);

        setMainContent(main);
    }
}
