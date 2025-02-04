package pos.proiect.AcademiaMongoAPI.exception;

public class MaterialCursAlreadyExistsException extends RuntimeException {
    public MaterialCursAlreadyExistsException(String codDisciplina) {
        super("Material de curs complet deja există pentru disciplina cu codul: " + codDisciplina);
    }
}