package pos.proiect.AcademiaAPI.controller;

import com.fasterxml.jackson.databind.JsonMappingException;
import jakarta.persistence.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pos.proiect.AcademiaAPI.assembler.CadruDidacticAssembler;
import pos.proiect.AcademiaAPI.dto.CadruDidacticDTO;
import pos.proiect.AcademiaAPI.dto.StudentDTO;
import pos.proiect.AcademiaAPI.enums.GradDidactic;
import pos.proiect.AcademiaAPI.enums.TipAsociere;
import pos.proiect.AcademiaAPI.exceptions.CadruDidacticNotFound;
import pos.proiect.AcademiaAPI.exceptions.StudentNotFoundException;
import pos.proiect.AcademiaAPI.service.CadruDidacticService;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


@RestController
@RequestMapping("/api/academia/professors")
public class CadruDidacticController {

    @Autowired
    private CadruDidacticService cadruDidacticService;

    @Autowired
    private CadruDidacticAssembler cadruDidacticAssembler;

    @GetMapping()
    public ResponseEntity<CollectionModel<EntityModel<CadruDidacticDTO>>> getAllProfessors(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String acad_rank,
            @RequestParam(required = false) String asociation,
            @RequestParam(required = false) String lecture
    ) {

        List<EntityModel<CadruDidacticDTO>> professors = cadruDidacticService.getAllProfessors(name,acad_rank,asociation,lecture).stream()
                .map(cadruDidacticAssembler::toModel)
                .collect(Collectors.toList());



        CollectionModel<EntityModel<CadruDidacticDTO>> collectionModel = CollectionModel.of(professors, linkTo(methodOn(CadruDidacticController.class).getAllProfessors(null,null,null,lecture)).withSelfRel());
        return new ResponseEntity<>(collectionModel, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProfessorById(@PathVariable Long id) {
        ResponseEntity<?> response = cadruDidacticService.getProfessorById(id)
                .map(cadruDidacticDTO -> cadruDidacticAssembler.toModel(cadruDidacticDTO))
                .map(cadruDidacticDTO -> ResponseEntity.ok(cadruDidacticDTO))
                .orElseThrow(() -> new CadruDidacticNotFound(id));
        return response;
    }

    @PostMapping()
    public ResponseEntity<?> createProfessor(@Valid @RequestBody CadruDidacticDTO cadruDidacticDTO) {

        CadruDidacticDTO createdProfessor = cadruDidacticService.createProfessor(cadruDidacticDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(cadruDidacticAssembler.toModel(createdProfessor));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateProfessor(@PathVariable Long id, @Valid @RequestBody CadruDidacticDTO cadruDidacticDTO)
    {
        if(!cadruDidacticService.updateProfessor(id,cadruDidacticDTO).isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        CadruDidacticDTO createdProfessor = cadruDidacticService.createProfessor(cadruDidacticDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(cadruDidacticAssembler.toModel(createdProfessor));

    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> patchProfessor(@PathVariable Long id, @RequestBody Map<String, Object> fields) throws JsonMappingException {


        if (fields == null || fields.isEmpty()){
            return new ResponseEntity<>("No fields to be updated",HttpStatus.BAD_REQUEST);
        }

        ResponseEntity<?> response = cadruDidacticService.patchProfessor(id, fields)
                .map(patchedProfessor -> ResponseEntity.ok(cadruDidacticAssembler.toModel(patchedProfessor)))
                .orElse(ResponseEntity.notFound().build());
        return response;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProfessor(@PathVariable Long id) {
        if(!cadruDidacticService.existsProfessor(id))
            return new ResponseEntity<>("Student not found",HttpStatus.NOT_FOUND);
        ResponseEntity<?> response = cadruDidacticService.deleteProfessor(id)
                .map(deletedProfessor -> ResponseEntity.ok(cadruDidacticAssembler.toModel(deletedProfessor)))
                .orElse(ResponseEntity.notFound().build());
        return response;
    }


}
