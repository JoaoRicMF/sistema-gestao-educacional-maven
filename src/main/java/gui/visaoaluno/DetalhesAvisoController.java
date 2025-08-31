package gui.visaoaluno;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import modelo.Mural;

import java.time.format.DateTimeFormatter;

/**
 *
 * @author Josemar
 */

public class DetalhesAvisoController {
    @FXML private Label lblTitulo;
    @FXML private Label lblSubtitulo;
    @FXML private TextArea txtConteudo;

    public void setAviso(Mural aviso) {
        if (aviso != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            lblTitulo.setText(aviso.getTitulo());
            lblSubtitulo.setText("Enviado por " + aviso.getAutor().getNome() + " em " + aviso.getDataPostagem().format(formatter));
            txtConteudo.setText(aviso.getConteudo());
        }
    }
}
