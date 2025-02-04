package pos.proiect.AcademiaMongoAPI.dto;

import lombok.Data;
import org.bson.types.ObjectId;

@Data
public class ProbaEvaluareDTO {
    private ObjectId id;
    private String codDisciplina;
    private String nume;
    private Double pondere;
}
