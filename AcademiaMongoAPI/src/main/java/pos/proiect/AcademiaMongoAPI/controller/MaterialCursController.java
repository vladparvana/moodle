package pos.proiect.AcademiaMongoAPI.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pos.proiect.AcademiaMongoAPI.dto.MaterialCursDTO;
import pos.proiect.AcademiaMongoAPI.enums.Tip;
import pos.proiect.AcademiaMongoAPI.service.MaterialCursService;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/material-curs")
public class MaterialCursController {
    @Autowired
    private MaterialCursService materialCursService;

    @GetMapping
    public ResponseEntity<?> getAll() {
        List<MaterialCursDTO> materials = materialCursService.getAll();
        if (materials.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(materials);
    }

    @GetMapping("/{codDisciplina}")
    public ResponseEntity<?> getByCodDisciplina(@PathVariable String codDisciplina) {
        Optional<MaterialCursDTO> material = materialCursService.getByCodDisciplina(codDisciplina);
        if (material.isPresent()) {
            return ResponseEntity.ok(material.get());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Materialul pentru disciplina " + codDisciplina + " nu a fost găsit");
    }

    @PostMapping("/{codDisciplina}")
    public ResponseEntity<?> addMaterial(
            @PathVariable String codDisciplina,
            @RequestParam("type") Tip type,
            @RequestParam("title") String title,
            @RequestParam("content") MultipartFile content
            ) throws IOException {
        return new ResponseEntity<>(materialCursService.addMaterial(codDisciplina,type,title,content), HttpStatus.CREATED);
    }

    @PutMapping("/{codDisciplina}")
    public ResponseEntity<?> updateMaterial(
            @PathVariable String codDisciplina,
            @RequestParam("type") Tip type,
            @RequestParam(value = "title", required = false) String title,
            @RequestParam("content") MultipartFile content
    ) throws IOException {
        MaterialCursDTO updatedMaterial = materialCursService.updateMaterial(codDisciplina, type, title, content);
        return ResponseEntity.status(HttpStatus.OK).body(updatedMaterial);
    }

    @DeleteMapping("/{codDisciplina}")
    public ResponseEntity<?> deleteMaterial(
            @PathVariable String codDisciplina,
            @RequestParam("type") Tip type,
            @RequestParam(value = "title", required = false) String title
    ) {
        materialCursService.deleteMaterial(codDisciplina, type, title);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/{codDisciplina}/continut")
    public ResponseEntity<?> getEntireContent(@PathVariable String codDisciplina) {
        byte[] content = materialCursService.getContinut(codDisciplina);
        return ResponseEntity.status(HttpStatus.OK)
                .header("Content-Type", "application/pdf")
                .body(content);
    }

    @GetMapping("/{codDisciplina}/capitol/{title}")
    public ResponseEntity<?> getChapterContent(
            @PathVariable String codDisciplina,
            @PathVariable String title
    ) {
        byte[] content = materialCursService.getCapitolContinut(codDisciplina, title);
        return ResponseEntity.status(HttpStatus.OK)
                .header("Content-Type", "application/pdf")
                .body(content);
    }
}
