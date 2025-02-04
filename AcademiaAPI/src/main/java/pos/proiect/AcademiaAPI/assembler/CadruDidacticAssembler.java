package pos.proiect.AcademiaAPI.assembler;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import pos.proiect.AcademiaAPI.controller.CadruDidacticController;
import pos.proiect.AcademiaAPI.controller.StudentController;
import pos.proiect.AcademiaAPI.dto.CadruDidacticDTO;
import pos.proiect.AcademiaAPI.dto.StudentDTO;
import pos.proiect.AcademiaAPI.entity.CadruDidactic;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class CadruDidacticAssembler implements RepresentationModelAssembler<CadruDidacticDTO, EntityModel<CadruDidacticDTO>>
{
    @Override
    public EntityModel<CadruDidacticDTO> toModel(CadruDidacticDTO cadruDidacticDTO) {
        return EntityModel.of(cadruDidacticDTO,
                linkTo(methodOn(CadruDidacticController.class).getProfessorById(cadruDidacticDTO.getId())).withSelfRel(),
                linkTo(methodOn(CadruDidacticController.class).getAllProfessors(cadruDidacticDTO.getNume(),cadruDidacticDTO.getGradDidactic().toString(),cadruDidacticDTO.getTipAsociere().toString(),null)).withRel("parent"));
    }

}
