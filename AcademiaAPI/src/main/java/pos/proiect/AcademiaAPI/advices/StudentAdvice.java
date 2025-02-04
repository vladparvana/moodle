package pos.proiect.AcademiaAPI.advices;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pos.proiect.AcademiaAPI.exceptions.StudentNotFoundException;

@RestControllerAdvice
public class StudentAdvice {

    @ExceptionHandler(StudentNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String studentNotFoundHandler(StudentNotFoundException e) {
        return e.getMessage();
    }

}
