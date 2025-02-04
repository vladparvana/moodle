package pos.proiect.AcademiaAPI.dto;

import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import pos.proiect.AcademiaAPI.entity.Disciplina;
import pos.proiect.AcademiaAPI.entity.Student;
import pos.proiect.AcademiaAPI.enums.CicluStudii;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class StudentDTO {
    private Long id;

    @NotNull(message = "Numele nu poate fi gol")
    @Size(min=2,message = "Numele e prea scurt")
    @Pattern(regexp = "^[a-zA-Z\\s]+$", message = "Numele poate conține doar litere și spații.")
    private String nume;

    @NotNull(message = "Prenumele nu poate fi gol")
    @Size(min=2,message = "Prenumele e prea scurt")
    @Pattern(regexp = "^[a-zA-Z\\s]+$", message = "Prenumele poate conține doar litere și spații.")
    private String prenume;

    @Size(max=255)
    @Email(message = "Email-ul nu are formatul corespunzator")
    private String email;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Ciclul de studii nu poate fi gol")
    private CicluStudii cicluStudii;


    @NotNull(message = "Anul de studiu nu poate fi gol")
    private Integer anStudiu;

    @NotNull(message = "Grupa nu poate fi goala")
    private Integer grupa;

    private Set<DisciplinaSimpleDTO> discipline = new LinkedHashSet<>();


}