package messenger.exception.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ViolationResponseDto {

    private String fieldName;

    private String rejectedValue;

    private String message;
}
