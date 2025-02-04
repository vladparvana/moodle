package pos.proiect.AcademiaAPI.listener;

import jakarta.persistence.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pos.proiect.AcademiaAPI.entity.Student;


public class StudentListener {

    private static Logger log = LoggerFactory.getLogger(StudentListener.class);


    @PreUpdate
    private void preUpdate(Student student) {
        log.info("[STUDENT] Student will be updated: " + student.toString() );
    }

    @PostUpdate
    private void postUpdate(Student student) {
        log.info("[STUDENT] Student updated: " + student.toString() );
    }

    @PrePersist
    private void prePersist(Student student) {
        log.info("[STUDENT] Student will be created: " + student.toString() );
    }

    @PostPersist
    private void postPersist(Student student) {
        log.info("[STUDENT] Student created: " + student.toString() );
    }

    @PreRemove
    private void preRemove(Student student) {
        log.info("[STUDENT] Student will be removed: " + student.toString() );
    }

    @PostRemove
    private void postRemove(Student student) {
        log.info("[STUDENT] Student removed: " + student.toString() );
    }
}
