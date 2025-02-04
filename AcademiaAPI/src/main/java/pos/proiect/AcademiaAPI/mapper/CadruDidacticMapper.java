package pos.proiect.AcademiaAPI.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import pos.proiect.AcademiaAPI.dto.CadruDidacticDTO;
import pos.proiect.AcademiaAPI.dto.DisciplinaDTO;
import pos.proiect.AcademiaAPI.dto.StudentDTO;
import pos.proiect.AcademiaAPI.entity.CadruDidactic;
import pos.proiect.AcademiaAPI.entity.Disciplina;
import pos.proiect.AcademiaAPI.entity.Student;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper
public interface CadruDidacticMapper {
    CadruDidacticMapper INSTANCE = Mappers.getMapper(CadruDidacticMapper.class);

    DisciplinaMapper disciplinaMapper = Mappers.getMapper(DisciplinaMapper.class);

    @Mapping(target = "discipline", expression = "java(mapDiscipline(profesor.getDiscipline()))")
    CadruDidacticDTO toDTO(CadruDidactic profesor);

    default Set<DisciplinaDTO> mapDiscipline(Set<Disciplina> discipline) {
        return discipline.stream()
                .map(disciplinaMapper::toDTO)
                .collect(Collectors.toSet());
    }

    @Mapping(target = "discipline", expression = "java(mapDisciplineToEntity(profesor.getDiscipline()))")
    CadruDidactic toEntity(CadruDidacticDTO profesor);

    default Set<Disciplina> mapDisciplineToEntity(Set<DisciplinaDTO> discipline) {
        return discipline.stream()
                .map(disciplinaMapper::toEntity)
                .collect(Collectors.toSet());
    }
}