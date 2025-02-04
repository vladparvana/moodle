package pos.proiect.AcademiaAPI.exceptions;

public class StudentAlreadyEnrolledException extends RuntimeException {
    public StudentAlreadyEnrolledException(String cod,Long idStudent) {
        super("Studentul cu id-ul " + idStudent + " este deja inscris la disciplina cu codul "+cod);
    }
}
