package pos.proiect.AcademiaMongoAPI.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.bson.types.Binary;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;

@Data
@Document(collection = "materiale_curs")
public class MaterialCurs {
    @Id
    private ObjectId id;
    private String codDisciplina;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String titlu;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Binary continut;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Map<String, Binary> capitole;
}

