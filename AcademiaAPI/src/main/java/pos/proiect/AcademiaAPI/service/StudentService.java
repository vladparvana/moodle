package pos.proiect.AcademiaAPI.service;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.ReflectionUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import pos.proiect.AcademiaAPI.dto.StudentDTO;
import pos.proiect.AcademiaAPI.entity.Student;
import pos.proiect.AcademiaAPI.enums.CicluStudii;
import pos.proiect.AcademiaAPI.exceptions.StudentNotFoundException;
import pos.proiect.AcademiaAPI.mapper.StudentMapper;
import pos.proiect.AcademiaAPI.repository.StudentRepository;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Validated
public class StudentService {
    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private Validator validator;

    public List<StudentDTO> getAllStudents()
    {
       return studentRepository.findAll().stream()
               .map(StudentMapper.INSTANCE::toDTO)
               .collect(Collectors.toList());
    }

    public Optional<StudentDTO> getStudentById(Long id) {
        //return studentRepository.findById(id).map(StudentDTO::new);
        return studentRepository.findById(id)
                .map(StudentMapper.INSTANCE::toDTO);

    }

    public StudentDTO createStudent(StudentDTO studentDTO) {
        Student student = StudentMapper.INSTANCE.toEntity(studentDTO);
        return StudentMapper.INSTANCE.toDTO(studentRepository.save(student));
        //return new StudentDTO(studentRepository.save(student));
    }

    @Transactional
    public Optional<StudentDTO> getStudentByEmail(String email) {
        return studentRepository.findByEmail(email).map(StudentMapper.INSTANCE::toDTO);
    }

    public Optional<StudentDTO> updateStudent(Long id, StudentDTO studentDTO) {
        Optional<Student> studentOptional = studentRepository.findById(id);
        if (studentOptional.isPresent()) {
            studentDTO.setId(id);
            //validateStudent(studentDTO);
            return Optional.of(StudentMapper.INSTANCE.toDTO(studentRepository.save(StudentMapper.INSTANCE.toEntity(studentDTO))));
        }
        throw new StudentNotFoundException(id);
    }

    public Optional<StudentDTO> patchStudent(Long id, Map<String, Object> fields) throws JsonMappingException {
        Optional<Student> studentOptional = studentRepository.findById(id);
        if (studentOptional.isPresent()) {
            Student student = studentOptional.get();
            StudentDTO studentDTO = StudentMapper.INSTANCE.toDTO(student);
            objectMapper.updateValue(studentDTO, fields);
            validateStudent(studentDTO);
            studentRepository.save(StudentMapper.INSTANCE.toEntity(studentDTO));
            return Optional.of(StudentMapper.INSTANCE.toDTO(student));
        }
        throw new StudentNotFoundException(id);
    }

    public Optional<StudentDTO> deleteStudent(Long id) {
        Optional<StudentDTO> studentOptional = studentRepository.findById(id).map(StudentMapper.INSTANCE::toDTO);
        studentRepository.deleteById(id);
        return studentOptional;
    }

    public Boolean existsStudent(Long id) {
        return studentRepository.existsById(id);
    }

    private void validateStudent(@Valid StudentDTO studentDTO) {
        Set<ConstraintViolation<StudentDTO>> violations = validator.validate(studentDTO);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }

}
