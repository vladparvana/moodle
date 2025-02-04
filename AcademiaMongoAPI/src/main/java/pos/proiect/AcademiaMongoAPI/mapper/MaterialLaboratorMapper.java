package pos.proiect.AcademiaMongoAPI.mapper;

import org.bson.types.Binary;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import pos.proiect.AcademiaMongoAPI.dto.MaterialLaboratorDTO;
import pos.proiect.AcademiaMongoAPI.entity.MaterialLaborator;

import java.util.HashMap;
import java.util.Map;

@Mapper
public interface MaterialLaboratorMapper {
    MaterialLaboratorMapper INSTANCE =  Mappers.getMapper(MaterialLaboratorMapper.class);

    @Mapping(target = "continut",  expression = "java(binaryToByteArray(materialLaborator.getContinut()))")
    @Mapping(target="capitole",  expression = "java(mapBinaryToByteArray(materialLaborator.getCapitole()))")
    MaterialLaboratorDTO toDTO(MaterialLaborator materialLaborator);

    @Mapping(target = "continut",  expression = "java(byteArrayToBinary(materialLaboratorDTO.getContinut()))")
    @Mapping(target="capitole",  expression = "java(mapByteArrayToBinary(materialLaboratorDTO.getCapitole()))")
    MaterialLaborator toEntity(MaterialLaboratorDTO materialLaboratorDTO);

    default byte[] binaryToByteArray(Binary binary) {
        if(binary == null)
            return null;
        return binary.getData();
    }

    default Map<String, byte[]> mapBinaryToByteArray(Map<String, Binary> binaryMap) {
        if(binaryMap == null)
            return null;
        Map<String, byte[]> newMap = new HashMap<>();
        binaryMap.forEach((key,value) -> newMap.put(key,value.getData()));
        return newMap;
    }


    default Binary byteArrayToBinary(byte[] byteArray) {
        if(byteArray == null)
            return null;
        return new Binary(byteArray);
    }


    default Map<String, Binary> mapByteArrayToBinary(Map<String, byte[]> byteArrayMap) {
        if(byteArrayMap == null)
            return null;
        Map<String, Binary> newMap = new HashMap<>();
        byteArrayMap.forEach((key,value) -> newMap.put(key, new Binary(value)));
        return newMap;
    }

}
