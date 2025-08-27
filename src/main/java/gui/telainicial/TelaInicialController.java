package gui.telainicial;

import gui.PrincipalFX;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.animation.FadeTransition;
import javafx.util.Duration;
import javafx.scene.Node;

public class TelaInicialController {
    private PrincipalFX mainApp;

    @FXML
    private ToggleButton themeToggle;

    @FXML
    private ImageView schoolIcon;

    public void setMainApp(PrincipalFX mainApp) {
        this.mainApp = mainApp;

        // Sincroniza o estado do botão e do ícone quando a tela é carregada
        if (mainApp.isDarkMode()) {
            themeToggle.setSelected(true);
            themeToggle.setText("☀️");
            schoolIcon.setImage(new Image(getClass().getResourceAsStream("/Imagens/school_24dp_google_material_Symbols_Dark.png")));
        } else {
            themeToggle.setSelected(false);
            themeToggle.setText("🌙");
            schoolIcon.setImage(new Image(getClass().getResourceAsStream("/Imagens/school_24dp_google_material_Symbols.png")));
        }
    }

    @FXML
    private void abrirPainelAdministrativo(ActionEvent event) {
        mainApp.abrirJanelaPrincipal("admin");
    }

    @FXML
    private void abrirPainelProfessor(ActionEvent event) {
        mainApp.abrirJanelaPrincipal("professor");
    }

    @FXML
    private void abrirPainelAluno(ActionEvent event) {
        mainApp.abrirJanelaPrincipal("aluno");
    }

    @FXML
    private void toggleTheme(ActionEvent event) {
        mainApp.toggleTheme();

        // Atualiza o botão e o ícone após a troca de tema
        if (mainApp.isDarkMode()) {
            themeToggle.setSelected(true);
            themeToggle.setText("☀️");
            schoolIcon.setImage(new Image(getClass().getResourceAsStream("/Imagens/school_24dp_google_material_Symbols_Dark.png")));
        } else {
            themeToggle.setSelected(false);
            themeToggle.setText("🌙");
            schoolIcon.setImage(new Image(getClass().getResourceAsStream("/Imagens/school_24dp_google_material_Symbols.png")));
        }
    }
    @FXML
    private Button btnAdministrativo;

    @FXML
    private Button btnProfessor;

    @FXML
    private Button btnAluno;

    @FXML
    public void initialize() {
        // Aplica o efeito de fade in em cada botão
        btnAdministrativo.setOpacity(0);
        btnProfessor.setOpacity(0);
        btnAluno.setOpacity(0);

        aplicarFadeIn(btnAdministrativo, 100);
        aplicarFadeIn(btnProfessor, 200);
        aplicarFadeIn(btnAluno, 300);
    }

    private void aplicarFadeIn(Node node, double delay) {
        FadeTransition ft = new FadeTransition(Duration.millis(500), node);
        ft.setFromValue(0.0);
        ft.setToValue(1.0);
        ft.setDelay(Duration.millis(delay));
        ft.setCycleCount(1);
        ft.setAutoReverse(false);
        ft.play();
    }
}