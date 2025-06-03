package hirjanfabian.bachelors.ui.security;

import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.button.Button;

public class LoginDialog extends Dialog {
    public LoginDialog(SessionService sess, Runnable onSuccess) {
        setHeaderTitle("Login");
        TextField user = new TextField("Username");
        PasswordField pass = new PasswordField("Password");
        Button ok = new Button("Login", e -> {
            if (sess.login(user.getValue(), pass.getValue())) { close(); onSuccess.run(); }
            else Notification.show("Invalid credentials");
        });
        add(new VerticalLayout(new H2("Please sign in"), user, pass, ok));
        setCloseOnEsc(false); setCloseOnOutsideClick(false);
    }
}