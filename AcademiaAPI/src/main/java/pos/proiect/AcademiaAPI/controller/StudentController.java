package pos.proiect.AcademiaAPI.controller;

import com.fasterxml.jackson.databind.JsonMappingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pos.proiect.AcademiaAPI.assembler.StudentAssembler;
import pos.proiect.AcademiaAPI.dto.StudentDTO;
import pos.proiect.AcademiaAPI.exceptions.StudentNotFoundException;
import pos.proiect.AcademiaAPI.service.StudentService;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/academia/students")
public class StudentController {
    @Autowired
    private StudentService studentService;

    @Autowired
    private StudentAssembler studentAssembler;

    @GetMapping()
    public ResponseEntity<CollectionModel<EntityModel<StudentDTO>>> getAllStudents() {
        List<EntityModel<StudentDTO>> students = studentService.getAllStudents().stream()
                .map(studentAssembler::toModel)
                .collect(Collectors.toList());

        CollectionModel<EntityModel<StudentDTO>> collectionModel = CollectionModel.of(students,linkTo(methodOn(StudentController.class).getAllStudents()).withSelfRel());
        return new ResponseEntity<>(collectionModel, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getStudentById(@PathVariable Long id) {
        ResponseEntity<?> response= studentService.getStudentById(id)
                .map(studentDTO -> studentAssembler.toModel(studentDTO))
                .map(studentDTO -> ResponseEntity.ok(studentDTO))
                .orElseThrow(() -> new StudentNotFoundException(id));
        return response;
    }

    @PostMapping()
    public ResponseEntity<?> createStudent(@Valid @RequestBody StudentDTO studentDTO) {

        StudentDTO createdStudent = studentService.createStudent(studentDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(studentAssembler.toModel(createdStudent));
    }


    @PutMapping("/{id}")
    public ResponseEntity<?> updateStudent(@PathVariable Long id,@Valid @RequestBody StudentDTO studentDTO) {

        if(!studentService.updateStudent(id, studentDTO).isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }


        StudentDTO createdStudent = studentService.createStudent(studentDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(studentAssembler.toModel(createdStudent));


    }



    @PatchMapping("/{id}")
    public ResponseEntity<?> patchStudent(@PathVariable Long id, @RequestBody Map<String, Object> fields) throws JsonMappingException {


        if (fields == null || fields.isEmpty()){
            return new ResponseEntity<>("No fields to be updated",HttpStatus.BAD_REQUEST);
        }


        ResponseEntity<?> response = studentService.patchStudent(id, fields)
                    .map(patchedStudent -> ResponseEntity.ok(studentAssembler.toModel(patchedStudent)))
                    .orElse(ResponseEntity.notFound().build());
        return response;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteStudent(@PathVariable Long id) {
        if(!studentService.existsStudent(id))
            return new ResponseEntity<>("Student not found",HttpStatus.NOT_FOUND);
        ResponseEntity<?> response = studentService.deleteStudent(id)
                .map(deletedStudent -> ResponseEntity.ok(studentAssembler.toModel(deletedStudent)))
                .orElse(ResponseEntity.notFound().build());
        return response;
    }


}
