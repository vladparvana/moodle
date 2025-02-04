package pos.proiect.AcademiaAPI.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import pos.proiect.AcademiaAPI.dto.DisciplinaDTO;
import pos.proiect.AcademiaAPI.dto.DisciplinaSimpleDTO;
import pos.proiect.AcademiaAPI.dto.StudentDTO;
import pos.proiect.AcademiaAPI.dto.StudentSimpleDTO;
import pos.proiect.AcademiaAPI.entity.CadruDidactic;
import pos.proiect.AcademiaAPI.entity.Disciplina;
import pos.proiect.AcademiaAPI.entity.Student;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface DisciplinaMapper {
    DisciplinaMapper INSTANCE = Mappers.getMapper(DisciplinaMapper.class);

    @Mapping(target = "idTitular", source = "idTitular", qualifiedByName = "mapCadruDidacticToId")
    @Mapping(target = "students", expression = "java(mapStudentsToSimpleDTO(disciplina.getStudents()))")
    DisciplinaDTO toDTO(Disciplina disciplina);

    @Mapping(target = "idTitular", source = "idTitular", qualifiedByName = "mapCadruDidacticToId")
    DisciplinaSimpleDTO toSimpleDTO(Disciplina disciplina);

    default Set<StudentSimpleDTO> mapStudentsToSimpleDTO(Set<Student> students) {
        if (students == null) return new HashSet<>();
        return students.stream()
                .map(this::studentToSimpleDTO)
                .collect(Collectors.toSet());
    }

    StudentSimpleDTO studentToSimpleDTO(Student student);

    @Mapping(target = "students", ignore = true)
    @Mapping(target = "idTitular", source = "idTitular", qualifiedByName = "mapIdToCadruDidactic")
    Disciplina toEntity(DisciplinaDTO disciplinaDTO);

    @Named("mapCadruDidacticToId")
    default Long mapCadruDidacticToId(CadruDidactic cadruDidactic) {
        return cadruDidactic != null ? cadruDidactic.getId() : null;
    }

    @Named("mapIdToCadruDidactic")
    default CadruDidactic mapIdToCadruDidactic(Long id) {
        if (id == null) {
            return null;
        }
        CadruDidactic cadruDidactic = new CadruDidactic();
        cadruDidactic.setId(id);
        return cadruDidactic;
    }
}
