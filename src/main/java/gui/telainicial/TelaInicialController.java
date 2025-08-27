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

    // Adicione os ImageViews para os ícones de tema aqui também
    private final ImageView lightModeIcon = new ImageView(new Image(getClass().getResourceAsStream("/Imagens/white_mode_24dp_google_material_Symbols.png")));
    private final ImageView darkModeIcon = new ImageView(new Image(getClass().getResourceAsStream("/Imagens/dark_mode_24dp_google_material_Symbols.png")));

    public void setMainApp(PrincipalFX mainApp) {
        this.mainApp = mainApp;

        // Configura o tamanho dos ícones
        lightModeIcon.setFitWidth(24);
        lightModeIcon.setFitHeight(24);
        darkModeIcon.setFitWidth(24);
        darkModeIcon.setFitHeight(24);

        // Sincroniza o estado do botão e do ícone quando a tela é carregada
        if (mainApp.isDarkMode()) {
            themeToggle.setSelected(true);
            themeToggle.setGraphic(darkModeIcon); // Define o gráfico para dark mode
            schoolIcon.setImage(new Image(getClass().getResourceAsStream("/Imagens/icone_principal_24dp_google_material_Symbols_Dark.png")));
        } else {
            themeToggle.setSelected(false);
            themeToggle.setGraphic(lightModeIcon); // Define o gráfico para light mode
            schoolIcon.setImage(new Image(getClass().getResourceAsStream("/Imagens/icone_principal_24dp_google_material_Symbols.png")));
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
            themeToggle.setGraphic(darkModeIcon); // Atualiza o gráfico para dark mode
            schoolIcon.setImage(new Image(getClass().getResourceAsStream("/Imagens/icone_principal_24dp_google_material_Symbols_Dark.png")));
        } else {
            themeToggle.setSelected(false);
            themeToggle.setGraphic(lightModeIcon); // Atualiza o gráfico para light mode
            schoolIcon.setImage(new Image(getClass().getResourceAsStream("/Imagens/icone_principal_24dp_google_material_Symbols.png")));
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