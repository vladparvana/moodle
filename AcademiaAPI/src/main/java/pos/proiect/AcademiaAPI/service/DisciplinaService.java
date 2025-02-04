package pos.proiect.AcademiaAPI.service;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pos.proiect.AcademiaAPI.dto.CadruDidacticDTO;
import pos.proiect.AcademiaAPI.dto.DisciplinaDTO;
import pos.proiect.AcademiaAPI.dto.StudentDTO;
import pos.proiect.AcademiaAPI.entity.CadruDidactic;
import pos.proiect.AcademiaAPI.entity.Disciplina;
import pos.proiect.AcademiaAPI.entity.Student;
import pos.proiect.AcademiaAPI.entity.StudentiDiscipline;
import pos.proiect.AcademiaAPI.enums.CategorieDisciplina;
import pos.proiect.AcademiaAPI.enums.TipDisciplina;
import pos.proiect.AcademiaAPI.exceptions.CadruDidacticNotFound;
import pos.proiect.AcademiaAPI.exceptions.DisciplinaNotFoundException;
import pos.proiect.AcademiaAPI.exceptions.StudentAlreadyEnrolledException;
import pos.proiect.AcademiaAPI.exceptions.StudentNotFoundException;
import pos.proiect.AcademiaAPI.ids.StudentiDisciplineId;
import pos.proiect.AcademiaAPI.mapper.CadruDidacticMapper;
import pos.proiect.AcademiaAPI.mapper.DisciplinaMapper;
import pos.proiect.AcademiaAPI.repository.CadruDidacticRepository;
import pos.proiect.AcademiaAPI.repository.DisciplinaRepository;
import pos.proiect.AcademiaAPI.repository.StudentRepository;
import pos.proiect.AcademiaAPI.repository.StudentiDisciplineRepository;
import pos.proiect.AcademiaAPI.specification.DisciplinaSpecification;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class DisciplinaService {
    @Autowired
    private DisciplinaRepository disciplinaRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private StudentiDisciplineRepository studentiDisciplineRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private Validator validator;

    @Transactional
    public Page<DisciplinaDTO> getAllLectures(String tipDisciplina, String categorieDisciplina, Pageable pageable)
    {
        Specification<Disciplina> specification = Specification.where(null);

        if(tipDisciplina!=null)
        {
            specification = specification.and(DisciplinaSpecification.hasTipDisciplina(TipDisciplina.valueOf(tipDisciplina)));
        }

        if(categorieDisciplina!=null)
        {
            specification = specification.and(DisciplinaSpecification.hasCategorieDisciplina(CategorieDisciplina.valueOf(categorieDisciplina)));
        }

        Page<Disciplina> lectures = disciplinaRepository.findAll(specification,pageable);

        return lectures.map(DisciplinaMapper.INSTANCE::toDTO);

    }

    public Optional<DisciplinaDTO> getLectureByCod(String cod) {
        return disciplinaRepository.findByCod(cod)
                .map(DisciplinaMapper.INSTANCE::toDTO);

    }

    public DisciplinaDTO createLecture(DisciplinaDTO disciplinaDTO) {
        Disciplina disciplina = DisciplinaMapper.INSTANCE.toEntity(disciplinaDTO);
        return DisciplinaMapper.INSTANCE.toDTO(disciplinaRepository.save(disciplina));
    }

    public Optional<DisciplinaDTO> updateLecture(String cod, DisciplinaDTO disciplinaDTO) {
        Optional<Disciplina> disciplinaOptional = disciplinaRepository.findByCod(cod);
        if(disciplinaOptional.isPresent()) {
            disciplinaDTO.setCod(cod);
            return Optional.of(DisciplinaMapper.INSTANCE.toDTO(disciplinaRepository.save(DisciplinaMapper.INSTANCE.toEntity(disciplinaDTO))));
        }
        throw new DisciplinaNotFoundException(cod);
    }

    public Optional<DisciplinaDTO> patchLecture(String cod, Map<String, Object> fields) throws JsonMappingException {
        Optional<Disciplina> disciplinaOptional = disciplinaRepository.findByCod(cod);
        if(disciplinaOptional.isPresent()) {
            Disciplina disciplina = disciplinaOptional.get();
            DisciplinaDTO disciplinaDTO = DisciplinaMapper.INSTANCE.toDTO(disciplina);
            objectMapper.updateValue(disciplina, fields);
            validateLecture(disciplinaDTO);
            disciplinaRepository.save(DisciplinaMapper.INSTANCE.toEntity(disciplinaDTO));
            return Optional.of(DisciplinaMapper.INSTANCE.toDTO(disciplina));
        }
        throw new DisciplinaNotFoundException(cod);
    }

    public Optional<DisciplinaDTO> deleteLecture(String cod) {
        Optional<DisciplinaDTO> disciplinaOptional = disciplinaRepository.findByCod(cod).map(DisciplinaMapper.INSTANCE::toDTO);
        disciplinaRepository.deleteByCod(cod);
        return disciplinaOptional;
    }

    public DisciplinaDTO addStudent(String cod, Long idStudent)
    {
        Optional<Student> student = studentRepository.findById(idStudent);
        if(!student.isPresent()) {
            throw new StudentNotFoundException(idStudent);
        }

        Optional<Disciplina> disciplina = disciplinaRepository.findByCod(cod);
        if(!disciplina.isPresent()) {
            throw new DisciplinaNotFoundException(cod);
        }

        StudentiDisciplineId studentiDisciplineId = new StudentiDisciplineId(idStudent, cod);
        if (studentiDisciplineRepository.existsById(studentiDisciplineId)) {
            throw new StudentAlreadyEnrolledException(cod, idStudent);
        }
        StudentiDiscipline studentiDiscipline = new StudentiDiscipline();
        studentiDiscipline.setId(studentiDisciplineId);
        studentiDiscipline.setIdStudent(student.get());
        studentiDiscipline.setCodDisciplina(disciplina.get());

        studentiDisciplineRepository.save(studentiDiscipline);

        return DisciplinaMapper.INSTANCE.toDTO(disciplinaRepository.findByCod(cod).get());
    }

    public Boolean existsLecture(String cod) {
        return disciplinaRepository.existsByCod(cod);
    }



    private void validateLecture(@Valid DisciplinaDTO disciplinaDTO) {
        Set<ConstraintViolation<DisciplinaDTO>> violations = validator.validate(disciplinaDTO);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }


}
