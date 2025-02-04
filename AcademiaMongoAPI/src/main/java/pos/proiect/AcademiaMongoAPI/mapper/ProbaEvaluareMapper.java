package pos.proiect.AcademiaMongoAPI.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import pos.proiect.AcademiaMongoAPI.dto.ProbaEvaluareDTO;
import pos.proiect.AcademiaMongoAPI.entity.ProbaEvaluare;

@Mapper
public interface ProbaEvaluareMapper {
    ProbaEvaluareMapper INSTANCE = Mappers.getMapper(ProbaEvaluareMapper.class);

    ProbaEvaluareDTO toDto(ProbaEvaluare probaEvaluare);
    ProbaEvaluare toEntity(ProbaEvaluareDTO probaEvaluareDTO);
}
