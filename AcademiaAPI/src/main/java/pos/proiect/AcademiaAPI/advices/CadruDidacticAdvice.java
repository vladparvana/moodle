package pos.proiect.AcademiaAPI.advices;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pos.proiect.AcademiaAPI.exceptions.CadruDidacticNotFound;
import pos.proiect.AcademiaAPI.exceptions.StudentNotFoundException;

@RestControllerAdvice
public class CadruDidacticAdvice {
    @ExceptionHandler(CadruDidacticNotFound.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String cadruDidacticNotFoundHandler(CadruDidacticNotFound e) {
        return e.getMessage();
    }
}
