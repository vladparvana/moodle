package pos.proiect.AcademiaAPI.listener;

import jakarta.persistence.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pos.proiect.AcademiaAPI.entity.CadruDidactic;
import pos.proiect.AcademiaAPI.entity.Student;

public class CadruDidacticListener {
    private static Logger log = LoggerFactory.getLogger(CadruDidacticListener.class);

    @PreUpdate
    private void preUpdate(CadruDidactic cadruDidactic) {
        log.info("[PROFESSOR] Professor will be updated: " + cadruDidactic.toString() );
    }

    @PostUpdate
    private void postUpdate(CadruDidactic cadruDidactic) {
        log.info("[PROFESSOR] Professor updated: " + cadruDidactic.toString() );
    }

    @PrePersist
    private void prePersist(CadruDidactic cadruDidactic) {
        log.info("[PROFESSOR] Professor will be created: " + cadruDidactic.toString() );
    }

    @PostPersist
    private void postPersist(CadruDidactic cadruDidactic) {
        log.info("[PROFESSOR] Professor created: " + cadruDidactic.toString() );
    }

    @PreRemove
    private void preRemove(CadruDidactic cadruDidactic) {
        log.info("[PROFESSOR] Professor will be removed: " + cadruDidactic.toString() );
    }

    @PostRemove
    private void postRemove(CadruDidactic cadruDidactic) {
        log.info("[PROFESSOR] Professor removed: " + cadruDidactic.toString() );
    }
}
