package pos.proiect.AcademiaAPI.permissions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import pos.proiect.AcademiaAPI.dto.StudentDTO;
import pos.proiect.AcademiaAPI.entity.Student;
import pos.proiect.AcademiaAPI.exceptions.StudentNotFoundException;
import pos.proiect.AcademiaAPI.mapper.StudentMapper;
import pos.proiect.AcademiaAPI.repository.StudentRepository;
import pos.proiect.AcademiaAPI.service.StudentService;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class StudentPermissions {

    @Autowired
    private StudentService studentService;


    public Map<String, Set<String>> getAllowedLinksAndOperations(String email) {

        Optional<StudentDTO> studentDTOOptional = studentService.getStudentByEmail(email);

        if(!studentDTOOptional.isPresent()) {
            return null;
        }

        StudentDTO studentDTO = studentDTOOptional.get();

        Map<String, Set<String>> permissions = new HashMap<>();

        String studentLink = "/api/academia/students/" + studentDTO.getId();
        permissions.put(studentLink, Set.of("GET", "PUT","PATCH"));

        Set<String> lectureLinks = studentDTO.getDiscipline().stream()
                .map(disciplina -> "/api/academia/lectures/" + disciplina.getCod())
                .collect(Collectors.toSet());


        lectureLinks.forEach(link -> permissions.put(link, Set.of("GET")));
        return permissions;
    }

}