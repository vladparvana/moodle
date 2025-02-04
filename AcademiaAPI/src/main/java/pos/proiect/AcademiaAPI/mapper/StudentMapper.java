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
public interface StudentMapper {
    StudentMapper INSTANCE = Mappers.getMapper(StudentMapper.class);

    @Mapping(target = "discipline", expression = "java(mapDisciplineToSimpleDTO(student.getDiscipline()))")
    StudentDTO toDTO(Student student);

    StudentSimpleDTO toSimpleDTO(Student student);

    default Set<DisciplinaSimpleDTO> mapDisciplineToSimpleDTO(Set<Disciplina> discipline) {
        if (discipline == null) return new HashSet<>();
        return discipline.stream()
                .map(this::disciplinaToSimpleDTO)
                .collect(Collectors.toSet());
    }

    @Mapping(target = "idTitular", source = "idTitular", qualifiedByName = "mapCadruDidacticToId")
    DisciplinaSimpleDTO disciplinaToSimpleDTO(Disciplina disciplina);

    @Mapping(target = "discipline", ignore = true)
    Student toEntity(StudentDTO studentDTO);

    @Named("mapCadruDidacticToId")
    default Long mapCadruDidacticToId(CadruDidactic cadruDidactic) {
        return cadruDidactic != null ? cadruDidactic.getId() : null;
    }
}