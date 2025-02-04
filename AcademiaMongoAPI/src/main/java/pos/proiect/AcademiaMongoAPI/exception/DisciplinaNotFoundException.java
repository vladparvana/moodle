package pos.proiect.AcademiaMongoAPI.exception;

public class DisciplinaNotFoundException extends RuntimeException {
    public DisciplinaNotFoundException(String cod) {
        super("Disciplina cu codul " + cod + " nu a fost gasita");
    }
}
