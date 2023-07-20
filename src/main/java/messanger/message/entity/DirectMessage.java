package messanger.message.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import messanger.user.entity.User;
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

    @Size(max = 500)
    private String message;

    private Instant createdAt = Instant.now();
}
