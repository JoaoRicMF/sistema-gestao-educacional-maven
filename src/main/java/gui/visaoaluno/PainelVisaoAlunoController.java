package gui.visaoaluno;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import modelo.*;
import service.FrequenciaService;
import service.MuralService;
import service.NotaService;

import java.io.IOException;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class PainelVisaoAlunoController {

    @FXML private Label lblBoasVindas;
    @FXML private ListView<BoletimDisciplina> listNotas;
    @FXML private ListView<Turma> listTurmas;
    @FXML private ListView<Mural> listMural;
    @FXML private TextField txtNome, txtMatricula, txtCurso, txtEmail;

    private Aluno alunoLogado;
    private MuralService muralService;
    private List<Turma> todasAsTurmas;
    private NotaService notaService;
    private FrequenciaService frequenciaService;

    public void setAluno(Aluno aluno, List<Turma> todasAsTurmas) {
        this.alunoLogado = aluno;
        this.todasAsTurmas = todasAsTurmas;
    }
    public void setMuralService(MuralService muralService) {
        this.muralService = muralService;
    }
    public void setNotaService(NotaService notaService) {
        this.notaService = notaService;
    }
    public void setFrequenciaService(FrequenciaService frequenciaService) {
        this.frequenciaService = frequenciaService;
    }

    public void iniciarControlador() {
        atualizarDados();
    }

    @FXML
    public void initialize() {
        configurarListViews();
        listTurmas.setPlaceholder(new Label("A carregar..."));
        listMural.setPlaceholder(new Label("A carregar..."));
        listNotas.setPlaceholder(new Label("A carregar..."));

        listTurmas.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                Turma turmaSelecionada = listTurmas.getSelectionModel().getSelectedItem();
                if (turmaSelecionada != null) abrirDetalhesTurma(turmaSelecionada);
            }
        });

        listNotas.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                BoletimDisciplina boletim = listNotas.getSelectionModel().getSelectedItem();
                if (boletim != null) abrirDetalhesDisciplina(boletim);
            }
        });

        listMural.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                Mural aviso = listMural.getSelectionModel().getSelectedItem();
                if (aviso != null) abrirDetalhesAviso(aviso);
            }
        });
    }

    private void atualizarDados() {
        if (alunoLogado == null) return;

        lblBoasVindas.setText("Bem-vindo(a), " + alunoLogado.getNome() + "!");
        txtNome.setText(alunoLogado.getNome());
        txtMatricula.setText(alunoLogado.getMatricula());
        txtCurso.setText(alunoLogado.getCurso());
        txtEmail.setText(alunoLogado.getEmail());

        Optional<Turma> turmaDoAlunoOpt = todasAsTurmas.stream()
                .filter(turma -> turma.getAlunos().stream()
                        .anyMatch(alunoNaTurma -> alunoNaTurma.getMatricula().equals(alunoLogado.getMatricula())))
                .findFirst();

        if (turmaDoAlunoOpt.isPresent()) {
            Turma turmaDoAluno = turmaDoAlunoOpt.get();
            listTurmas.getItems().setAll(turmaDoAluno);
            listMural.getItems().setAll(muralService.verPostagensDaTurma(turmaDoAluno));

            if (turmaDoAluno.getDisciplinas().isEmpty()) {
                listNotas.setPlaceholder(new Label("A sua turma não tem disciplinas cadastradas."));
                listNotas.getItems().clear();
            } else {
                List<BoletimDisciplina> boletimCompleto = new ArrayList<>();
                List<Nota> todasAsNotasDoAluno = notaService.buscarNotasDoAluno(alunoLogado);

                for (Disciplina disciplina : turmaDoAluno.getDisciplinas()) {
                    List<Nota> notasDaDisciplina = todasAsNotasDoAluno.stream()
                            .filter(n -> n.getDisciplina().getId() == disciplina.getId())
                            .collect(Collectors.toList());

                    double n1 = notasDaDisciplina.stream().findFirst().map(Nota::getValor).orElse(0.0);
                    double n2 = notasDaDisciplina.stream().skip(1).findFirst().map(Nota::getValor).orElse(0.0);
                    double media = notasDaDisciplina.isEmpty() ? 0.0 : notasDaDisciplina.stream().mapToDouble(Nota::getValor).average().orElse(0.0);

                    int[] dadosFrequencia = frequenciaService.consultarFaltasEPresencas(alunoLogado, disciplina);
                    int totalAulas = dadosFrequencia[0];
                    int presencas = dadosFrequencia[1];
                    int faltas = totalAulas - presencas;

                    String status = notaService.verificarStatusAprovacao(media);
                    BoletimDisciplina boletimDisciplina = new BoletimDisciplina(disciplina, n1, n2, media, totalAulas, faltas, status);
                    boletimCompleto.add(boletimDisciplina);
                }
                listNotas.getItems().setAll(boletimCompleto);
            }
        } else {
            // Se o aluno não for encontrado em nenhuma turma
            listTurmas.setPlaceholder(new Label("Você não está matriculado(a) em nenhuma turma."));
            listMural.setPlaceholder(new Label("Sem avisos. Matricule-se numa turma."));
            listNotas.setPlaceholder(new Label("Sem notas. Matricule-se numa turma."));
            listTurmas.getItems().clear();
            listMural.getItems().clear();
            listNotas.getItems().clear();
        }
    }

    // O resto da classe permanece igual (configurarListViews, abrirDetalhes, etc.)
    private void configurarListViews() {
        listMural.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Mural item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
                    setText(String.format("[%s por %s] %s\n%s",
                            item.getDataPostagem().format(formatter),
                            item.getAutor().getNome(),
                            item.getTitulo(),
                            item.getConteudo()));
                    setWrapText(true);
                }
            }
        });
        listTurmas.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Turma turma, boolean empty) {
                super.updateItem(turma, empty);
                setText(empty ? null : turma.getNomeTurma() + " - " + turma.getSemestre() + "º Semestre - " + turma.getTurno());
            }
        });
        listNotas.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(BoletimDisciplina item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : item.getDisciplina().getNomeDisciplina() + " - Média: " + String.format("%.2f", item.getMediaFinal()));
            }
        });
    }

    private void abrirDetalhesTurma(Turma turma) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/visaoaluno/DetalhesTurma.fxml"));
            VBox page = loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Detalhes da Turma");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(lblBoasVindas.getScene().getWindow());
            Scene scene = new Scene(page);
            scene.getStylesheets().addAll(lblBoasVindas.getScene().getStylesheets());
            dialogStage.setScene(scene);
            DetalhesTurmaController controller = loader.getController();
            controller.setTurma(turma);
            dialogStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void abrirDetalhesDisciplina(BoletimDisciplina boletim) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/visaoaluno/DetalhesDisciplina.fxml"));
            VBox page = loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Detalhes da Disciplina");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(lblBoasVindas.getScene().getWindow());
            Scene scene = new Scene(page);
            scene.getStylesheets().addAll(lblBoasVindas.getScene().getStylesheets());
            dialogStage.setScene(scene);
            DetalhesDisciplinaController controller = loader.getController();
            controller.setBoletim(boletim);
            dialogStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void abrirDetalhesAviso(Mural aviso) {
        try {
            URL resource = getClass().getResource("/gui/visaoaluno/DetalhesAviso.fxml");
            if (resource == null) {
                System.err.println("Erro: Não foi possível encontrar o ficheiro /gui/visaoaluno/DetalhesAviso.fxml");
                return;
            }
            FXMLLoader loader = new FXMLLoader(resource);
            VBox page = loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Aviso da Turma");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(lblBoasVindas.getScene().getWindow());
            Scene scene = new Scene(page);
            scene.getStylesheets().addAll(lblBoasVindas.getScene().getStylesheets());
            dialogStage.setScene(scene);
            DetalhesAvisoController controller = loader.getController();
            controller.setAviso(aviso);
            dialogStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}