package pos.proiect.AcademiaAPI.dto;

import lombok.Data;
import pos.proiect.AcademiaAPI.enums.CicluStudii;

@Data
public class StudentSimpleDTO {
    private Long id;
    private String nume;
    private String prenume;
    private String email;
    private CicluStudii cicluStudii;
    private Integer anStudiu;
    private Integer grupa;
}
