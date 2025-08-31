package gui.visaoaluno;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import modelo.Disciplina;
import modelo.Professor;
import modelo.Turma;

/**
 *
 * @author Josemar
 */

public class DetalhesTurmaController {
    @FXML private Label lblNomeTurma;
    @FXML private ListView<Disciplina> listDisciplinas;
    @FXML private ListView<Professor> listProfessores;

    public void setTurma(Turma turma) {
        if (turma != null) {
            lblNomeTurma.setText("Detalhes da Turma: " + turma.getNomeTurma());

            listDisciplinas.getItems().setAll(turma.getDisciplinas());

            Professor professor = turma.getProfessor();
            if (professor != null) {
                listProfessores.getItems().setAll(professor);
            } else {
                listProfessores.getItems().clear();
            }
        }
    }

    @FXML
    public void initialize() {
        listDisciplinas.setCellFactory(lv -> new ListCell<Disciplina>() {
            @Override
            protected void updateItem(Disciplina item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : item.getNomeDisciplina() + " (" + item.getCargaHoraria() + "h)");
            }
        });

        listProfessores.setCellFactory(lv -> new ListCell<Professor>() {
            @Override
            protected void updateItem(Professor item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : item.getNome());
            }
        });

        listProfessores.setPlaceholder(new Label("Não há professor cadastrado para esta turma."));
    }
}