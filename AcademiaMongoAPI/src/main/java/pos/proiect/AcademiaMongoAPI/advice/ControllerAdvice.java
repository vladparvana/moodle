package pos.proiect.AcademiaMongoAPI.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pos.proiect.AcademiaMongoAPI.exception.DisciplinaNotFoundException;
import pos.proiect.AcademiaMongoAPI.exception.MaterialCursAlreadyExistsException;
import pos.proiect.AcademiaMongoAPI.exception.MaterialLaboratorAlreadyExistsException;
import pos.proiect.AcademiaMongoAPI.exception.ProbaEvaluareValidationException;

import java.util.Map;

@RestControllerAdvice
public class ControllerAdvice {
    @ExceptionHandler(DisciplinaNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String disciplinaNotFound(DisciplinaNotFoundException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(MaterialCursAlreadyExistsException.class)
    public ResponseEntity<?> handleMaterialCursAlreadyExistsException(MaterialCursAlreadyExistsException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of(
                "error", "Conflict",
                "message", ex.getMessage()
        ));
    }

    @ExceptionHandler(MaterialLaboratorAlreadyExistsException.class)
    public ResponseEntity<?> handleMaterialLaboratorAlreadyExistsException(MaterialLaboratorAlreadyExistsException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of(
                "error", "Conflict",
                "message", ex.getMessage()
        ));
    }

    @ExceptionHandler(ProbaEvaluareValidationException.class)
    public ResponseEntity<?> handleValidationException(ProbaEvaluareValidationException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                "error", "Validation Error",
                "message", ex.getMessage()
        ));
    }
}

