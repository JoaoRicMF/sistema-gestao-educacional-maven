package gui.matricula;

import excecoes.ValidacaoExcecoes;
import gui.util.Validacoes;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import modelo.Aluno;
import modelo.Turma;
import service.MatriculaService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import javafx.scene.control.ListCell;

public class PainelMatriculaController {

    private MatriculaService matriculaService;
    private List<Aluno> listaDeAlunos;
    private List<Turma> listaDeTurmas;

    @FXML private ComboBox<Aluno> cbAlunos;
    @FXML private ComboBox<Turma> cbTurmas;
    @FXML private TextField txtDataMatricula;

    public void setMatriculaService(MatriculaService matriculaService) {
        this.matriculaService = matriculaService;
    }

    public void setListaDeAlunos(List<Aluno> alunos) {
        this.listaDeAlunos = alunos;
        if (cbAlunos != null) {
            cbAlunos.getItems().setAll(listaDeAlunos);
        }
    }

    public void setListaDeTurmas(List<Turma> turmas) {
        this.listaDeTurmas = turmas;
        if (cbTurmas != null) {
            cbTurmas.getItems().setAll(listaDeTurmas);
        }
    }

    @FXML
    public void initialize() {
        Validacoes.addRequiredFieldValidator(txtDataMatricula);

        cbAlunos.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Aluno aluno, boolean empty) {
                super.updateItem(aluno, empty);
                setText(empty ? null : aluno.getNome());
            }
        });
        cbAlunos.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Aluno aluno, boolean empty) {
                super.updateItem(aluno, empty);
                setText(empty ? null : aluno.getNome());
            }
        });

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
        limparCampos();
    }

    @FXML
    private void realizarMatricula() {
        try {
            Aluno alunoSelecionado = cbAlunos.getValue();
            Turma turmaSelecionada = cbTurmas.getValue();
            String dataMatriculaStr = txtDataMatricula.getText();

            if (alunoSelecionado == null || turmaSelecionada == null || dataMatriculaStr.isEmpty()) {
                throw new ValidacaoExcecoes("Por favor, selecione um aluno, uma turma e preencha a data.");
            }

            LocalDate.parse(dataMatriculaStr, DateTimeFormatter.ofPattern("dd/MM/yyyy"));

            if (matriculaService != null) {
                matriculaService.matricularAluno(alunoSelecionado, turmaSelecionada);
            }

            showAlert(Alert.AlertType.INFORMATION, "Sucesso", "Aluno '" + alunoSelecionado.getNome() + "' matriculado na turma '" + turmaSelecionada.getNomeTurma() + "' com sucesso!");
            limparCampos();

        } catch (ValidacaoExcecoes e) {
            showAlert(Alert.AlertType.ERROR, "Erro de Validação", "Erro ao matricular: " + e.getMessage());
        } catch (DateTimeParseException e) {
            showAlert(Alert.AlertType.ERROR, "Erro de Formato", "A data da matrícula deve estar no formato dd/MM/yyyy.");
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erro", "Ocorreu um erro inesperado: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void limparCampos() {
        cbAlunos.getSelectionModel().clearSelection();
        cbTurmas.getSelectionModel().clearSelection();
        txtDataMatricula.clear();
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}