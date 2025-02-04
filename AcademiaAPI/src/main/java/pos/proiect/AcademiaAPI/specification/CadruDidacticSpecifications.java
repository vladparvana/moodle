package pos.proiect.AcademiaAPI.specification;

import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;
import pos.proiect.AcademiaAPI.entity.CadruDidactic;
import pos.proiect.AcademiaAPI.entity.Disciplina;
import pos.proiect.AcademiaAPI.enums.GradDidactic;
import pos.proiect.AcademiaAPI.enums.TipAsociere;

public class CadruDidacticSpecifications {
    public static Specification<CadruDidactic> hasGradDidactic(GradDidactic gradDidactic) {
        return (Root<CadruDidactic> root,CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) ->
                criteriaBuilder.equal(root.get("gradDidactic"), gradDidactic);
    }

    public static Specification<CadruDidactic> hasTipAsociere(TipAsociere tipAsociere) {
        return (Root<CadruDidactic> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) ->
                criteriaBuilder.equal(root.get("tipAsociere"), tipAsociere);
    }

    public static Specification<CadruDidactic> hasNume(String nume) {
        return (Root<CadruDidactic> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) ->
                criteriaBuilder.like(criteriaBuilder.lower(root.get("nume")),  nume.substring(0,1).toUpperCase() + nume.substring(1).toLowerCase() + "%");
    }

    public static Specification<CadruDidactic> hasDisciplina(String lecture) {
        return (Root<CadruDidactic> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
            Join<CadruDidactic, Disciplina> disciplinaJoin = root.join("discipline", JoinType.INNER);
            return criteriaBuilder.like(criteriaBuilder.lower(disciplinaJoin.get("numeDisciplina")), "%" + lecture + "%");
        };
    }
}
