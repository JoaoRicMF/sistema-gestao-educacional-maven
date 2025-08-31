package gui.professor;

import excecoes.ValidacaoExcecoes;
import gui.util.BuscaController;
import gui.util.Mask;
import gui.util.Validacoes;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import modelo.*;
import service.ProfessorService;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author Josemar
 */

public class PainelProfessorController {

    private ProfessorService professorService;
    private List<Disciplina> todasAsDisciplinas;
    private Professor professorCarregado;
    private List<Professor> todosOsProfessores;

    @FXML private TextField txtNome, txtCPF, txtDataNascimento, txtMatricula, txtRG, txtTelefone, txtEmail;
    @FXML private ListView<Disciplina> listDisciplinas;
    @FXML private Button btnSalvar, btnCancelar, btnBuscarProfessor, btnExcluirProfessor;
    @FXML private TextField txtRua, txtNumero, txtComplemento, txtBairro, txtCidade, txtCEP;
    @FXML private ComboBox<UF> cbUF;
    @FXML private RadioButton rbMasculino, rbFeminino, rbOutro, rbNaoInformar;
    @FXML private ToggleGroup bgGenero;


    public void setProfessorService(ProfessorService professorService) {
        this.professorService = professorService;
    }
    public void setTodasAsDisciplinas(List<Disciplina> todasAsDisciplinas) {
        this.todasAsDisciplinas = todasAsDisciplinas;
    }
    public void setTodosOsProfessores(List<Professor> todosOsProfessores) {
        this.todosOsProfessores = todosOsProfessores;
    }

    @FXML
    public void initialize() {
        if (professorService == null) professorService = new ProfessorService();
        cbUF.getItems().setAll(UF.values());

        Mask.addCPFMask(txtCPF);
        Mask.addRGMask(txtRG);
        Mask.addDateMask(txtDataNascimento);
        Mask.addPhoneMask(txtTelefone);
        Mask.addCEPMask(txtCEP);
        Mask.addMatriculaMask(txtMatricula);

        Validacoes.addRequiredFieldValidator(txtNome);
        Validacoes.addRequiredFieldValidator(txtCPF);
        Validacoes.addRequiredFieldValidator(txtRG);
        Validacoes.addRequiredFieldValidator(txtMatricula);
        Validacoes.addRequiredFieldValidator(txtDataNascimento);
        Validacoes.addRequiredFieldValidator(txtTelefone);
        Validacoes.addRequiredFieldValidator(txtEmail);
        Validacoes.addRequiredFieldValidator(txtRua);
        Validacoes.addRequiredFieldValidator(txtNumero);
        Validacoes.addRequiredFieldValidator(txtBairro);
        Validacoes.addRequiredFieldValidator(txtCidade);
        Validacoes.addRequiredFieldValidator(txtCEP);
        atualizarListaDisciplinasGUI();
        limparCampos();
    }

    @FXML
    private void salvarOuAtualizar() {
        try {
            String nome = txtNome.getText();
            String cpf = txtCPF.getText();
            String dataNascimento = txtDataNascimento.getText();
            String matricula = txtMatricula.getText();
            String rg = txtRG.getText();
            String telefone = txtTelefone.getText();
            String email = txtEmail.getText();
            List<Disciplina> disciplinasSelecionadas = listDisciplinas.getSelectionModel().getSelectedItems();
            Genero genero = getGeneroSelecionado();

            String rua = txtRua.getText();
            String numero = txtNumero.getText();
            String complemento = txtComplemento.getText();
            String bairro = txtBairro.getText();
            String cidade = txtCidade.getText();
            String cep = txtCEP.getText();
            UF uf = cbUF.getValue();
            Endereco endereco = new Endereco(rua, numero, complemento, bairro, cidade, uf, cep);

            if (professorCarregado != null) {
                professorCarregado.setNome(nome);
                professorCarregado.setCpf(cpf);
                professorCarregado.setDataNascimento(dataNascimento);
                professorCarregado.setRG(rg);
                professorCarregado.setMatricula(matricula);
                professorCarregado.setGenero(genero);
                professorCarregado.setTelefone(telefone);
                professorCarregado.setEmail(email);
                professorCarregado.setEndereco(endereco);
                professorCarregado.setDisciplinas(disciplinasSelecionadas);
                professorService.atualizarDadosProfessor(professorCarregado);
                showAlert(AlertType.INFORMATION, "Sucesso", "Dados do professor '" + professorCarregado.getNome() + "' atualizados com sucesso! ✅");
            } else {
                Professor novoProfessor = new Professor(nome, cpf, rg, genero, dataNascimento, telefone, email, endereco, matricula);
                novoProfessor.setDisciplinas(disciplinasSelecionadas);
                professorService.cadastrarNovoProfessor(novoProfessor);
                todosOsProfessores.add(novoProfessor);
                showAlert(AlertType.INFORMATION, "Sucesso", "Professor '" + nome + "' cadastrado com sucesso! ✅");
            }
            limparCampos();
        } catch (ValidacaoExcecoes e) {
            showAlert(AlertType.ERROR, "Erro de Validação", "Erro: " + e.getMessage());
        }
    }

