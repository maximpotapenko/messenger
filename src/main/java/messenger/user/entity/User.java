package messenger.user.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "users")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User implements Serializable {
    @Serial
    private static final long serialVersionUID = 4141717765406401594L;

    @Id
    @GeneratedValue
    private long id;

    @NotNull
    @Size(min = 4, max = 32)
    @Column(nullable = false, unique = true)
    private String username;

    @NotNull
    @Column(nullable = false)
    private String password;

    @NotNull
    @Size(min = 2, max = 32)
    private String firstName;

    @NotNull
    @Size(min = 2, max = 32)
    private String lastName;

    private LocalDate birthday;

    @NotNull
    @Email
    @Column(nullable = false)
    private String email;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Role> roles;

    private Instant createdAt = Instant.now();

    private boolean deleted = false;

    private boolean banned = false;

}
