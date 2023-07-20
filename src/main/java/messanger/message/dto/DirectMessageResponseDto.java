package messanger.message.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DirectMessageResponseDto {
    private Long id;

    private Long authorId;

    private Long recipientId;

    private String authorName;

    private String message;

    private Instant createdAt;
}
