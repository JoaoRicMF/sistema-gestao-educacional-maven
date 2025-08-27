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
 * @author João Ricardo
 */
public class DetalhesTurmaController {
    @FXML private Label lblNomeTurma;
    @FXML private ListView<Disciplina> listDisciplinas;
    @FXML private ListView<Professor> listProfessores;

    public void setTurma(Turma turma) {
        if (turma != null) {
            lblNomeTurma.setText("Detalhes da Turma: " + turma.getNomeTurma());

            // Preenche a lista de disciplinas
            listDisciplinas.getItems().setAll(turma.getDisciplinas());

            // Simulação: Adiciona o professor da turma, se houver
            // No futuro, você pode buscar os professores associados a esta turma
            if (turma.getAlunos() != null && !turma.getAlunos().isEmpty()){
                // A lógica para obter o professor da turma precisa ser implementada
                // Por enquanto, vamos simular com dados estáticos
            }
        }
    }

    @FXML
    public void initialize() {
        // Formata como a disciplina será exibida na lista
        listDisciplinas.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Disciplina item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : item.getNomeDisciplina() + " (" + item.getCargaHoraria() + "h)");
            }
        });

        // Formata como o professor será exibido na lista
        listProfessores.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Professor item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : item.getNome());
            }
        });
    }
}
