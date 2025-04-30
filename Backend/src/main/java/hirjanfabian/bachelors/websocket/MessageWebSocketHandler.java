package hirjanfabian.bachelors.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import hirjanfabian.bachelors.entities.Message;
import hirjanfabian.bachelors.entities.User;
import hirjanfabian.bachelors.repositories.MessageRepository;
import hirjanfabian.bachelors.repositories.UserRepository;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.web.socket.CloseStatus;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MessageWebSocketHandler extends TextWebSocketHandler {

    private static final Map<Long, WebSocketSession> sessions = new ConcurrentHashMap<>();
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public MessageWebSocketHandler(
            MessageRepository messageRepository,
            UserRepository userRepository
    ) {
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        Long userId = getUserIdFromSession(session);
        if (userId != null) {
            sessions.put(userId, session);
            System.out.println("User cu ID-ul " + userId + " s-a conectat la WebSocket (chat).");

            // -------------------------------------------
            // If you want to load "pending messages" from DB
            // ONLY for offline messages, you can do that here.
            // But if your SwiftUI client calls fetchConversation(...),
            // there's no need to push messages again. That can cause duplicates.
            //
            // Example (commented out):
            //
            // List<Message> pendingMessages = messageRepository.findByReceiverId(userId);
            // for (Message message : pendingMessages) {
            //     ChatMessage chatMessage = new ChatMessage();
            //     chatMessage.setSenderId(message.getSender().getId());
            //     chatMessage.setReceiverId(message.getReceiver().getId());
            //     chatMessage.setMessage(message.getMessage());
            //
            //     session.sendMessage(new TextMessage(
            //         objectMapper.writeValueAsString(chatMessage)
            //     ));
            // }
            // -------------------------------------------
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage textMessage) throws Exception {
        ChatMessage chatMessage = objectMapper.readValue(textMessage.getPayload(), ChatMessage.class);

        // Validate sender/receiver
        User sender   = userRepository.findById(chatMessage.getSenderId()).orElse(null);
        User receiver = userRepository.findById(chatMessage.getReceiverId()).orElse(null);

        if (sender == null || receiver == null) {
            return; // or throw an exception if you prefer
        }

        // Save the message to the DB
        Message message = new Message();
        message.setMessage(chatMessage.getMessage());
        message.setSender(sender);
        message.setReceiver(receiver);
        message.setSentDate(new java.util.Date());
        messageRepository.save(message);

        // Convert ChatMessage -> JSON
        String outgoingJson = objectMapper.writeValueAsString(chatMessage);

        // Send to the receiver if online
        WebSocketSession receiverSession = sessions.get(chatMessage.getReceiverId());
        if (receiverSession != null && receiverSession.isOpen()) {
            receiverSession.sendMessage(new TextMessage(outgoingJson));
        }

        // **Echo** to the sender as well, so they see their own message instantly
        WebSocketSession senderSession = sessions.get(chatMessage.getSenderId());
        if (senderSession != null && senderSession.isOpen()) {
            senderSession.sendMessage(new TextMessage(outgoingJson));
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        Long userId = getUserIdFromSession(session);
        if (userId != null) {
            sessions.remove(userId);
            System.out.println("User cu ID-ul " + userId + " s-a deconectat de la WebSocket (chat).");
        }
    }

    // Extract userId from ?userId=123 in the WebSocket URL
    private Long getUserIdFromSession(WebSocketSession session) {
        if (session.getUri() != null && session.getUri().getQuery() != null) {
            String query = session.getUri().getQuery();
            if (query.startsWith("userId=")) {
                return Long.valueOf(query.substring("userId=".length()));
            }
        }
        return null;
    }

    /**
     * Inner class for data transfer.
     * Must match the SwiftUI ChatMessage shape (senderId, receiverId, message).
     */
    private static class ChatMessage {
        private Long senderId;
        private Long receiverId;
        private String message;

        public Long getSenderId() { return senderId; }
        public void setSenderId(Long senderId) { this.senderId = senderId; }
        public Long getReceiverId() { return receiverId; }
        public void setReceiverId(Long receiverId) { this.receiverId = receiverId; }
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
    }
}
