package pos.proiect.AcademiaMongoAPI.mapper;

import org.bson.types.Binary;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import pos.proiect.AcademiaMongoAPI.dto.MaterialCursDTO;
import pos.proiect.AcademiaMongoAPI.entity.MaterialCurs;

import java.util.HashMap;
import java.util.Map;

@Mapper
public interface MaterialCursMapper {
    MaterialCursMapper INSTANCE =  Mappers.getMapper(MaterialCursMapper.class);


    @Mapping(target = "continut",  expression = "java(binaryToByteArray(materialCurs.getContinut()))")
    @Mapping(target="capitole",  expression = "java(mapBinaryToByteArray(materialCurs.getCapitole()))")
    MaterialCursDTO toDTO(MaterialCurs materialCurs);

    @Mapping(target = "continut",  expression = "java(byteArrayToBinary(materialCursDTO.getContinut()))")
    @Mapping(target="capitole",  expression = "java(mapByteArrayToBinary(materialCursDTO.getCapitole()))")
    MaterialCurs toEntity(MaterialCursDTO materialCursDTO);

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