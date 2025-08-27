package gui.mural;

import excecoes.ValidacaoExcecoes;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import modelo.Professor;
import modelo.Turma;
import service.MuralService;
import java.util.List;

/**
 *
 * @author João Ricardo
 */
public class PainelMuralController {
    private MuralService muralService;
    private List<Turma> todasAsTurmas;
    private List<Professor> todosOsProfessores;

    @FXML private ComboBox<Turma> cbTurmas;
    @FXML private TextField txtTitulo;
    @FXML private TextArea txtConteudo;

    public void setMuralService(MuralService muralService) { this.muralService = muralService; }
    public void setTodasAsTurmas(List<Turma> todasAsTurmas) { this.todasAsTurmas = todasAsTurmas; }
    public void setTodosOsProfessores(List<Professor> todosOsProfessores) { this.todosOsProfessores = todosOsProfessores; }

    @FXML
    public void initialize() {
        if (todasAsTurmas != null) {
            cbTurmas.getItems().setAll(todasAsTurmas);
        }
    }

    @FXML
    private void enviarAviso() {
        try {
            Turma turma = cbTurmas.getValue();
            String titulo = txtTitulo.getText();
            String conteudo = txtConteudo.getText();

            // Simula o professor logado (pega o primeiro da lista)
            Professor autor = (todosOsProfessores != null && !todosOsProfessores.isEmpty()) ? todosOsProfessores.get(0) : null;

            muralService.postarNoMural(titulo, conteudo, autor, turma);

            showAlert(Alert.AlertType.INFORMATION, "Sucesso", "Aviso enviado para a turma " + turma.getNomeTurma());
            limparCampos();
        } catch (ValidacaoExcecoes e) {
            showAlert(Alert.AlertType.ERROR, "Erro de Validação", e.getMessage());
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erro", "Ocorreu um erro ao enviar o aviso.");
        }
    }

    @FXML
    private void limparCampos() {
        cbTurmas.getSelectionModel().clearSelection();
        txtTitulo.clear();
        txtConteudo.clear();
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
