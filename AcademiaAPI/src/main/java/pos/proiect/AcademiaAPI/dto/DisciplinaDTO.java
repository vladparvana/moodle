package pos.proiect.AcademiaAPI.dto;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pos.proiect.AcademiaAPI.entity.CadruDidactic;
import pos.proiect.AcademiaAPI.entity.Disciplina;
import pos.proiect.AcademiaAPI.entity.Student;
import pos.proiect.AcademiaAPI.enums.CategorieDisciplina;
import pos.proiect.AcademiaAPI.enums.TipDisciplina;
import pos.proiect.AcademiaAPI.enums.TipExaminare;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DisciplinaDTO {
    private String cod;

    @NotNull(message = "ID-ul titularului nu poate fi gol")
    private Long idTitular;

    @NotNull(message = "Numele nu poate fi gol")
    @Pattern(regexp = "^[a-zA-Z\\s]+$", message = "Numele poate conține doar litere și spații.")
    @Size(min=2,message = "Numele e prea scurt")
    private String numeDisciplina;

    @NotNull(message = "Anul nu poate fi gol")
    private Integer anStudiu;

    @NotNull(message = "Tipul disciplinei nu poate fi gol")
    @Enumerated(EnumType.STRING)
    private TipDisciplina tipDisciplina;

    @NotNull(message = "Categoria disciplinei nu poate fi goala")
    @Enumerated(EnumType.STRING)
    private CategorieDisciplina categorieDisciplina;

    @NotNull(message = "Tipul examinarei nu poate fi gol")
    @Enumerated(EnumType.STRING)
    private TipExaminare tipExaminare;

    private Set<StudentSimpleDTO> students = new LinkedHashSet<>();





}