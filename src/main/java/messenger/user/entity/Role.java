package messenger.user.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import messenger.common.component.logging.LoggerEntityListener;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EntityListeners(LoggerEntityListener.class)
public class Role implements Serializable {
    @Serial
    private static final long serialVersionUID = 8997690331725029733L;

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    @Size(min=3, max=32)
    @Column(nullable = false, unique = true)
    private String name;

    @ManyToMany(mappedBy = "roles")
    private List<User> userEntities;
}
