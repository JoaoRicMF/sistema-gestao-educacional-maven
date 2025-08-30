package gui.turma;

import excecoes.ValidacaoExcecoes;
import gui.util.Validacoes;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import modelo.*;
import service.TurmaService;
import dao.TurmaDAO;
import java.util.List;
import gui.util.BuscaController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;

public class PainelTurmaController {

    private TurmaService turmaService;
    private TurmaDAO turmaDAO;
    private List<Professor> listaDeProfessores;
    private List<Disciplina> todasAsDisciplinas;
    private Turma turmaCarregada;
    private List<Turma> todasAsTurmas;

    @FXML private TextField txtNomeTurma, txtSerieAno;
    @FXML private ComboBox<Turno> cbTurno;
    @FXML private ComboBox<Professor> cbProfessor;
    @FXML private ComboBox<Disciplina> cbDisciplinasDisponiveis;
    @FXML private ListView<Aluno> listAlunosMatriculados;
    @FXML private Button btnAdicionarDisciplina, btnRemoverAluno,btnBuscar;

    public void setTurmaService(TurmaService turmaService) { this.turmaService = turmaService; }
    public void setListaDeProfessores(List<Professor> listaDeProfessores) {
        this.listaDeProfessores = listaDeProfessores;
        if (cbProfessor != null) {
            cbProfessor.getItems().setAll(this.listaDeProfessores);
        }
    }
    public void setTodasAsDisciplinas(List<Disciplina> todasAsDisciplinas) {
        this.todasAsDisciplinas = todasAsDisciplinas;
        if (cbDisciplinasDisponiveis != null) {
            cbDisciplinasDisponiveis.getItems().setAll(this.todasAsDisciplinas);
        }
    }
    public void setTodasAsTurmas(List<Turma> turmas) {
        this.todasAsTurmas = turmas;
    }

