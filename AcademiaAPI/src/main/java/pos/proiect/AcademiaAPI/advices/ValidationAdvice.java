package pos.proiect.AcademiaAPI.advices;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@RestControllerAdvice
public class ValidationAdvice {

    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));
        return errors;
    }

    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    String handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        String errorMessage = ex.getMessage();
        StringBuilder detailedErrorMessage = new StringBuilder("Campuri invalide: ");

        if (errorMessage != null && !errorMessage.isEmpty()) {
            detailedErrorMessage.append(errorMessage);
        } else {
            detailedErrorMessage.append("Verificati campurile de input.");
        }

        return detailedErrorMessage.toString();
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<Map<String, String>> dataIntegrityViolationHandler(DataIntegrityViolationException e) {
        Map<String, String> response = new HashMap<>();
        response.put("error", "Exceptie de integritate");
        response.put("details", e.getMostSpecificCause().getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }


    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseBody
    public ResponseEntity<Map<String, String>> handleConstraintViolation(ConstraintViolationException ex) {
        Set<ConstraintViolation<?>> violations = ex.getConstraintViolations();
        Map<String, String> invalidFields = new HashMap<>();

        for (ConstraintViolation<?> violation : violations) {
            invalidFields.put(violation.getPropertyPath().toString(), violation.getMessage());
        }

        return new ResponseEntity<>(invalidFields, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(InvalidFormatException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public Map<String, String> handleInvalidFormatException(InvalidFormatException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", "Invalid value for enum type.");
        error.put("details", ex.getOriginalMessage());
        return error;
    }

}
