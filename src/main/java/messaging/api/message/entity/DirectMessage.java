package messaging.api.message.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import messaging.api.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DirectMessage {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn
    private User author;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn
    private User recipient;

    @Size(max = 500)
    private String message;

    @CreationTimestamp
    private Instant createdAt;

}
