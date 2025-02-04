package pos.proiect.AcademiaAPI.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pos.proiect.AcademiaAPI.entity.Student;

import java.util.List;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {
    List<Student> findAll();
    Optional<Student> findById(Long id);
    Student save(Student student);
    boolean existsById(Long id);
    void deleteById(Long id);
    Optional<Student> findByEmail(String email);
}
