package gui.disciplina;

import excecoes.ValidacaoExcecoes;
import gui.util.Validacoes;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.VBox;
import modelo.Disciplina;
import service.DisciplinaService;
import java.util.List;
import java.util.Optional;
import gui.util.BuscaController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;

public class PainelDisciplinaController {
    @FXML private TextField txtNomeDisciplina;
    @FXML private TextField txtCargaHoraria;
    @FXML private TextField txtSerieSemestre;
    @FXML private Button btnBuscar;
    @FXML private Button btnExcluir;

    private DisciplinaService disciplinaService;
    private List<Disciplina> todasAsDisciplinas;
    private Disciplina disciplinaCarregada;

    public void setService(DisciplinaService service) {
        this.disciplinaService = service;
    }

    public void setTodasAsDisciplinas(List<Disciplina> todasAsDisciplinas) {
        this.todasAsDisciplinas = todasAsDisciplinas;
    }

    @FXML
    public void initialize() {
        if (this.disciplinaService == null) {
            this.disciplinaService = new DisciplinaService();
        }
        Validacoes.addRequiredFieldValidator(txtNomeDisciplina);
        Validacoes.addRequiredFieldValidator(txtCargaHoraria);
        Validacoes.addRequiredFieldValidator(txtSerieSemestre);
        limparCampos(null);
    }

    @FXML
    void salvarOuAtualizar(ActionEvent event) {
        try {
            String nome = txtNomeDisciplina.getText();
            int cargaHoraria = Integer.parseInt(txtCargaHoraria.getText());
            int serieSemestre = Integer.parseInt(txtSerieSemestre.getText());

            if (disciplinaCarregada != null) {
                disciplinaService.atualizarDadosDisciplina(disciplinaCarregada, nome, cargaHoraria, serieSemestre);
                showAlert(AlertType.INFORMATION, "Sucesso", "Disciplina '" + nome + "' atualizada com sucesso!");
            } else {
                Disciplina novaDisciplina = disciplinaService.cadastrarNovaDisciplina(nome, cargaHoraria, serieSemestre);
                if (todasAsDisciplinas != null) {
                    todasAsDisciplinas.add(novaDisciplina);
                }
                showAlert(AlertType.INFORMATION, "Sucesso", "Disciplina '" + nome + "' cadastrada com sucesso!");
            }
            limparCampos(null);

        } catch (NumberFormatException e) {
            showAlert(AlertType.ERROR, "Erro de Formato", "A carga horária e a série/semestre devem ser números válidos.");
        } catch (ValidacaoExcecoes e) {
            showAlert(AlertType.ERROR, "Erro de Validação", "Erro: " + e.getMessage());
        }
    }

    @FXML
    private void buscarEPreencher(ActionEvent event) {
        if (todasAsDisciplinas == null || todasAsDisciplinas.isEmpty()) {
            showAlert(AlertType.INFORMATION, "Aviso", "Não há disciplinas cadastradas para buscar.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/util/Busca.fxml"));
            VBox page = loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Buscar Disciplina");
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.setScene(new Scene(page));

            BuscaController<Disciplina> controller = loader.getController();
            controller.setItens(todasAsDisciplinas, Disciplina::getNomeDisciplina);

            dialogStage.showAndWait();

            Disciplina disciplinaSelecionada = controller.getItemSelecionado();
            if (disciplinaSelecionada != null) {
                this.disciplinaCarregada = disciplinaSelecionada;
                carregarDadosParaFormulario(disciplinaCarregada);
                showAlert(AlertType.INFORMATION, "Sucesso", "Disciplina encontrada e dados carregados.");
            }
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Erro", "Não foi possível abrir a janela de busca.");
        }
    }

    @FXML
    private void excluirDisciplina(ActionEvent event) {
        if (disciplinaCarregada == null) {
            showAlert(AlertType.WARNING, "Aviso", "Nenhuma disciplina carregada. Busque uma primeiro.");
            return;
        }

        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirmação");
        alert.setHeaderText(null);
        alert.setContentText("Tem certeza que deseja excluir a disciplina '" + disciplinaCarregada.getNomeDisciplina() + "'?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                disciplinaService.excluirDisciplina(disciplinaCarregada);
                if (todasAsDisciplinas != null) {
                    todasAsDisciplinas.remove(disciplinaCarregada);
                }
                showAlert(AlertType.INFORMATION, "Sucesso", "Disciplina excluída com sucesso.");
                limparCampos(null);
            } catch (ValidacaoExcecoes e) {
                showAlert(AlertType.ERROR, "Erro de Validação", "Erro: " + e.getMessage());
            }
        }
    }

    private void carregarDadosParaFormulario(Disciplina disciplina) {
        txtNomeDisciplina.setText(disciplina.getNomeDisciplina());
        txtCargaHoraria.setText(String.valueOf(disciplina.getCargaHoraria()));
        txtSerieSemestre.setText(String.valueOf(disciplina.getSerieSemestre()));
    }

    @FXML
    void limparCampos(ActionEvent event) {
        txtNomeDisciplina.clear();
        txtCargaHoraria.clear();
        txtSerieSemestre.clear();
        this.disciplinaCarregada = null;
    }

    private void showAlert(AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
