package pos.proiect.AcademiaMongoAPI.exception;

public class MaterialLaboratorAlreadyExistsException extends RuntimeException {
    public MaterialLaboratorAlreadyExistsException(String codDisciplina) {
        super("Material de curs complet deja există pentru disciplina cu codul: " + codDisciplina);
    }
}
