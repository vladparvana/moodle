package pos.proiect.AcademiaAPI.ids;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.util.Objects;

@Getter
@Setter
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class StudentiDisciplineId implements java.io.Serializable {
    private static final long serialVersionUID = 277219682139262365L;
    @NotNull
    @Column(name = "ID_student", nullable = false)
    private Long idStudent;

    @Size(max = 255)
    @NotNull
    @Column(name = "COD_disciplina", nullable = false)
    private String codDisciplina;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        StudentiDisciplineId entity = (StudentiDisciplineId) o;
        return Objects.equals(this.codDisciplina, entity.codDisciplina) &&
                Objects.equals(this.idStudent, entity.idStudent);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codDisciplina, idStudent);
    }

}