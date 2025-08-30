package gui;

import gui.aluno.PainelAlunoController;
import gui.disciplina.PainelDisciplinaController;
import gui.matricula.PainelMatriculaController;
import gui.mural.PainelMuralController;
import gui.notas.PainelNotasController;
import gui.professor.PainelProfessorController;
import gui.telainicial.TelaInicialController;
import gui.turma.PainelTurmaController;
import gui.visaoaluno.PainelVisaoAlunoController;
import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import modelo.*;
import service.*;
import javafx.scene.paint.Color;
import gui.frequencia.PainelFrequenciaController;
import service.FrequenciaService;
import dao.*;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class PrincipalFX extends Application {

    private Stage primaryStage;
    private Scene scene;
    private boolean isDarkMode = false;
    private Object usuarioLogado;

    private final AlunoDAO alunoDAO = new AlunoDAO();
    private final ProfessorDAO professorDAO = new ProfessorDAO();
    private final DisciplinaDAO disciplinaDAO = new DisciplinaDAO();
    private final TurmaDAO turmaDAO = new TurmaDAO();

    private final AlunoService alunoService = new AlunoService();
    private final ProfessorService professorService = new ProfessorService();
    private final DisciplinaService disciplinaService = new DisciplinaService();
    private final TurmaService turmaService = new TurmaService();
    private final MatriculaService matriculaService = new MatriculaService();
    private final NotaService notaService = new NotaService();
    private final FrequenciaService frequenciaService = new FrequenciaService();
    private final MuralService muralService = new MuralService();

    private final List<Disciplina> todasAsDisciplinas = new ArrayList<>();
    private final List<Professor> todosOsProfessores = new ArrayList<>();
    private final List<Aluno> todosOsAlunos = new ArrayList<>();
    private final List<Turma> todasAsTurmas = new ArrayList<>();

    private String lightTheme;
    private String darkTheme;

    public List<Aluno> getTodosOsAlunos() { return todosOsAlunos; }
    public List<Professor> getTodosOsProfessores() { return todosOsProfessores; }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Sistema de Gestão Educacional");
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/Imagens/icone_principal_24dp_google_material_Symbols.png")));
        lightTheme = getClass().getResource("/gui/css/styles.css").toExternalForm();
        darkTheme = getClass().getResource("/gui/css/dark-theme.css").toExternalForm();

        VBox loadingLayout = criarLayoutCarregamento("A carregar dados iniciais...");
        scene = new Scene(loadingLayout);
        atualizarTema();
        primaryStage.setScene(scene);
        primaryStage.setMaximized(true);
        primaryStage.show();

        carregarDadosEExibirTelaInicial();
    }

    private void carregarDadosEExibirTelaInicial() {
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() throws Exception {
                carregarDadosIniciaisDoBanco();
                return null;
            }
        };

        task.setOnSucceeded(e -> {
            Parent root = carregarFXML("/gui/telainicial/TelaInicial.fxml");
            navegarPara(root);
        });

        task.setOnFailed(e -> {
            task.getException().printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erro de Conexão", "Não foi possível carregar os dados do banco.");
        });

        new Thread(task).start();
    }

    public boolean isDarkMode() {
        return isDarkMode;
    }

    private void navegarPara(Parent proximoConteudo) {
        if (scene == null || proximoConteudo == null) return;
        Node conteudoAtual = scene.getRoot();

        FadeTransition fadeOut = new FadeTransition(Duration.millis(250), conteudoAtual);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);
        fadeOut.setOnFinished(event -> {
            proximoConteudo.setOpacity(0.0);
            scene.setRoot(proximoConteudo);
            FadeTransition fadeIn = new FadeTransition(Duration.millis(250), proximoConteudo);
            fadeIn.setFromValue(0.0);
            fadeIn.setToValue(1.0);
            fadeIn.play();
        });
        fadeOut.play();
    }

    public void mostrarTelaInicial() {
        VBox loadingLayout = criarLayoutCarregamento("A regressar ao início...");
        navegarPara(loadingLayout);

        carregarDadosEExibirTelaInicial();
    }

    public void abrirJanelaPrincipal(String perfil, Object usuario) {
        this.usuarioLogado = usuario;

        VBox loadingLayout = criarLayoutCarregamento("A carregar painel...");
        navegarPara(loadingLayout);

        Task<Void> task = new Task<>() {
            @Override
            protected Void call() throws Exception {
                BorderPane layoutPrincipal = criarLayoutPrincipal(perfil);
                Platform.runLater(() -> navegarPara(layoutPrincipal));
                return null;
            }
        };

        task.setOnFailed(e -> {
            task.getException().printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erro Crítico", "Falha ao construir a interface principal.");
        });

        new Thread(task).start();
    }

    private void carregarDadosIniciaisDoBanco() {
        todosOsAlunos.clear();
        todosOsProfessores.clear();
        todasAsDisciplinas.clear();
        todasAsTurmas.clear();

        todosOsAlunos.addAll(alunoDAO.listarTodos());
        todosOsProfessores.addAll(professorDAO.listarTodos());
        todasAsDisciplinas.addAll(disciplinaDAO.listarTodas());
        todasAsTurmas.addAll(turmaDAO.listarTodas());
    }

    private BorderPane criarLayoutPrincipal(String perfil) throws IOException {
        TabPane tabPane = new TabPane();
        VBox.setVgrow(tabPane, Priority.ALWAYS);

        if ("admin".equals(perfil) || "administrativo".equals(perfil)) {
            carregarAbasAdministrativas(tabPane);
        } else if ("professor".equals(perfil)) {
            tabPane.getTabs().add(carregarAba("notas/PainelNotas.fxml", "LANÇAR NOTAS"));
            tabPane.getTabs().add(carregarAba("frequencia/PainelFrequencia.fxml", "LANÇAR FREQUÊNCIA"));
            tabPane.getTabs().add(carregarAba("mural/PainelMural.fxml", "MURAL DE AVISOS"));
        } else if ("aluno".equals(perfil)) {
            tabPane.getTabs().add(carregarAba("visaoaluno/PainelVisaoAluno.fxml", "PORTAL DO ALUNO"));
        }

        HBox header = criarCabecalho();

        Label footerLabel = new Label("Sistema de Gestão Educacional v1.0 2025");
        footerLabel.getStyleClass().add("footer-label");

        HBox footer = new HBox(footerLabel);
        footer.setAlignment(Pos.CENTER);
        footer.setPadding(new Insets(10));

        BorderPane layoutPrincipal = new BorderPane();
        layoutPrincipal.getStyleClass().add("root");
        layoutPrincipal.setTop(header);
        layoutPrincipal.setCenter(tabPane);
        layoutPrincipal.setBottom(footer);
        BorderPane.setMargin(tabPane, new Insets(15));

        Platform.runLater(() -> primaryStage.setTitle("Sistema de Gestão Educacional - Painel Principal"));

        return layoutPrincipal;
    }

    private VBox criarLayoutCarregamento(String mensagem) {
        VBox loadingLayout = new VBox(20);
        loadingLayout.setAlignment(Pos.CENTER);
        loadingLayout.getChildren().add(new ProgressIndicator());
        loadingLayout.getChildren().add(new Label(mensagem));
        loadingLayout.getStyleClass().add("root");
        return loadingLayout;
    }

    private HBox criarCabecalho() {
        Button btnVoltar = new Button("⬅ Voltar ao Início");
        btnVoltar.getStyleClass().add("botao-voltar");
        btnVoltar.setOnAction(e -> mostrarTelaInicial());

        Label lblRelogio = new Label();
        lblRelogio.getStyleClass().add("relogio-label");
        DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

        Timeline relogioTimeline = new Timeline(new KeyFrame(Duration.ZERO, e -> {
            lblRelogio.setText(LocalDateTime.now().format(formatador));
        }), new KeyFrame(Duration.seconds(1)));
        relogioTimeline.setCycleCount(Animation.INDEFINITE);
        relogioTimeline.play();

        ImageView lightModeIcon = new ImageView(new Image(getClass().getResourceAsStream("/Imagens/white_mode_24dp_google_material_Symbols.png")));
        ImageView darkModeIcon = new ImageView(new Image(getClass().getResourceAsStream("/Imagens/dark_mode_24dp_google_material_Symbols.png")));
        lightModeIcon.setFitWidth(24);
        lightModeIcon.setFitHeight(24);
        darkModeIcon.setFitWidth(24);
        darkModeIcon.setFitHeight(24);

        ToggleButton themeToggle = new ToggleButton();
        themeToggle.getStyleClass().add("toggle-button");
        themeToggle.setGraphic(isDarkMode ? darkModeIcon : lightModeIcon);
        themeToggle.setSelected(isDarkMode);
        themeToggle.setOnAction(event -> {
            toggleTheme();
            themeToggle.setGraphic(isDarkMode ? darkModeIcon : lightModeIcon);
        });

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox header = new HBox(10, btnVoltar, spacer, lblRelogio, themeToggle);
        header.setPadding(new Insets(15, 15, 0, 15));
        header.setAlignment(Pos.CENTER);
        return header;
    }

    public void toggleTheme() {
        isDarkMode = !isDarkMode;
        atualizarTema();
    }

    private void atualizarTema() {
        if (scene != null) {
            scene.getStylesheets().clear();
            if (isDarkMode) {
                scene.getStylesheets().add(darkTheme);
                scene.setFill(Color.valueOf("#1e1e1e")); // Define o fundo da cena para preto
            } else {
                scene.getStylesheets().add(lightTheme);
                scene.setFill(Color.valueOf("#f8f9fa")); // Define o fundo da cena para o cinza claro
            }
        }
    }

    private Parent carregarFXML(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            Object controller = loader.getController();
            if (controller instanceof TelaInicialController) {
                ((TelaInicialController) controller).setMainApp(this);
            }
            return root;
        } catch (IOException e) {
            System.err.println("Falha ao carregar o FXML: " + fxmlPath);
            e.printStackTrace();
            return null;
        }
    }

    private Tab carregarAba(String fxmlFile, String tabTitle) throws IOException {
        URL resourceUrl = getClass().getResource("/gui/" + fxmlFile);
        if (resourceUrl == null) {
            throw new IOException("Não foi possível encontrar o ficheiro FXML: /gui/" + fxmlFile);
        }
        FXMLLoader loader = new FXMLLoader(resourceUrl);
        Parent root = loader.load();
        Object controller = loader.getController();

        // Injeção de dependências...
        if (controller instanceof PainelAlunoController) {
            ((PainelAlunoController) controller).setTodosOsAlunos(todosOsAlunos);
        } else if (controller instanceof PainelProfessorController) {
            ((PainelProfessorController) controller).setProfessorService(professorService);
            ((PainelProfessorController) controller).setTodosOsProfessores(todosOsProfessores);
            ((PainelProfessorController) controller).setTodasAsDisciplinas(todasAsDisciplinas);
        } else if (controller instanceof PainelDisciplinaController) {
            ((PainelDisciplinaController) controller).setService(disciplinaService);
            ((PainelDisciplinaController) controller).setTodasAsDisciplinas(todasAsDisciplinas);
        } else if (controller instanceof PainelTurmaController) {
            ((PainelTurmaController) controller).setTurmaService(turmaService);
            ((PainelTurmaController) controller).setTodasAsTurmas(todasAsTurmas);
            ((PainelTurmaController) controller).setListaDeProfessores(todosOsProfessores);
            ((PainelTurmaController) controller).setTodasAsDisciplinas(todasAsDisciplinas);
        } else if (controller instanceof PainelMatriculaController) {
            ((PainelMatriculaController) controller).setMatriculaService(matriculaService);
            ((PainelMatriculaController) controller).setListaDeAlunos(todosOsAlunos);
            ((PainelMatriculaController) controller).setListaDeTurmas(todasAsTurmas);
        } else if (controller instanceof PainelNotasController) {
            ((PainelNotasController) controller).setNotaService(notaService);
            ((PainelNotasController) controller).setListaDeAlunos(todosOsAlunos);
            ((PainelNotasController) controller).setListaDeDisciplinas(todasAsDisciplinas);
        } else if (controller instanceof PainelFrequenciaController) {
            PainelFrequenciaController painelController = (PainelFrequenciaController) controller;
            painelController.setFrequenciaService(frequenciaService);
            painelController.setTodasAsTurmas(todasAsTurmas);
            painelController.setTodasAsDisciplinas(todasAsDisciplinas);
        } else if (controller instanceof PainelMuralController) {
            ((PainelMuralController) controller).setMuralService(muralService);
            ((PainelMuralController) controller).setTodasAsTurmas(todasAsTurmas);
            ((PainelMuralController) controller).setTodosOsProfessores(todosOsProfessores);
        } else if (controller instanceof PainelVisaoAlunoController) {
            if (usuarioLogado instanceof Aluno) {
                PainelVisaoAlunoController painelController = (PainelVisaoAlunoController) controller;
                painelController.setAluno((Aluno) usuarioLogado, todasAsTurmas);
                painelController.setMuralService(muralService);
                painelController.setNotaService(notaService);
                painelController.setFrequenciaService(frequenciaService);
                painelController.iniciarControlador();
            }
        }

        Tab tab = new Tab(tabTitle, root);
        tab.setClosable(false);
        return tab;
    }

    private void carregarAbasAdministrativas(TabPane tabPane) throws IOException {
        tabPane.getTabs().add(carregarAba("aluno/PainelAluno.fxml", "CADASTRO ALUNO"));
        tabPane.getTabs().add(carregarAba("professor/PainelProfessor.fxml", "CADASTRO PROFESSOR"));
        tabPane.getTabs().add(carregarAba("disciplina/PainelDisciplina.fxml", "CADASTRO DISCIPLINA"));
        tabPane.getTabs().add(carregarAba("turma/PainelTurma.fxml", "CADASTRO DE TURMA"));
        tabPane.getTabs().add(carregarAba("matricula/PainelMatricula.fxml", "MATRICULA DE ALUNO"));
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}