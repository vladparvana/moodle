package pos.proiect.AcademiaAPI.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import pos.proiect.AcademiaAPI.entity.CadruDidactic;
import pos.proiect.AcademiaAPI.enums.GradDidactic;

import java.awt.print.Pageable;
import java.util.List;
import java.util.Optional;

public interface CadruDidacticRepository extends JpaRepository<CadruDidactic, Long>, JpaSpecificationExecutor<CadruDidactic> {
    Optional<CadruDidactic> findById(Long id);
    List<CadruDidactic> findAll();
    CadruDidactic save(CadruDidactic cadruDidactic);
    boolean existsById(Long id);
    void deleteById(Long id);
    Optional<CadruDidactic> findByEmail(String email);

}
