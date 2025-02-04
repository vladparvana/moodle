package pos.proiect.AcademiaAPI.listener;

import jakarta.persistence.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pos.proiect.AcademiaAPI.entity.Disciplina;

public class DisciplinaListener {
    private static Logger log = LoggerFactory.getLogger(DisciplinaListener.class);


    @PreUpdate
    private void preUpdate(Disciplina disciplina) {
        log.info("[Disciplina] Disciplina will be updated: " + disciplina.toString() );
    }


    @PrePersist
    private void prePersist(Disciplina disciplina) {
        if (disciplina.getCod() == null || disciplina.getCod().isEmpty()) {
            String codGenerat = disciplina.getNumeDisciplina()
                    .replaceAll("\\s+", "")
                    .toUpperCase()
                    .substring(0, Math.min(4, disciplina.getNumeDisciplina().length()));
            disciplina.setCod(codGenerat+"_"+ disciplina.getAnStudiu());
        }
        log.info("[Disciplina] Disciplina will be created: " + disciplina.toString() );
    }

    @PostUpdate
    private void postUpdate(Disciplina disciplina) {
        log.info("[Disciplina] Disciplina updated: " + disciplina.toString() );
    }


    @PostPersist
    private void postPersist(Disciplina disciplina) {

        log.info("[Disciplina] Disciplina created: " + disciplina.toString() );
    }

    @PreRemove
    private void preRemove(Disciplina disciplina) {
        log.info("[Disciplina] Disciplina will be removed: " + disciplina.toString() );
    }

    @PostRemove
    private void postRemove(Disciplina disciplina) {
        log.info("[Disciplina] Disciplina removed: " + disciplina.toString() );
    }
}
