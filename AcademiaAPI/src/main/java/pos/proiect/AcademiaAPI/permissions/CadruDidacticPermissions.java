package pos.proiect.AcademiaAPI.permissions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import pos.proiect.AcademiaAPI.dto.CadruDidacticDTO;
import pos.proiect.AcademiaAPI.exceptions.CadruDidacticNotFound;
import pos.proiect.AcademiaAPI.service.CadruDidacticService;
import pos.proiect.AcademiaAPI.service.DisciplinaService;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class CadruDidacticPermissions {

    @Autowired
    private CadruDidacticService cadruDidacticService;

    @Autowired
    private DisciplinaService disciplinaService;

    public Map<String, Set<String>> getAllowedLinksAndOperations(String email) {
        Optional<CadruDidacticDTO> cadruDidacticDTOOptional = cadruDidacticService.findByEmail(email);
        if (!cadruDidacticDTOOptional.isPresent()) {
            return null;
        }
        CadruDidacticDTO cadruDidacticDTO = cadruDidacticDTOOptional.get();

        Map<String, Set<String>> permissions = new HashMap<>();

        Set<String> lectureLinks = cadruDidacticDTO.getDiscipline().stream()
                .map(disciplina -> "/api/academia/lectures/" + disciplina.getCod())
                .collect(Collectors.toSet());
        permissions.put("lectures", Set.of("GET", "PUT","PATCH","POST", "DELETE"));
        permissions.put("lectureLinks", lectureLinks);

        Set<String> allLectureLinks = disciplinaService.getAllLectures(null,null, PageRequest.of(0,10)).stream()
                .map(disciplina -> "/api/academia/lectures/" + disciplina.getCod())
                .collect(Collectors.toSet());
        allLectureLinks.forEach(link -> permissions.put(link, Set.of("GET")));

        permissions.put("/api/academia/lectures", Set.of("GET","POST"));


        Set<String> studentLinks = cadruDidacticDTO.getDiscipline().stream()
                .flatMap(disciplina -> disciplina.getStudents().stream())
                .map(student -> "/api/academia/students/" + student.getId())
                .collect(Collectors.toSet());
        studentLinks.forEach(link -> permissions.put(link, Set.of("GET")));


        Set<String> addStudentLinks = cadruDidacticDTO.getDiscipline().stream()
                .map(disciplina -> "/api/academia/lectures/" + disciplina.getCod() + "/add-student")
                .collect(Collectors.toSet());

        addStudentLinks.forEach(link -> permissions.put(link, Set.of("POST")));


        return permissions;
    }
}