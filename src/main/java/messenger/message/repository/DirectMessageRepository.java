package messenger.message.repository;

import messenger.message.entity.DirectMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DirectMessageRepository extends JpaRepository<DirectMessage, Long> {
    @Query(value = "SELECT * FROM direct_message" +
            " WHERE author_id = :authorId AND recipient_id = :recipientId" +
            " OR author_id = :recipientId AND recipient_id = :authorId" +
            " ORDER BY created_at DESC  OFFSET :offset LIMIT :limit",nativeQuery = true)
    List<DirectMessage> findAllByAuthorIdOrRecipientId(Long authorId, Long recipientId, int offset, int limit);
}
