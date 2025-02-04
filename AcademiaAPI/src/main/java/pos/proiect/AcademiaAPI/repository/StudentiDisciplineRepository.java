package pos.proiect.AcademiaAPI.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pos.proiect.AcademiaAPI.entity.StudentiDiscipline;
import pos.proiect.AcademiaAPI.ids.StudentiDisciplineId;

public interface StudentiDisciplineRepository extends JpaRepository<StudentiDiscipline, StudentiDisciplineId> {
    boolean existsById(StudentiDisciplineId id);
}
