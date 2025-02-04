package pos.proiect.AcademiaAPI.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pos.proiect.AcademiaAPI.ids.StudentiDisciplineId;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "studenti_discipline")
public class StudentiDiscipline {
    @EmbeddedId
    private StudentiDisciplineId id;

    @MapsId("idStudent")
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "ID_student", nullable = false)
    private Student idStudent;

    @MapsId("codDisciplina")
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "COD_disciplina", nullable = false)
    private Disciplina codDisciplina;

    public StudentiDiscipline(StudentiDisciplineId id)
    {
        this.id = id;
    }

}