package pos.proiect.AcademiaAPI.exceptions;

public class StudentNotFoundException extends RuntimeException {
     public StudentNotFoundException(Long id) {
        super("Studentul cu id-ul " + id + " nu a fost gasit");
    }
}
