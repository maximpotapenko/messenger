package messenger.message.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DirectMessageResponseDto {

    private Long id;

    private Long authorId;

    private Long recipientId;

    private String createdAt;

    private String message;
}
