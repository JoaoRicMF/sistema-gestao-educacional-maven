package gui.aluno;

import excecoes.ValidacaoExcecoes;
import gui.util.Mask;
import gui.util.Validacoes;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.VBox;
import modelo.*;
import service.AlunoService;
import gui.util.BuscaController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.List;

public class PainelAlunoController {

    private final AlunoService alunoService = new AlunoService();
    private Aluno alunoCarregado;

    @FXML private TextField txtNome, txtDataNascimento, txtCPF, txtRG, txtMatricula, txtSemestre, txtTelefone, txtEmail, txtCurso;
    @FXML private RadioButton rbMasculino, rbFeminino, rbOutro, rbNaoInformar;
    @FXML private ToggleGroup bgGenero;
    @FXML private ComboBox<TipoCurso> cbTipoCurso;
    @FXML private ComboBox<NivelAcademico> cbNivelAcademico;
    @FXML private ComboBox<StatusAluno> cbStatusAluno;
    @FXML private TextField txtRua, txtNumero, txtComplemento, txtBairro, txtCidade, txtCEP;
    @FXML private ComboBox<UF> cbUF;
    @FXML private Button btnSalvar, btnCancelar, btnBuscar, btnDesativar, btnTrancar, btnAvancarSemestre;
    private List<Aluno> todosOsAlunos;

    public void setTodosOsAlunos(List<Aluno> todosOsAlunos) {
        this.todosOsAlunos = todosOsAlunos;
    }

    @FXML
    public void initialize() {
        cbTipoCurso.getItems().setAll(TipoCurso.values());
        cbNivelAcademico.getItems().setAll(NivelAcademico.values());
        cbStatusAluno.getItems().setAll(StatusAluno.values());
        cbUF.getItems().setAll(UF.values());

        // máscaras
        Mask.addCPFMask(txtCPF);
        Mask.addRGMask(txtRG);
        Mask.addDateMask(txtDataNascimento);
        Mask.addPhoneMask(txtTelefone);
        Mask.addCEPMask(txtCEP);
        Mask.addMatriculaMask(txtMatricula);
        Mask.addSemestreMask(txtSemestre);

        Validacoes.addRequiredFieldValidator(txtNome);
        Validacoes.addRequiredFieldValidator(txtCPF);
        Validacoes.addRequiredFieldValidator(txtRG);
        Validacoes.addRequiredFieldValidator(txtMatricula);
        Validacoes.addRequiredFieldValidator(txtDataNascimento);
        Validacoes.addRequiredFieldValidator(txtTelefone);
        Validacoes.addRequiredFieldValidator(txtEmail);
        Validacoes.addRequiredFieldValidator(txtCurso);
        Validacoes.addRequiredFieldValidator(txtRua);
        Validacoes.addRequiredFieldValidator(txtNumero);
        Validacoes.addRequiredFieldValidator(txtBairro);
        Validacoes.addRequiredFieldValidator(txtCidade);
        Validacoes.addRequiredFieldValidator(txtCEP);

        limparCampos();
    }

