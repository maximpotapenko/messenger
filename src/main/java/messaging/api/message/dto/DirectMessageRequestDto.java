package messaging.api.message.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DirectMessageRequestDto {
    private Long authorId;

    private Long recipientId;

    @Size(max = 10)
    private String message;

}
