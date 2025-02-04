package pos.proiect.AcademiaMongoAPI.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pos.proiect.AcademiaMongoAPI.dto.ProbaEvaluareDTO;
import pos.proiect.AcademiaMongoAPI.service.ProbaEvaluareService;

import java.util.List;

@RestController
@RequestMapping("/api/probe-evaluare")
public class ProbaEvaluareController {

    @Autowired
    private ProbaEvaluareService probaEvaluareService;

    @GetMapping
    public ResponseEntity<?> getAll() {
        return new ResponseEntity<>(probaEvaluareService.getAll(),HttpStatus.OK);
    }

    @GetMapping("/{codDisciplina}")
    public ResponseEntity<?> getProbeByCodDisciplina(@PathVariable String codDisciplina) {
        List<ProbaEvaluareDTO> probe = probaEvaluareService.getByCodDisciplina(codDisciplina);
        return new ResponseEntity<>(probe, HttpStatus.OK);
    }

    @PostMapping("/{codDisciplina}")
    public ResponseEntity<?> addProba(@PathVariable String codDisciplina,@RequestBody ProbaEvaluareDTO probaEvaluareDTO) {
        ProbaEvaluareDTO savedProba = probaEvaluareService.addProba(probaEvaluareDTO,codDisciplina);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedProba);
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> updateProba(@PathVariable String id,
            @RequestBody ProbaEvaluareDTO probaEvaluareDTO) {
        ProbaEvaluareDTO updatedProba = probaEvaluareService.updateProba(id, probaEvaluareDTO);
        return ResponseEntity.status(HttpStatus.OK).body(updatedProba);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProba(@PathVariable String id,@RequestBody ProbaEvaluareDTO probaEvaluareDTO) {
        probaEvaluareService.deleteProba(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}