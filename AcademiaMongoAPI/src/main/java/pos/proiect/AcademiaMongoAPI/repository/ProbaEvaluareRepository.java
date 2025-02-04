package pos.proiect.AcademiaMongoAPI.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import pos.proiect.AcademiaMongoAPI.entity.ProbaEvaluare;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProbaEvaluareRepository extends MongoRepository<ProbaEvaluare, ObjectId> {
    List<ProbaEvaluare> findAllByCodDisciplina(String codDisciplina);
    Optional<ProbaEvaluare> findById(String id);
    Optional<ProbaEvaluare> findByNume(String nume);
}
