package pos.proiect.AcademiaMongoAPI.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import pos.proiect.AcademiaMongoAPI.entity.MaterialCurs;

import java.util.Optional;

@Repository
public interface MaterialCursRepository extends MongoRepository<MaterialCurs, ObjectId> {
    Optional<MaterialCurs> findByCodDisciplina(String codDisciplina);
    boolean existsByCodDisciplina(String codDisciplina);
}
