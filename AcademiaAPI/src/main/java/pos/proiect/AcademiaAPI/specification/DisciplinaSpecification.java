package pos.proiect.AcademiaAPI.specification;

import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;
import pos.proiect.AcademiaAPI.entity.Disciplina;
import pos.proiect.AcademiaAPI.enums.CategorieDisciplina;
import pos.proiect.AcademiaAPI.enums.TipDisciplina;

public class DisciplinaSpecification {
    public static Specification<Disciplina> hasTipDisciplina(TipDisciplina tipDisciplina) {
        return (Root<Disciplina> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) ->
                criteriaBuilder.equal(root.get("tipDisciplina"), tipDisciplina);
    }

    public static Specification<Disciplina> hasCategorieDisciplina(CategorieDisciplina categorieDisciplina) {
        return (Root<Disciplina> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) ->
                criteriaBuilder.equal(root.get("categorieDisciplina"), categorieDisciplina);
    }


}
