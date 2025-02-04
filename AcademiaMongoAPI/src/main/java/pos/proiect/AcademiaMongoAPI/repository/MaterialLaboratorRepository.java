package pos.proiect.AcademiaMongoAPI.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import pos.proiect.AcademiaMongoAPI.entity.MaterialLaborator;

import java.util.Optional;

@Repository
public interface MaterialLaboratorRepository extends MongoRepository<MaterialLaborator, ObjectId> {
    Optional<MaterialLaborator> findByCodDisciplina(String codDisciplina);
    boolean existsByCodDisciplina(String codDisciplina);
}
