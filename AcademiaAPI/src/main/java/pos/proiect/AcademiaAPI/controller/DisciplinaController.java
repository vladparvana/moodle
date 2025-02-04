package pos.proiect.AcademiaAPI.controller;

import com.fasterxml.jackson.databind.JsonMappingException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pos.proiect.AcademiaAPI.assembler.DisciplinaAssembler;
import pos.proiect.AcademiaAPI.dto.CadruDidacticDTO;
import pos.proiect.AcademiaAPI.dto.DisciplinaDTO;
import pos.proiect.AcademiaAPI.exceptions.CadruDidacticNotFound;
import pos.proiect.AcademiaAPI.exceptions.DisciplinaNotFoundException;
import pos.proiect.AcademiaAPI.repository.DisciplinaRepository;
import pos.proiect.AcademiaAPI.service.DisciplinaService;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/academia/lectures")
public class DisciplinaController {

    @Autowired
    private DisciplinaService disciplinaService;

    @Autowired
    private DisciplinaAssembler disciplinaAssembler;

    @GetMapping()
    public ResponseEntity<CollectionModel<EntityModel<DisciplinaDTO>>> getAllLecture(
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String category,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer items_per_page
    ) {
        Pageable pageable = PageRequest.of(page,items_per_page);
        Page<DisciplinaDTO> lecturesPage = disciplinaService.getAllLectures(type,category,pageable);

        List<EntityModel<DisciplinaDTO>> lectures = lecturesPage.getContent().stream()
                .map(disciplinaAssembler::toModel)
                .collect(Collectors.toList());

        PagedModel.PageMetadata pageMetadata = new PagedModel.PageMetadata(
                lecturesPage.getSize(),
                lecturesPage.getNumber(),
                lecturesPage.getTotalElements(),
                lecturesPage.getTotalPages());
        PagedModel<EntityModel<DisciplinaDTO>> pagedModel = PagedModel.of(lectures,pageMetadata,linkTo(methodOn(DisciplinaController.class).getAllLecture(null, null, null,null)).withSelfRel());

        return new ResponseEntity<>(pagedModel, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getLectureById(@PathVariable String id) {
        ResponseEntity<?> response = disciplinaService.getLectureByCod(id)
                .map(disciplinaDTO -> disciplinaAssembler.toModel(disciplinaDTO))
                .map(disciplinaDTO -> ResponseEntity.ok(disciplinaDTO))
                .orElseThrow(() -> new DisciplinaNotFoundException(id));
        return response;
    }

    @PostMapping()
    public ResponseEntity<?> createLecture(@Valid @RequestBody DisciplinaDTO disciplinaDTO) {

        DisciplinaDTO createdLecture = disciplinaService.createLecture(disciplinaDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(disciplinaAssembler.toModel(createdLecture));
    }

    @PostMapping("/{id}/add-student")
    public ResponseEntity<?> addStudent(@PathVariable String id,@RequestParam Long idStudent)
    {
        DisciplinaDTO updatedLecture = disciplinaService.addStudent(id,idStudent);
        return ResponseEntity.status(HttpStatus.OK).body(disciplinaAssembler.toModel(updatedLecture));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateLecture(@PathVariable String id, @Valid @RequestBody DisciplinaDTO disciplinaDTO)
    {
        if(!disciplinaService.updateLecture(id,disciplinaDTO).isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        DisciplinaDTO createdLecture = disciplinaService.createLecture(disciplinaDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(disciplinaAssembler.toModel(createdLecture));

    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> patchLecture(@PathVariable String id, @RequestBody Map<String, Object> fields) throws JsonMappingException {


        if (fields == null || fields.isEmpty()){
            return new ResponseEntity<>("No fields to be updated",HttpStatus.BAD_REQUEST);
        }

        ResponseEntity<?> response = disciplinaService.patchLecture(id, fields)
                .map(patchedLecture -> ResponseEntity.ok(disciplinaAssembler.toModel(patchedLecture)))
                .orElse(ResponseEntity.notFound().build());
        return response;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDisciplina(@PathVariable String id) {
        if(!disciplinaService.existsLecture(id))
            return new ResponseEntity<>("Disciplina not found",HttpStatus.NOT_FOUND);
        ResponseEntity<?> response = disciplinaService.deleteLecture(id)
                .map(deletedLecture -> ResponseEntity.ok(disciplinaAssembler.toModel(deletedLecture)))
                .orElse(ResponseEntity.notFound().build());
        return response;
    }

}
