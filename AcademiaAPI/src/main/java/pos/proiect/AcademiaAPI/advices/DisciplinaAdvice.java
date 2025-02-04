package pos.proiect.AcademiaAPI.advices;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pos.proiect.AcademiaAPI.exceptions.DisciplinaNotFoundException;
import pos.proiect.AcademiaAPI.exceptions.StudentAlreadyEnrolledException;
import pos.proiect.AcademiaAPI.exceptions.StudentNotFoundException;

@RestControllerAdvice
public class DisciplinaAdvice {
    @ExceptionHandler(DisciplinaNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String DisciplinaNotFound(DisciplinaNotFoundException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(StudentAlreadyEnrolledException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public String StudentNotFound(StudentAlreadyEnrolledException ex) {
        return ex.getMessage();
    }
}
