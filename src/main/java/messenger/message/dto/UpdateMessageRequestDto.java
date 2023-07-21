package messenger.message.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import messenger.message.constraint.ValidMessage;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateMessageRequestDto {

    @ValidMessage
    private String message;
}
