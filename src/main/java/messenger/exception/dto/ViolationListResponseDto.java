package messenger.exception.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ViolationListResponseDto {

    private final Instant timestamp = Instant.now();

    private int status;

    private String message;

    private List<ViolationResponseDto> violations;
}
