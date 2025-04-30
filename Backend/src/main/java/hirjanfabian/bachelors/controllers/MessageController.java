package hirjanfabian.bachelors.controllers;

import hirjanfabian.bachelors.dto.ChatMessageDTO;
import hirjanfabian.bachelors.entities.Message;
import hirjanfabian.bachelors.entities.User;
import hirjanfabian.bachelors.repositories.MessageRepository;
import hirjanfabian.bachelors.repositories.UserRepository;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;

    public MessageController(MessageRepository messageRepository, UserRepository userRepository) {
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
    }


    @GetMapping("/conversations")
    public List<User> getConversations(@RequestParam("userId") Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found: " + userId));

        List<User> senders   = messageRepository.findAllSendersForUser(userId);
        List<User> receivers = messageRepository.findAllReceiversForUser(userId);

        Set<User> partnersSet = new HashSet<>();
        partnersSet.addAll(senders);
        partnersSet.addAll(receivers);

        return new ArrayList<>(partnersSet);
    }


    @GetMapping("/history")
    public List<ChatMessageDTO> getHistory(
            @RequestParam("user1") Long user1,
            @RequestParam("user2") Long user2
    ) {
        userRepository.findById(user1)
                .orElseThrow(() -> new RuntimeException("User not found: " + user1));
        userRepository.findById(user2)
                .orElseThrow(() -> new RuntimeException("User not found: " + user2));

        List<Message> allMsgs = messageRepository.findConversation(user1, user2);

        return allMsgs.stream()
                .map(m -> new ChatMessageDTO(
                        m.getSender().getId(),
                        m.getReceiver().getId(),
                        m.getMessage(),
                        m.getSentDate()
                ))
                .toList();
    }

}
