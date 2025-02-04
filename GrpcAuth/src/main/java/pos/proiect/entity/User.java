package pos.proiect.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.checkerframework.common.aliasing.qual.Unique;
import pos.proiect.enums.Rol;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "utilizatori")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    @Column(nullable = false)
    private String password;

    @Unique
    @Column(unique = true, nullable = false)
    @Email
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Rol rol;

    public User(String email, String password, Rol rol) {
        this.email = email;
        this.password = password;
        this.rol = rol;
    }
}
