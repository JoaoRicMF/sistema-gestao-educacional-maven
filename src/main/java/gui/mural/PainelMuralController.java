package gui.mural;

import excecoes.ValidacaoExcecoes;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import modelo.Professor;
import modelo.Turma;
import service.MuralService;
import java.util.List;

public class PainelMuralController {
    private MuralService muralService;
    private List<Turma> todasAsTurmas;
    private List<Professor> todosOsProfessores;

    @FXML private ComboBox<Turma> cbTurmas;
    @FXML private TextField txtTitulo;
    @FXML private TextArea txtConteudo;

    public void setMuralService(MuralService muralService) {
        this.muralService = muralService;
    }

    public void setTodasAsTurmas(List<Turma> todasAsTurmas) {
        this.todasAsTurmas = todasAsTurmas;
        if (cbTurmas != null) {
            cbTurmas.getItems().setAll(this.todasAsTurmas);
        }
    }
    public void setTodosOsProfessores(List<Professor> todosOsProfessores) {
        this.todosOsProfessores = todosOsProfessores;
    }

    @FXML
    public void initialize() {
        cbTurmas.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Turma turma, boolean empty) {
                super.updateItem(turma, empty);
                setText(empty ? null : turma.getNomeTurma());
            }
        });
        cbTurmas.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Turma turma, boolean empty) {
                super.updateItem(turma, empty);
                setText(empty ? null : turma.getNomeTurma());
            }
        });
    }

    @FXML
    private void enviarAviso() {
        try {
            Turma turma = cbTurmas.getValue();
            String titulo = txtTitulo.getText();
            String conteudo = txtConteudo.getText();
            Professor autor = (todosOsProfessores != null && !todosOsProfessores.isEmpty()) ? todosOsProfessores.get(0) : null;

            muralService.postarNoMural(titulo, conteudo, autor, turma);

            showAlert(Alert.AlertType.INFORMATION, "Sucesso", "Aviso enviado para a turma " + turma.getNomeTurma());
            limparCampos();
        } catch (ValidacaoExcecoes e) {
            showAlert(Alert.AlertType.ERROR, "Erro de Validação", e.getMessage());
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erro", "Ocorreu um erro ao enviar o aviso.");
            e.printStackTrace();
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