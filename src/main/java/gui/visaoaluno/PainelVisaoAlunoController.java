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
import service.MuralService;
import java.io.IOException;
import java.time.format.DateTimeFormatter;

public class PainelVisaoAlunoController {

    @FXML private Label lblBoasVindas;
    @FXML private ListView<BoletimDisciplina> listNotas;
    @FXML private ListView<Turma> listTurmas;
    @FXML private ListView<Mural> listMural;
    @FXML private TextField txtNome;
    @FXML private TextField txtMatricula;
    @FXML private TextField txtCurso;
    @FXML private TextField txtEmail;

    private Aluno alunoLogado;
    private Turma turmaDoAluno;
    private MuralService muralService;

    public void setAluno(Aluno aluno, Turma turma) {
        this.alunoLogado = aluno;
        this.turmaDoAluno = turma;
        atualizarDados();
    }
    public void setMuralService(MuralService muralService) { this.muralService = muralService; }

    @FXML
    public void initialize() {
        // --- Configuração das Listas ---
        listMural.setCellFactory(lv -> new ListCell<>() {
            protected void updateItem(Mural item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
                    setText(String.format("[%s] %s: %s",
                            item.getDataPostagem().format(formatter),
                            item.getTitulo(),
                            item.getConteudo()));
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

        // --- Configuração dos cliques ---
        listTurmas.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                Turma turmaSelecionada = listTurmas.getSelectionModel().getSelectedItem();
                if (turmaSelecionada != null) {
                    abrirDetalhesTurma(turmaSelecionada);
                }
            }
        });

        listNotas.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                BoletimDisciplina boletim = listNotas.getSelectionModel().getSelectedItem();
                if (boletim != null) {
                    abrirDetalhesDisciplina(boletim);
                }
            }
        });
    }

    private void atualizarDados() {
        if (alunoLogado != null) {
            lblBoasVindas.setText("Bem-vindo(a), " + alunoLogado.getNome() + "!");
            txtNome.setText(alunoLogado.getNome());
            txtMatricula.setText(alunoLogado.getMatricula());
            txtCurso.setText(alunoLogado.getCurso());
            txtEmail.setText(alunoLogado.getEmail());

            if(turmaDoAluno != null) {
                // Preenche a lista de turmas
                listTurmas.getItems().add(turmaDoAluno);

                // Preenche o mural
                if(muralService != null) {
                    listMural.getItems().setAll(muralService.verPostagensDaTurma(turmaDoAluno));
                }

                // Preenche as notas com base nas disciplinas da turma
                if (!turmaDoAluno.getDisciplinas().isEmpty()) {
                    Disciplina d1 = turmaDoAluno.getDisciplinas().get(0);
                    BoletimDisciplina b1 = new BoletimDisciplina(d1, 9.0, 10.0, 9.5, 40, 1, "APROVADO");
                    listNotas.getItems().add(b1);

                    if (turmaDoAluno.getDisciplinas().size() > 1) {
                        Disciplina d2 = turmaDoAluno.getDisciplinas().get(1);
                        BoletimDisciplina b2 = new BoletimDisciplina(d2, 7.5, 8.5, 8.0, 30, 2, "APROVADO");
                        listNotas.getItems().add(b2);
                    }
                }
            }
        }
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
}