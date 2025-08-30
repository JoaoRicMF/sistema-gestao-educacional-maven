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

    // --- DAOs ---
    private final AlunoDAO alunoDAO = new AlunoDAO();
    private final ProfessorDAO professorDAO = new ProfessorDAO();
    private final DisciplinaDAO disciplinaDAO = new DisciplinaDAO();
    private final TurmaDAO turmaDAO = new TurmaDAO();

    // --- Serviços ---
    private final AlunoService alunoService = new AlunoService();
    private final ProfessorService professorService = new ProfessorService();
    private final DisciplinaService disciplinaService = new DisciplinaService();
    private final TurmaService turmaService = new TurmaService();
    private final MatriculaService matriculaService = new MatriculaService();
    private final NotaService notaService = new NotaService();
    private final FrequenciaService frequenciaService = new FrequenciaService();
    private final MuralService muralService = new MuralService();

    // --- Listas de Dados ---
    private final List<Disciplina> todasAsDisciplinas = new ArrayList<>();
    private final List<Professor> todosOsProfessores = new ArrayList<>();
    private final List<Aluno> todosOsAlunos = new ArrayList<>();
    private final List<Turma> todasAsTurmas = new ArrayList<>();

    private String lightTheme;
    private String darkTheme;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Sistema de Gestão Educacional");
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/Imagens/icone_principal_24dp_google_material_Symbols.png")));
        lightTheme = getClass().getResource("/gui/css/styles.css").toExternalForm();
        darkTheme = getClass().getResource("/gui/css/dark-theme.css").toExternalForm();

        Parent root = carregarFXML("/gui/telainicial/TelaInicial.fxml");
        if (root == null) return;

        scene = new Scene(root);
        atualizarTema();
        primaryStage.setScene(scene);
        primaryStage.setMaximized(true);
        primaryStage.show();
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
        Parent root = carregarFXML("/gui/telainicial/TelaInicial.fxml");
        navegarPara(root);
    }

    public void abrirJanelaPrincipal(String perfil) {
        VBox loadingLayout = new VBox(20);
        loadingLayout.setAlignment(Pos.CENTER);
        loadingLayout.getChildren().add(new ProgressIndicator());
        loadingLayout.getChildren().add(new Label("A carregar dados do banco..."));
        loadingLayout.getStyleClass().add("root");
        navegarPara(loadingLayout);

        Task<Void> carregarDadosTask = new Task<>() {
            @Override
            protected Void call() throws Exception {
                System.out.println("Iniciando carregamento de dados em background...");
                carregarDadosIniciaisDoBanco();
                System.out.println("Dados carregados.");
                return null;
            }
        };

        carregarDadosTask.setOnSucceeded(e -> {
            System.out.println("Tarefa de carregamento concluída. Construindo e exibindo a UI...");
            try {
                BorderPane layoutPrincipal = criarLayoutPrincipal(perfil);
                navegarPara(layoutPrincipal);
            } catch (IOException ex) {
                ex.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Erro Crítico", "Falha ao construir a interface principal.");
            }
        });

        carregarDadosTask.setOnFailed(e -> {
            System.err.println("A tarefa de carregamento de dados falhou.");
            carregarDadosTask.getException().printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erro de Conexão", "Não foi possível conectar ao banco de dados.\nVerifique sua conexão ou contate o suporte.");
            mostrarTelaInicial();
        });

        new Thread(carregarDadosTask).start();
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

        System.out.println("Dados carregados do banco: " + todosOsAlunos.size() + " alunos, "
                + todosOsProfessores.size() + " professores, " + todasAsDisciplinas.size() + " disciplinas, "
                + todasAsTurmas.size() + " turmas.");
    }

    private BorderPane criarLayoutPrincipal(String perfil) throws IOException {
        TabPane tabPane = new TabPane();
        VBox.setVgrow(tabPane, Priority.ALWAYS);

        if ("admin".equals(perfil)) {
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
        primaryStage.setTitle("Sistema de Gestão Educacional - Painel Principal");

        return layoutPrincipal;
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

        // Crie os ImageViews para os ícones de tema
        ImageView lightModeIcon = new ImageView(new Image(getClass().getResourceAsStream("/Imagens/white_mode_24dp_google_material_Symbols.png")));
        ImageView darkModeIcon = new ImageView(new Image(getClass().getResourceAsStream("/Imagens/dark_mode_24dp_google_material_Symbols.png")));
        lightModeIcon.setFitWidth(24);
        lightModeIcon.setFitHeight(24);
        darkModeIcon.setFitWidth(24);
        darkModeIcon.setFitHeight(24);


        ToggleButton themeToggle = new ToggleButton();
        themeToggle.getStyleClass().add("toggle-button");
        themeToggle.setGraphic(isDarkMode ? darkModeIcon : lightModeIcon); // Define o gráfico inicial
        themeToggle.setSelected(isDarkMode);
        themeToggle.setOnAction(event -> {
            toggleTheme();
            themeToggle.setGraphic(isDarkMode ? darkModeIcon : lightModeIcon); // Atualiza o gráfico
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
                scene.setFill(Color.valueOf("#1e1e1e"));
            } else {
                scene.getStylesheets().add(lightTheme);
                scene.setFill(Color.valueOf("#F4F6F8"));
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

        if (controller instanceof PainelAlunoController) {
            ((PainelAlunoController) controller).setTodosOsAlunos(todosOsAlunos);
        } else if (controller instanceof PainelProfessorController) {
            ((PainelProfessorController) controller).setTodosOsProfessores(todosOsProfessores);
            ((PainelProfessorController) controller).setTodasAsDisciplinas(todasAsDisciplinas);
        } else if (controller instanceof PainelDisciplinaController) {
            ((PainelDisciplinaController) controller).setTodasAsDisciplinas(todasAsDisciplinas);
        } else if (controller instanceof PainelTurmaController) {
            ((PainelTurmaController) controller).setTodasAsTurmas(todasAsTurmas);
            ((PainelTurmaController) controller).setListaDeProfessores(todosOsProfessores);
            ((PainelTurmaController) controller).setTodasAsDisciplinas(todasAsDisciplinas);
        } else if (controller instanceof PainelMatriculaController) {
            ((PainelMatriculaController) controller).setMatriculaService(matriculaService);
            ((PainelMatriculaController) controller).setListaDeAlunos(todosOsAlunos);
            ((PainelMatriculaController) controller).setListaDeTurmas(todasAsTurmas);
        } else if (controller instanceof PainelNotasController) {
            ((PainelNotasController) controller).setListaDeAlunos(todosOsAlunos);
            ((PainelNotasController) controller).setListaDeDisciplinas(todasAsDisciplinas);
        } else if (controller instanceof PainelFrequenciaController) {
            ((PainelFrequenciaController) controller).setTodasAsTurmas(todasAsTurmas);
            ((PainelFrequenciaController) controller).setTodasAsDisciplinas(todasAsDisciplinas);
        } else if (controller instanceof PainelMuralController) {
            ((PainelMuralController) controller).setTodasAsTurmas(todasAsTurmas);
            ((PainelMuralController) controller).setTodosOsProfessores(todosOsProfessores);
        } else if (controller instanceof PainelVisaoAlunoController) {
            if (!todosOsAlunos.isEmpty()) {
                ((PainelVisaoAlunoController) controller).setAluno(todosOsAlunos.get(0), todasAsTurmas);
                ((PainelVisaoAlunoController) controller).setMuralService(muralService);
                ((PainelVisaoAlunoController) controller).setNotaService(notaService);
                ((PainelVisaoAlunoController) controller).setFrequenciaService(frequenciaService);
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

    private void carregarDadosIniciais() {
        if (todosOsAlunos.isEmpty()) {
            todosOsAlunos.addAll(alunoDAO.listarTodos());
        }
        if (todasAsDisciplinas.isEmpty()) {
            Disciplina disciplina1 = new Disciplina("Programação Orientada a Objetos", 80, 2);
            Disciplina disciplina2 = new Disciplina("Estrutura de Dados", 60, 3);
            todasAsDisciplinas.add(disciplina1);
            todasAsDisciplinas.add(disciplina2);

            Professor professor1 = new Professor("Prof. Andre", "11122233344", "1234567",
                    Genero.MASCULINO, "01/01/1980", "11987654321", "prof.andre@escola.com", null, "PROF001");
            todosOsProfessores.add(professor1);

            if(todosOsAlunos.isEmpty()){
                Aluno aluno1 = new Aluno("Aluno Teste", "12312312312", "1234567",
                        Genero.NAO_INFORMAR, "10/10/2000", "11988887777", "aluno@escola.com", null,
                        "ALN123456", "Ciência da Computação", TipoCurso.PRESENCIAL, 1,
                        NivelAcademico.GRADUACAO_EM_ANDAMENTO, StatusAluno.ATIVO, 0, 0, 0);
                todosOsAlunos.add(aluno1);
            }


            Turma turma1 = new Turma("Turma A", 1, Turno.MANHA);
            if(!todosOsAlunos.isEmpty()){
                turma1.adicionarAluno(todosOsAlunos.get(0));
            }
            turma1.adicionarDisciplina(disciplina1);
            turma1.adicionarDisciplina(disciplina2);
            todasAsTurmas.add(turma1);
        }
    }
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}