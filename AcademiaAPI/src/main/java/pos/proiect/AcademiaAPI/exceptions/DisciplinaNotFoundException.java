package pos.proiect.AcademiaAPI.exceptions;

public class DisciplinaNotFoundException extends RuntimeException {
    public DisciplinaNotFoundException(String cod) {
        super("Disciplina cu codul " + cod + " nu a fost gasita");
    }
}
