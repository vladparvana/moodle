package pos.proiect.AcademiaAPI.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import pos.proiect.AcademiaAPI.enums.CategorieDisciplina;
import pos.proiect.AcademiaAPI.enums.TipDisciplina;
import pos.proiect.AcademiaAPI.enums.TipExaminare;
import pos.proiect.AcademiaAPI.listener.DisciplinaListener;
import pos.proiect.AcademiaAPI.listener.StudentListener;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@EntityListeners(DisciplinaListener.class)
@Entity
@Table(name = "discipline")
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Disciplina {
    @Id
    @Size(max = 255)
    @Column(name = "COD", nullable = false)
    private String cod;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_titular")
    private CadruDidactic idTitular;

    @Size(max = 255)
    @NotNull
    @Column(name = "nume_disciplina", nullable = false)
    private String numeDisciplina;

    @NotNull
    @Column(name = "an_studiu", nullable = false)
    private Integer anStudiu;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "tip_disciplina", nullable = false)
    private TipDisciplina tipDisciplina;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "categorie_disciplina", nullable = false)
    private CategorieDisciplina categorieDisciplina;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "tip_examinare", nullable = false)
    private TipExaminare tipExaminare;

    @ManyToMany(mappedBy = "discipline")
    private Set<Student> students = new LinkedHashSet<>();

}