package messanger.message.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DirectMessageListResponseDto {
    List<DirectMessageResponseDto> list;
}
