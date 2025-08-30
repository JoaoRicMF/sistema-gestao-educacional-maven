package gui.notas;

import excecoes.ValidacaoExcecoes;
import gui.util.Mask;
import gui.util.Validacoes;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import modelo.Aluno;
import modelo.Disciplina;
import service.NotaService;
import java.util.List;

public class PainelNotasController {

    private NotaService notaService;
    private List<Aluno> listaDeAlunos;
    private List<Disciplina> listaDeDisciplinas;

    @FXML private ComboBox<Aluno> cbAlunos;
    @FXML private ComboBox<Disciplina> cbDisciplinas;
    @FXML private TextField txtNota1;
    @FXML private TextField txtNota2;
    @FXML private TextField txtMediaFinal;

    public void setNotaService(NotaService notaService) {
        this.notaService = notaService;
    }

    public void setListaDeAlunos(List<Aluno> alunos) {
        this.listaDeAlunos = alunos;
        if (cbAlunos != null) {
            cbAlunos.getItems().setAll(this.listaDeAlunos);
        }
    }
    public void setListaDeDisciplinas(List<Disciplina> disciplinas) {
        this.listaDeDisciplinas = disciplinas;
        if (cbDisciplinas != null) {
            cbDisciplinas.getItems().setAll(this.listaDeDisciplinas);
        }
    }
    @FXML
    public void initialize() {
        Validacoes.addRequiredFieldValidator(txtNota1);
        Validacoes.addRequiredFieldValidator(txtNota2);
        Mask.addNotaMask(txtNota1);
        Mask.addNotaMask(txtNota2);
        configurarComboBoxes();
        limparCampos();
    }
    private void configurarComboBoxes(){
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
        cbDisciplinas.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Disciplina disciplina, boolean empty) {
                super.updateItem(disciplina, empty);
                setText(empty ? null : disciplina.getNomeDisciplina());
            }
        });
        cbDisciplinas.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Disciplina disciplina, boolean empty) {
                super.updateItem(disciplina, empty);
                setText(empty ? null : disciplina.getNomeDisciplina());
            }
        });
    }
    @FXML
    private void lancarNotas() {
        try {
            Aluno alunoSelecionado = cbAlunos.getValue();
            Disciplina disciplinaSelecionada = cbDisciplinas.getValue();
            double valorNota1 = Double.parseDouble(txtNota1.getText().replace(",", "."));
            double valorNota2 = Double.parseDouble(txtNota2.getText().replace(",", "."));
            double media = notaService.lancarNotasEObterMedia(alunoSelecionado, disciplinaSelecionada, valorNota1, valorNota2);
            String status = notaService.verificarStatusAprovacao(media);
            txtMediaFinal.setText(String.format("%.2f", media));
            showAlert(AlertType.INFORMATION, "Sucesso", "Notas lançadas para " + alunoSelecionado.getNome() + ".\nMédia: " + String.format("%.2f", media) + "\nStatus: " + status);
        } catch (NumberFormatException e) {
            showAlert(AlertType.ERROR, "Erro de Formato", "As notas devem ser números válidos.");
        } catch (ValidacaoExcecoes e) {
            showAlert(AlertType.ERROR, "Erro de Validação", "Erro: " + e.getMessage());
        } catch (Exception e) {
            showAlert(AlertType.ERROR, "Erro", "Ocorreu um erro inesperado: " + e.getMessage());
            e.printStackTrace();
        }
    }
    @FXML
    private void limparCampos() {
        cbAlunos.getSelectionModel().clearSelection();
        cbDisciplinas.getSelectionModel().clearSelection();
        txtNota1.clear();
        txtNota2.clear();
        txtMediaFinal.clear();
    }
    private void showAlert(AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}