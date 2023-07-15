package messaging.api.message.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FetchMessagesRequestDto {
    private Long recipientId;

    private Integer offset;

    private Integer limit;
}
