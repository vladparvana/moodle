package pos.proiect.AcademiaAPI.dto;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pos.proiect.AcademiaAPI.entity.CadruDidactic;
import pos.proiect.AcademiaAPI.entity.Disciplina;
import pos.proiect.AcademiaAPI.enums.GradDidactic;
import pos.proiect.AcademiaAPI.enums.TipAsociere;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CadruDidacticDTO {
    private Long id;

    @Size(max = 255)
    @NotNull(message = "Numele nu poate fi gol")
    @Pattern(regexp = "^[a-zA-Z\\s]+$", message = "Numele poate conține doar litere și spații.")
    @Size(min=2,message = "Numele e prea scurt")
    private String nume;

    @Size(max = 255)
    @Size(min=2,message = "Prenumele e prea scurt")
    @NotNull(message = "Prenumele nu poate fi gol")
    @Pattern(regexp = "^[a-zA-Z\\s]+$", message = "Prenmele poate conține doar litere și spații.")
    private String prenume;

    @Size(max = 255)
    @Email(message = "Email-ul nu are formatul corespunzator")
    private String email;

    @Enumerated(EnumType.STRING)
    private GradDidactic gradDidactic;


    @NotNull(message = "Tipul asocierii nu poate fi gol")
    @Enumerated(EnumType.STRING)
    private TipAsociere tipAsociere;

    @Size(max = 255)
    private String afiliere;

    private Set<DisciplinaDTO> discipline = new LinkedHashSet<>();



}
