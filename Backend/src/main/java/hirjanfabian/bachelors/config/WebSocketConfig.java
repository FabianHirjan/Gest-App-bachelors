package hirjanfabian.bachelors.config;

import hirjanfabian.bachelors.websocket.UserTrackingWebSocketHandler;
import hirjanfabian.bachelors.websocket.MessageWebSocketHandler;
import hirjanfabian.bachelors.repositories.MessageRepository;
import hirjanfabian.bachelors.repositories.UserRepository;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.beans.factory.annotation.Autowired;

@Configuration
@EnableWebSocket
@NoArgsConstructor
public class WebSocketConfig implements WebSocketConfigurer {
    private MessageRepository messageRepository;

    private UserRepository userRepository;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new UserTrackingWebSocketHandler(), "/ws/user-tracking")
                .setAllowedOrigins("*");

        registry.addHandler(
                new MessageWebSocketHandler(messageRepository, userRepository),
                "/ws/messages"
        ).setAllowedOrigins("*");
    }
}
