package pos.proiect.AcademiaAPI.exceptions;

public class CadruDidacticNotFound extends RuntimeException {
    public CadruDidacticNotFound(Long id) {
        super("Professorul cu id-ul " + id + " nu a fost gasit.");
    }
}
