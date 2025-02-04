package pos.proiect.AcademiaMongoAPI.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.bson.types.Binary;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;

@Data
public class MaterialLaboratorDTO {
    private ObjectId id;
    private String codDisciplina;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String titlu;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private byte[] continut;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Map<String, byte[]> capitole;
}
