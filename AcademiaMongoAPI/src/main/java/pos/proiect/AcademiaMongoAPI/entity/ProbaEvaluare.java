package pos.proiect.AcademiaMongoAPI.entity;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "probe_evaluare")
public class ProbaEvaluare {
    @Id
    private ObjectId id;
    private String codDisciplina;
    private String nume;
    private Double pondere;
}
