package gui.visaoaluno;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import modelo.BoletimDisciplina;

/**
 *
 * @author João Ricardo
 */
public class DetalhesDisciplinaController {

        @FXML private Label lblNomeDisciplina;
        @FXML private TextField txtNota1, txtNota2, txtMediaFinal, txtTotalAulas, txtFaltas, txtFrequencia, txtStatus;

        public void setBoletim(BoletimDisciplina boletim) {
            if (boletim != null) {
                lblNomeDisciplina.setText(boletim.getDisciplina().getNomeDisciplina());
                txtNota1.setText(String.format("%.2f", boletim.getNota1()));
                txtNota2.setText(String.format("%.2f", boletim.getNota2()));
                txtMediaFinal.setText(String.format("%.2f", boletim.getMediaFinal()));
                txtTotalAulas.setText(String.valueOf(boletim.getTotalAulas()));
                txtFaltas.setText(String.valueOf(boletim.getNumeroFaltas()));
                txtFrequencia.setText(String.format("%.1f%%", boletim.getFrequencia()));
                txtStatus.setText(boletim.getStatus());
            }
        }
}
