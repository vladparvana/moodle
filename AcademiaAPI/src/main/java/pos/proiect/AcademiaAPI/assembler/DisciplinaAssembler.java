package pos.proiect.AcademiaAPI.assembler;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import pos.proiect.AcademiaAPI.controller.CadruDidacticController;
import pos.proiect.AcademiaAPI.controller.DisciplinaController;
import pos.proiect.AcademiaAPI.dto.DisciplinaDTO;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class DisciplinaAssembler implements RepresentationModelAssembler<DisciplinaDTO, EntityModel<DisciplinaDTO>> {
    @Value("${mongo.api.url}")
    private String mongoApiUrl;

    @Override
    public EntityModel<DisciplinaDTO> toModel(DisciplinaDTO disciplinaDTO) {
        {
            String mongoLink = mongoApiUrl  + disciplinaDTO.getCod();

            return EntityModel.of(disciplinaDTO,
                    linkTo(methodOn(DisciplinaController.class).getLectureById(disciplinaDTO.getCod())).withSelfRel(),
                    linkTo(methodOn(DisciplinaController.class).getAllLecture(disciplinaDTO.getTipDisciplina().toString(),disciplinaDTO.getCategorieDisciplina().toString(),null,null)).withRel("parent"),
                    Link.of(mongoLink).withRel("materiale-curs"),
                    Link.of(mongoLink).withRel("materiale-laborator"));
        }
    }
}
