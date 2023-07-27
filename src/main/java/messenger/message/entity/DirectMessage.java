package messenger.message.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import messenger.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
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

    @Builder.Default
    private Instant createdAt = Instant.now();

    @Size(max = 500)
    private String message;
}
