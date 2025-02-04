package pos.proiect.AcademiaAPI.assembler;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import pos.proiect.AcademiaAPI.controller.StudentController;
import pos.proiect.AcademiaAPI.dto.StudentDTO;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class StudentAssembler implements RepresentationModelAssembler<StudentDTO, EntityModel<StudentDTO>> {
    @Override
    public EntityModel<StudentDTO> toModel(StudentDTO studentDTO) {
        return EntityModel.of(studentDTO,
                linkTo(methodOn(StudentController.class).getStudentById(studentDTO.getId())).withSelfRel(),
                linkTo(methodOn(StudentController.class).getAllStudents()).withRel("parent"));
    }
}
