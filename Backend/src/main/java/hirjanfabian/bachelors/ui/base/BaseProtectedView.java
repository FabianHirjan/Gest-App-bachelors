package hirjanfabian.bachelors.ui.base;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import hirjanfabian.bachelors.ui.security.SessionService;
import lombok.Getter;
import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Getter
public abstract class BaseProtectedView extends AppLayout implements BeforeEnterObserver {

    protected final RestTemplate rest;
    private final SessionService sess;

    protected BaseProtectedView(String activeRoute) {
        HttpClient http = HttpClients.createDefault();
        this.rest = new RestTemplate(new HttpComponentsClientHttpRequestFactory(http));
        this.sess = SessionService.get();

        // header
        String uname = sess.getUsername().orElse("User");
        H1 hello = new H1("Hello " + uname);
        hello.getStyle().set("font-size", "var(--lumo-font-size-l)")
                .set("margin", "var(--lumo-space-m)");
        Button lo = new Button("Log out", e -> { sess.clear(); UI.getCurrent().navigate("index"); });
        lo.getStyle().set("margin", "var(--lumo-space-m)");
        HorizontalLayout head = new HorizontalLayout(new DrawerToggle(), hello, lo);
        head.setWidthFull();
        head.setAlignItems(HorizontalLayout.Alignment.CENTER);
        head.setJustifyContentMode(HorizontalLayout.JustifyContentMode.BETWEEN);
        addToNavbar(head);

        // sidenav
        SideNav nav = new SideNav();
        nav.addItem(new SideNavItem("Dashboard", "index"));
        nav.addItem(new SideNavItem("Cars",      "cars"));
        nav.addItem(new SideNavItem("Users",     "users"));
        nav.addItem(new SideNavItem("Activities","activities"));
        addToDrawer(nav);
    }

    protected void setMainContent(com.vaadin.flow.component.Component... comps) {
        var col = new com.vaadin.flow.component.orderedlayout.VerticalLayout(comps);
        col.setSizeFull(); col.setPadding(true); col.setSpacing(true);
        setContent(col);
    }

    protected String jwt() { return sess.getJwt().orElse(null); }

    @Override
    public void beforeEnter(BeforeEnterEvent e) {
        if (sess.getJwt().isEmpty()) {
            e.forwardTo("app/index");
        }
    }
}