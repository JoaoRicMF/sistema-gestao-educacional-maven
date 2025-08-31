package gui.telainicial;

import gui.PrincipalFX;
import gui.util.Buscar;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import modelo.Aluno;
import modelo.Professor;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author Josemar
 */

public class TelaInicialController {
    private PrincipalFX mainApp;
    private List<Aluno> todosOsAlunos;
    private List<Professor> todosOsProfessores;

    @FXML private ToggleButton themeToggle;
    @FXML private ImageView schoolIcon;
    @FXML private Button btnAdministrativo, btnProfessor, btnAluno;

    private ImageView lightModeIcon;
    private ImageView darkModeIcon;


    public void setMainApp(PrincipalFX mainApp) {
        this.mainApp = mainApp;
        this.todosOsAlunos = mainApp.getTodosOsAlunos();
        this.todosOsProfessores = mainApp.getTodosOsProfessores();

        lightModeIcon = new ImageView(new Image(getClass().getResourceAsStream("/Imagens/white_mode_24dp_google_material_Symbols.png")));
        darkModeIcon = new ImageView(new Image(getClass().getResourceAsStream("/Imagens/dark_mode_24dp_google_material_Symbols.png")));
        lightModeIcon.setFitWidth(24);
        lightModeIcon.setFitHeight(24);
        darkModeIcon.setFitWidth(24);
        darkModeIcon.setFitHeight(24);

        atualizarIconesDeTema();
    }

    @FXML
    private void abrirLoginAluno(ActionEvent event) {
        if (todosOsAlunos == null || todosOsAlunos.isEmpty()) {
            showAlert(Alert.AlertType.INFORMATION, "Aviso", "Não há alunos cadastrados para login.");
            return;
        }

        Buscar<Aluno> buscarDialog = new Buscar<>("Login Aluno", "Selecione o seu nome na lista:", todosOsAlunos, Aluno::getNome);
        Optional<Aluno> resultado = buscarDialog.showAndWait();

        resultado.ifPresent(aluno -> mainApp.abrirJanelaPrincipal("aluno", aluno));
    }

    @FXML
    private void abrirLoginProfessor(ActionEvent event) {
        if (todosOsProfessores == null || todosOsProfessores.isEmpty()) {
            showAlert(Alert.AlertType.INFORMATION, "Aviso", "Não há professores cadastrados para login.");
            return;
        }

        Buscar<Professor> buscarDialog = new Buscar<>("Login Professor", "Selecione o seu nome na lista:", todosOsProfessores, Professor::getNome);
        Optional<Professor> resultado = buscarDialog.showAndWait();

        resultado.ifPresent(professor -> mainApp.abrirJanelaPrincipal("professor", professor));
    }

    @FXML
    private void abrirPainelAdministrativo(ActionEvent event) {
        mainApp.abrirJanelaPrincipal("admin", null);
    }

    @FXML
    private void toggleTheme(ActionEvent event) {
        mainApp.toggleTheme();
        atualizarIconesDeTema();
    }

    private void atualizarIconesDeTema() {
        if (mainApp.isDarkMode()) {
            themeToggle.setGraphic(darkModeIcon);
            schoolIcon.setImage(new Image(getClass().getResourceAsStream("/Imagens/icone_principal_24dp_google_material_Symbols_Dark.png")));
        } else {
            themeToggle.setGraphic(lightModeIcon);
            schoolIcon.setImage(new Image(getClass().getResourceAsStream("/Imagens/icone_principal_24dp_google_material_Symbols.png")));
        }
        themeToggle.setSelected(mainApp.isDarkMode());
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}