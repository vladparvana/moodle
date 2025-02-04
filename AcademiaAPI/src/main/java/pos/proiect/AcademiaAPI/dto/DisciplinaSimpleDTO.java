package pos.proiect.AcademiaAPI.dto;

import lombok.Data;
import pos.proiect.AcademiaAPI.enums.CategorieDisciplina;
import pos.proiect.AcademiaAPI.enums.TipDisciplina;
import pos.proiect.AcademiaAPI.enums.TipExaminare;

@Data
public class DisciplinaSimpleDTO {
    private String cod;
    private Long idTitular;
    private String numeDisciplina;
    private Integer anStudiu;
    private TipDisciplina tipDisciplina;
    private CategorieDisciplina categorieDisciplina;
    private TipExaminare tipExaminare;
}
