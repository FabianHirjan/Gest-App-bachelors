package hirjanfabian.bachelors.repositories;

import hirjanfabian.bachelors.entities.Message;
import hirjanfabian.bachelors.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {

    // Where user is the receiver, distinct "senders"
    @Query("""
        SELECT DISTINCT m.sender
        FROM Message m
        WHERE m.receiver.id = :userId
    """)
    List<User> findAllSendersForUser(@Param("userId") Long userId);

    // Where user is the sender, distinct "receivers"
    @Query("""
        SELECT DISTINCT m.receiver
        FROM Message m
        WHERE m.sender.id = :userId
    """)
    List<User> findAllReceiversForUser(@Param("userId") Long userId);

    // Full conversation between two users
    @Query("""
        SELECT m
        FROM Message m
        WHERE (m.sender.id = :user1 AND m.receiver.id = :user2)
           OR (m.sender.id = :user2 AND m.receiver.id = :user1)
        ORDER BY m.sentDate ASC
    """)
    List<Message> findConversation(@Param("user1") Long user1, @Param("user2") Long user2);

    // If you want to fetch all messages for a user as "pending"
    List<Message> findByReceiverId(Long receiverId);
}
