package gui.frequencia;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxListCell;
import modelo.Aluno;
import modelo.Disciplina;
import modelo.Turma;
import service.FrequenciaService;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class PainelFrequenciaController {
    private FrequenciaService frequenciaService;
    private List<Turma> todasAsTurmas;
    private List<Disciplina> todasAsDisciplinas;

    @FXML private ComboBox<Turma> cbTurmas;
    @FXML private ComboBox<Disciplina> cbDisciplinas;
    @FXML private DatePicker dpData;
    @FXML private ListView<Aluno> listAlunos;

    public void setFrequenciaService(FrequenciaService frequenciaService) { this.frequenciaService = frequenciaService; }
    public void setTodasAsTurmas(List<Turma> todasAsTurmas) {
        this.todasAsTurmas = todasAsTurmas;
        if(cbTurmas != null){
            cbTurmas.getItems().setAll(this.todasAsTurmas);
        }
    }
    public void setTodasAsDisciplinas(List<Disciplina> todasAsDisciplinas) {
        this.todasAsDisciplinas = todasAsDisciplinas;
        if(cbDisciplinas != null){
            cbDisciplinas.getItems().setAll(this.todasAsDisciplinas);
        }
    }

    @FXML
    public void initialize() {
        // Configura a ListView para usar CheckBoxes
        listAlunos.setCellFactory(CheckBoxListCell.forListView(Aluno::presenteProperty));

        dpData.setValue(LocalDate.now()); // Define a data atual como padrão
        limparCampos();
    }

    @FXML
    private void carregarAlunosDaTurma() {
        Turma turmaSelecionada = cbTurmas.getValue();
        if (turmaSelecionada != null) {
            listAlunos.getItems().setAll(turmaSelecionada.getAlunos());
        } else {
            listAlunos.getItems().clear();
        }
    }

    @FXML
    private void salvarFrequencia() {
        try {
            Turma turma = cbTurmas.getValue();
            Disciplina disciplina = cbDisciplinas.getValue();
            LocalDate data = dpData.getValue();
            List<Aluno> presentes = listAlunos.getItems().stream()
                    .filter(Aluno::isPresente)
                    .collect(Collectors.toList());

            frequenciaService.lancarFrequencia(turma, disciplina, data, presentes);
            showAlert(Alert.AlertType.INFORMATION, "Sucesso", "Frequência salva!");
            limparCampos();
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erro", e.getMessage());
        }
    }

    @FXML
    private void limparCampos() {
        cbTurmas.getSelectionModel().clearSelection();
        cbDisciplinas.getSelectionModel().clearSelection();
        dpData.setValue(LocalDate.now());
        listAlunos.getItems().clear();
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}