    private Genero getGeneroSelecionado() {
        if (rbMasculino.isSelected()) return Genero.MASCULINO;
        if (rbFeminino.isSelected()) return Genero.FEMININO;
        if (rbOutro.isSelected()) return Genero.OUTRO;
        if (rbNaoInformar.isSelected()) return Genero.NAO_INFORMAR;
        return null;
    }

    private void carregarDadosParaFormulario(Professor prof) {
        txtNome.setText(prof.getNome());
        txtCPF.setText(prof.getCpf());
        txtDataNascimento.setText(prof.getDataNascimento());
        txtMatricula.setText(prof.getMatricula());
        txtRG.setText(prof.getRG());
        txtTelefone.setText(prof.getTelefone());
        txtEmail.setText(prof.getEmail());
        if (prof.getGenero() != null) {
            switch (prof.getGenero()) {
                case MASCULINO: rbMasculino.setSelected(true); break;
                case FEMININO: rbFeminino.setSelected(true); break;
                case OUTRO: rbOutro.setSelected(true); break;
                case NAO_INFORMAR: rbNaoInformar.setSelected(true); break;
            }
        } else {
            if (bgGenero.getSelectedToggle() != null) {
                bgGenero.getSelectedToggle().setSelected(false);
            }
        }
        Endereco end = prof.getEndereco();
        if (end != null) {
            txtRua.setText(end.getRua());
            txtNumero.setText(end.getNumero());
            txtComplemento.setText(end.getComplemento());
            txtBairro.setText(end.getBairro());
            txtCidade.setText(end.getCidade());
            txtCEP.setText(end.getCep());
            cbUF.setValue(end.getUf());
        }

        listDisciplinas.getSelectionModel().clearSelection();
        if (prof.getDisciplinas() != null && !prof.getDisciplinas().isEmpty()) {
            listDisciplinas.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
            for (Disciplina d : prof.getDisciplinas()) {
                listDisciplinas.getSelectionModel().select(d);
            }
        }
    }

    @FXML
    private void limparCampos() {
        txtNome.clear();
        txtCPF.clear();
        txtDataNascimento.clear();
        txtMatricula.clear();
        txtRG.clear();
        txtTelefone.clear();
        txtEmail.clear();
        if (bgGenero.getSelectedToggle() != null) {
            bgGenero.getSelectedToggle().setSelected(false);
        }
        txtRua.clear();
        txtNumero.clear();
        txtComplemento.clear();
        txtBairro.clear();
        txtCidade.clear();
        txtCEP.clear();
        cbUF.getSelectionModel().clearSelection();
        listDisciplinas.getSelectionModel().clearSelection();
        this.professorCarregado = null;
    }

    @FXML
    private void buscarEPreencher() {
        if (todosOsProfessores == null || todosOsProfessores.isEmpty()) {
            showAlert(AlertType.INFORMATION, "Aviso", "Não há professores cadastrados para buscar.");
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/util/Busca.fxml"));
            VBox page = (VBox) loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Buscar Professor");
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.setScene(new Scene(page));
            BuscaController<Professor> controller = loader.getController();
            controller.setItens(todosOsProfessores, prof -> prof.getNome() + " - Matrícula: " + prof.getMatricula());
            dialogStage.showAndWait();
            Professor professorSelecionado = controller.getItemSelecionado();
            if (professorSelecionado != null) {
                this.professorCarregado = professorSelecionado;
                carregarDadosParaFormulario(professorCarregado);
                showAlert(AlertType.INFORMATION, "Sucesso", "Professor encontrado e dados carregados.");
            }
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Erro", "Não foi possível abrir a janela de busca.");
        }
    }

    @FXML
    private void excluir() {
        if (professorCarregado == null) {
            showAlert(AlertType.WARNING, "Aviso", "Nenhum professor carregado. Busque um primeiro.");
            return;
        }
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirmação");
        alert.setHeaderText(null);
        alert.setContentText("Tem certeza que deseja excluir o professor '" + professorCarregado.getNome() + "'?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                professorService.excluirProfessor(professorCarregado);
                todosOsProfessores.remove(professorCarregado);
                showAlert(AlertType.INFORMATION, "Sucesso", "Professor excluído com sucesso.");
                limparCampos();
            } catch (ValidacaoExcecoes e) {
                showAlert(AlertType.ERROR, "Erro de Validação", "Erro: " + e.getMessage());
            }
        }
    }

    public void atualizarListaDisciplinasGUI() {
        if (todasAsDisciplinas != null) {
            listDisciplinas.getItems().setAll(todasAsDisciplinas);
            listDisciplinas.setCellFactory(lv -> new ListCell<Disciplina>() {
                @Override
                protected void updateItem(Disciplina disciplina, boolean empty) {
                    super.updateItem(disciplina, empty);
                    setText(empty ? null : disciplina.getNomeDisciplina());
                }
            });
            listDisciplinas.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        }
    }

    private void showAlert(AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}