package pos.proiect.AcademiaAPI.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pos.proiect.AcademiaAPI.dto.CadruDidacticDTO;
import pos.proiect.AcademiaAPI.enums.GradDidactic;
import pos.proiect.AcademiaAPI.enums.TipAsociere;
import pos.proiect.AcademiaAPI.listener.CadruDidacticListener;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@EntityListeners(CadruDidacticListener.class)
@Entity
@Table(name = "cadre_didactice")
@NoArgsConstructor
@AllArgsConstructor
public class CadruDidactic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Long id;

    @Size(max = 255)
    @NotNull
    @Column(name = "nume", nullable = false)
    private String nume;

    @Size(max = 255)
    @NotNull
    @Column(name = "prenume", nullable = false)
    private String prenume;

    @Size(max = 255)
    @Column(name = "email")
    private String email;

    @Column(name = "grad_didactic")
    @Enumerated(EnumType.STRING)
    private GradDidactic gradDidactic;

    @NotNull
    @Column(name = "tip_asociere", nullable = false)
    @Enumerated(EnumType.STRING)
    private TipAsociere tipAsociere;

    @Size(max = 255)
    @Column(name = "afiliere")
    private String afiliere;

    @OneToMany(mappedBy = "idTitular")
    private Set<Disciplina> discipline = new LinkedHashSet<>();



}