    @FXML
    private void salvarOuAtualizar() {
        try {
            // Coleta dos dados
            String nome = txtNome.getText();
            String dataNascimento = txtDataNascimento.getText();
            String cpf = txtCPF.getText();
            String rg = txtRG.getText();
            String matricula = txtMatricula.getText();
            String semestreStr = txtSemestre.getText();
            String telefone = txtTelefone.getText();
            String email = txtEmail.getText();
            String curso = txtCurso.getText();
            TipoCurso tipoCurso = cbTipoCurso.getValue();
            NivelAcademico nivelAcademico = cbNivelAcademico.getValue();
            StatusAluno statusAluno = cbStatusAluno.getValue();
            Genero genero = getGeneroSelecionado();

            String rua = txtRua.getText();
            String numero = txtNumero.getText();
            String complemento = txtComplemento.getText();
            String bairro = txtBairro.getText();
            String cidade = txtCidade.getText();
            String cep = txtCEP.getText();
            UF uf = cbUF.getValue();

            Endereco endereco = new Endereco(rua, numero, complemento, bairro, cidade, uf, cep);

            if (alunoCarregado != null) {
                // Atualiza o objeto 'alunoCarregado' com os novos dados do formulário
                alunoCarregado.setNome(nome);
                alunoCarregado.setCpf(cpf);
                alunoCarregado.setRG(rg);
                alunoCarregado.setGenero(genero);
                alunoCarregado.setDataNascimento(dataNascimento);
                alunoCarregado.setTelefone(telefone);
                alunoCarregado.setEmail(email);
                alunoCarregado.setEndereco(endereco);

                alunoCarregado.setMatricula(matricula);
                alunoCarregado.setCurso(curso);
                alunoCarregado.setTipoCurso(tipoCurso);
                alunoCarregado.setSemestre(Integer.parseInt(semestreStr));
                alunoCarregado.setNivelAcademico(nivelAcademico);
                alunoCarregado.setStatusAluno(statusAluno);

                alunoService.atualizarDadosAluno(alunoCarregado, telefone, email, endereco);
                showAlert(AlertType.INFORMATION, "Sucesso", "Dados do aluno '" + alunoCarregado.getNome() + "' atualizados com sucesso! ✅");
            } else {
                Aluno novoAluno = new Aluno(nome, cpf, rg, genero, dataNascimento, telefone, email, endereco,
                        matricula, curso, tipoCurso, Integer.parseInt(semestreStr),
                        nivelAcademico, statusAluno, 0, 0, 0);
                alunoService.cadastrarNovoAluno(novoAluno);
                if (todosOsAlunos != null) {
                    todosOsAlunos.add(novoAluno);
                }
                showAlert(AlertType.INFORMATION, "Sucesso", "Aluno '" + nome + "' cadastrado com sucesso! ✅");
            }
            limparCampos();
        } catch (ValidacaoExcecoes e) {
            showAlert(AlertType.ERROR, "Erro de Validação", "Erro de Validação:\n" + e.getMessage());
        } catch (NumberFormatException e) {
            showAlert(AlertType.ERROR, "Erro de Formato", "O campo 'Série / Semestre' deve conter um número válido.");
        } catch (Exception e) {
            showAlert(AlertType.ERROR, "Erro", "Ocorreu um erro inesperado: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void buscarEPreencher() {
        if (todosOsAlunos == null || todosOsAlunos.isEmpty()) {
            showAlert(AlertType.INFORMATION, "Aviso", "Não há alunos cadastrados para buscar.");
            return;
        }

        try {
            // Carrega o FXML da janela de busca
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/util/Busca.fxml"));
            VBox page = loader.load();

            // Cria um novo Stage (janela) para o diálogo
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Buscar Aluno");
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            // Passa a lista de alunos para o controller da busca
            BuscaController<Aluno> controller = loader.getController();
            controller.setItens(todosOsAlunos, aluno -> aluno.getNome() + " - Matrícula: " + aluno.getMatricula());

            // Mostra o diálogo e espera ele ser fechado
            dialogStage.showAndWait();

            // Pega o resultado
            Aluno alunoSelecionado = controller.getItemSelecionado();
            if (alunoSelecionado != null) {
                this.alunoCarregado = alunoSelecionado;
                carregarDadosParaFormulario(alunoCarregado);
                showAlert(AlertType.INFORMATION, "Sucesso", "Aluno encontrado e dados carregados.");
            }

        } catch (IOException e) {
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Erro", "Não foi possível abrir a janela de busca.");
        }
    }

    @FXML
    private void desativar() {
        if (alunoCarregado == null) {
            showAlert(AlertType.WARNING, "Aviso", "Nenhum aluno carregado. Busque um aluno primeiro.");
            return;
        }
        try {
            alunoService.desativarAluno(alunoCarregado);
            cbStatusAluno.setValue(StatusAluno.INATIVO);
            showAlert(AlertType.INFORMATION, "Sucesso", "Aluno '" + alunoCarregado.getNome() + "' desativado com sucesso.");
        } catch (ValidacaoExcecoes e) {
            showAlert(AlertType.ERROR, "Erro de Validação", "Erro: " + e.getMessage());
        }
    }

    @FXML
    private void trancarMatricula() {
        if (alunoCarregado == null) {
            showAlert(AlertType.WARNING, "Aviso", "Nenhum aluno carregado. Busque um aluno primeiro.");
            return;
        }
        try {
            alunoService.trancarMatricula(alunoCarregado);
            cbStatusAluno.setValue(StatusAluno.TRANCADO);
            showAlert(AlertType.INFORMATION, "Sucesso", "Matrícula do aluno '" + alunoCarregado.getNome() + "' trancada com sucesso.");
        } catch (ValidacaoExcecoes e) {
            showAlert(AlertType.ERROR, "Erro de Validação", "Erro: " + e.getMessage());
        }
    }

    @FXML
    private void avancarSemestre() {
        if (alunoCarregado == null) {
            showAlert(AlertType.WARNING, "Aviso", "Nenhum aluno carregado. Busque um aluno primeiro.");
            return;
        }
        try {
            alunoService.passarAlunoParaProximoSemestre(alunoCarregado);
            txtSemestre.setText(String.valueOf(alunoCarregado.getSemestre()));
            showAlert(AlertType.INFORMATION, "Sucesso", "Aluno '" + alunoCarregado.getNome() + "' avançou para o semestre " + alunoCarregado.getSemestre() + ".");
        } catch (ValidacaoExcecoes e) {
            showAlert(AlertType.ERROR, "Erro de Validação", "Erro: " + e.getMessage());
        }
    }

    @FXML
    private void limparCampos() {
        txtNome.clear();
        txtDataNascimento.clear();
        txtCPF.clear();
        txtRG.clear();
        txtMatricula.clear();
        txtSemestre.clear();
        txtTelefone.clear();
        txtEmail.clear();
        txtCurso.clear();
        txtRua.clear();
        txtNumero.clear();
        txtComplemento.clear();
        txtBairro.clear();
        txtCidade.clear();
        txtCEP.clear();

        if (bgGenero.getSelectedToggle() != null) {
            bgGenero.getSelectedToggle().setSelected(false);
        }

        cbUF.getSelectionModel().selectFirst();
        cbTipoCurso.getSelectionModel().selectFirst();
        cbNivelAcademico.getSelectionModel().selectFirst();
        cbStatusAluno.getSelectionModel().selectFirst();

        this.alunoCarregado = null;
    }

    private void carregarDadosParaFormulario(Aluno aluno) {
        txtNome.setText(aluno.getNome());
        txtDataNascimento.setText(aluno.getDataNascimento());
        txtCPF.setText(aluno.getCpf());
        txtRG.setText(aluno.getRG());
        txtMatricula.setText(aluno.getMatricula());
        txtSemestre.setText(String.valueOf(aluno.getSemestre()));
        txtTelefone.setText(aluno.getTelefone());
        txtEmail.setText(aluno.getEmail());
        txtCurso.setText(aluno.getCurso());

        if (aluno.getGenero() != null) {
            switch (aluno.getGenero()) {
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

        cbTipoCurso.setValue(aluno.getTipoCurso());
        cbNivelAcademico.setValue(aluno.getNivelAcademico());
        cbStatusAluno.setValue(aluno.getStatusAluno());

        Endereco end = aluno.getEndereco();
        if (end != null) {
            txtRua.setText(end.getRua());
            txtNumero.setText(end.getNumero());
            txtComplemento.setText(end.getComplemento());
            txtBairro.setText(end.getBairro());
            txtCidade.setText(end.getCidade());
            txtCEP.setText(end.getCep());
            cbUF.setValue(end.getUf());
        }
    }

    private Genero getGeneroSelecionado() {
        if (rbMasculino.isSelected()) return Genero.MASCULINO;
        if (rbFeminino.isSelected()) return Genero.FEMININO;
        if (rbOutro.isSelected()) return Genero.OUTRO;
        if (rbNaoInformar.isSelected()) return Genero.NAO_INFORMAR;
        return null;
    }

    private void showAlert(AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}