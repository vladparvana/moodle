package pos.proiect.AcademiaAPI.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import pos.proiect.AcademiaAPI.dto.StudentDTO;
import pos.proiect.AcademiaAPI.enums.CicluStudii;
import pos.proiect.AcademiaAPI.listener.StudentListener;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@EntityListeners(StudentListener.class)
@Entity
@Table(name = "studenti")
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Long id;

    @Size(max = 255)
    @NotNull
    @Column(name = "nume", nullable = false)
    private String nume;

    @Size(max = 255)
    @NotNull
    @Column(name = "prenume", nullable = false)
    private String prenume;

    @Size(max = 255)
    @Column(name = "email")
    @Email
    private String email;

    @NotNull
    @Column(name = "ciclu_studii", nullable = false)
    @Enumerated(EnumType.STRING)
    private CicluStudii cicluStudii;

    @NotNull
    @Column(name = "an_studiu", nullable = false)
    private Integer anStudiu;

    @NotNull
    @Column(name = "grupa", nullable = false)
    private Integer grupa;

    @ManyToMany
    @JoinTable(name = "studenti_discipline",
            joinColumns = @JoinColumn(name = "ID_student"),
            inverseJoinColumns = @JoinColumn(name = "COD_disciplina"))
    private Set<Disciplina> discipline = new LinkedHashSet<>();



}