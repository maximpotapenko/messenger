package messenger.message.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import messenger.message.constraint.ValidMessage;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DirectMessageRequestDto {

    @NotNull(message = "recipientId can't be null")
    private Long recipientId;

    @ValidMessage
    private String message;
}
