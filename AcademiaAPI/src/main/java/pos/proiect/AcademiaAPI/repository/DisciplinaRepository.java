package pos.proiect.AcademiaAPI.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import pos.proiect.AcademiaAPI.entity.Disciplina;

import java.util.List;
import java.util.Optional;

public interface DisciplinaRepository extends JpaRepository<Disciplina, String> {
    Optional<Disciplina> findByCod(String cod);
    Page<Disciplina> findAll(Specification<Disciplina> specification, Pageable pageable);
    Disciplina save(Disciplina disciplina);
    boolean existsByCod(String cod);
    void deleteByCod(String cod);
}