    @FXML
    public void initialize() {
        if (turmaService == null) turmaService = new TurmaService();
        if (turmaDAO == null) turmaDAO = new TurmaDAO();
        Validacoes.addRequiredFieldValidator(txtNomeTurma);
        Validacoes.addRequiredFieldValidator(txtSerieAno);

        cbTurno.getItems().setAll(Turno.values());

        configurarComboBoxes();

        listAlunosMatriculados.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Aluno aluno, boolean empty) {
                super.updateItem(aluno, empty);
                setText(empty ? null : aluno.getNome());
            }
        });

        listAlunosMatriculados.setPlaceholder(new Label("Não há alunos matriculados nesta turma."));

        limparCampos();
    }

    private void configurarComboBoxes() {
        cbProfessor.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Professor professor, boolean empty) {
                super.updateItem(professor, empty);
                setText(empty ? null : professor.getNome());
            }
        });
        cbProfessor.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Professor professor, boolean empty) {
                super.updateItem(professor, empty);
                setText(empty ? null : professor.getNome());
            }
        });

        cbDisciplinasDisponiveis.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Disciplina disciplina, boolean empty) {
                super.updateItem(disciplina, empty);
                setText(empty ? null : disciplina.getNomeDisciplina());
            }
        });
        cbDisciplinasDisponiveis.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Disciplina disciplina, boolean empty) {
                super.updateItem(disciplina, empty);
                setText(empty ? null : disciplina.getNomeDisciplina());
            }
        });
    }

    @FXML
    private void salvarOuAtualizar(ActionEvent event) {
        try {
            String nomeTurma = txtNomeTurma.getText();
            int serieAno = Integer.parseInt(txtSerieAno.getText());
            Turno turno = cbTurno.getValue();
            Professor professorSelecionado = cbProfessor.getValue();

            if (turmaCarregada != null) {
                turmaCarregada.setNomeTurma(nomeTurma);
                turmaCarregada.setSemestre(serieAno);
                turmaCarregada.setTurno(turno);
                turmaCarregada.setProfessor(professorSelecionado);
                turmaService.atualizarTurma(turmaCarregada);
                showAlert(AlertType.INFORMATION, "Sucesso", "Turma '" + nomeTurma + "' atualizada com sucesso!");
            } else {
                Turma novaTurma = turmaService.cadastrarNovaTurma(nomeTurma, serieAno, turno, professorSelecionado);
                if (todasAsTurmas != null) {
                    todasAsTurmas.add(novaTurma);
                }
                showAlert(AlertType.INFORMATION, "Sucesso", "Turma '" + nomeTurma + "' cadastrada com sucesso!");
            }
            limparCampos();

        } catch (NumberFormatException e) {
            showAlert(AlertType.ERROR, "Erro de Formato", "O campo 'Série/Ano' deve ser um número válido.");
        } catch (ValidacaoExcecoes e) {
            showAlert(AlertType.ERROR, "Erro de Validação", e.getMessage());
        } catch (Exception e) {
            showAlert(AlertType.ERROR, "Erro", "Ocorreu um erro: " + e.getMessage());
        }
    }
    @FXML
    private void buscarTurma() {
        if (todasAsTurmas == null || todasAsTurmas.isEmpty()) {
            showAlert(AlertType.INFORMATION, "Aviso", "Não há turmas cadastradas para buscar.");
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/util/Busca.fxml"));
            VBox page = loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Buscar Turma");
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);
            BuscaController<Turma> controller = loader.getController();
            controller.setItens(todasAsTurmas, Turma::getNomeTurma);
            dialogStage.showAndWait();
            Turma turmaSelecionada = controller.getItemSelecionado();
            if (turmaSelecionada != null) {
                this.turmaCarregada = turmaSelecionada;
                carregarDadosParaFormulario(turmaCarregada);
                showAlert(AlertType.INFORMATION, "Sucesso", "Turma encontrada e dados carregados.");
            }
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Erro", "Não foi possível abrir a janela de busca.");
        }
    }
    @FXML
    private void adicionarDisciplinaNaTurma() {
        if (turmaCarregada == null) {
            showAlert(AlertType.WARNING, "Aviso", "Nenhuma turma carregada. Busque e carregue uma turma primeiro.");
            return;
        }
        Disciplina disciplinaSelecionada = cbDisciplinasDisponiveis.getValue();
        if (disciplinaSelecionada == null) {
            showAlert(AlertType.WARNING, "Aviso", "Selecione uma disciplina para adicionar.");
            return;
        }

        if (!turmaCarregada.getDisciplinas().contains(disciplinaSelecionada)) {
            turmaCarregada.adicionarDisciplina(disciplinaSelecionada);
            turmaDAO.adicionarDisciplinaNaTurma(turmaCarregada.getId(), disciplinaSelecionada.getId());
            atualizarListasDaTurma();
            showAlert(AlertType.INFORMATION, "Sucesso", "Disciplina adicionada à grade da turma.");
        } else {
            showAlert(AlertType.INFORMATION, "Aviso", "Esta disciplina já está na grade desta turma.");
        }
    }
    @FXML
    private void removerAlunoDaTurma() {
        if (turmaCarregada == null) {
            showAlert(AlertType.WARNING, "Aviso", "Nenhuma turma carregada.");
            return;
        }
        Aluno alunoSelecionado = listAlunosMatriculados.getSelectionModel().getSelectedItem();
        if (alunoSelecionado == null) {
            showAlert(AlertType.WARNING, "Aviso", "Selecione um aluno para remover.");
            return;
        }

        turmaCarregada.removerAluno(alunoSelecionado);
        turmaDAO.removerAlunoDaTurma(turmaCarregada.getId(), alunoSelecionado.getMatricula());
        atualizarListasDaTurma();
        showAlert(AlertType.INFORMATION, "Sucesso", "Aluno removido da turma.");
    }
    private void carregarDadosParaFormulario(Turma turma) {
        txtNomeTurma.setText(turma.getNomeTurma());
        txtSerieAno.setText(String.valueOf(turma.getSemestre()));
        cbTurno.setValue(turma.getTurno());
        cbProfessor.setValue(turma.getProfessor());
        atualizarListasDaTurma();
    }
    private void atualizarListasDaTurma() {
        if (turmaCarregada != null) {
            listAlunosMatriculados.getItems().setAll(turmaCarregada.getAlunos());
        } else {
            listAlunosMatriculados.getItems().clear();
        }
    }
    @FXML
    private void limparCampos(ActionEvent event) {
        limparCampos();
    }
    private void limparCampos() {
        txtNomeTurma.clear();
        txtSerieAno.clear();
        cbTurno.getSelectionModel().clearSelection();
        cbProfessor.getSelectionModel().clearSelection();
        cbDisciplinasDisponiveis.getSelectionModel().clearSelection();
        this.turmaCarregada = null;
        atualizarListasDaTurma();
    }
    private void showAlert(AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}