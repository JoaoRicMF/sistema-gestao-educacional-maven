package gui.util;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 *
 * @author João Ricardo
 */

public class BuscaController<T> {

    @FXML private TextField txtFiltro;
    @FXML private ListView<T> listView;
    @FXML private Button btnConfirmar;
    @FXML private Button btnCancelar;

    private List<T> listaCompleta;
    private Function<T, String> conversorTexto;
    private T itemSelecionado;

    @FXML
    public void initialize() {
        btnConfirmar.setDisable(true);
        listView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            btnConfirmar.setDisable(newVal == null);
        });

        // Adiciona evento de duplo clique para seleção rápida
        listView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && listView.getSelectionModel().getSelectedItem() != null) {
                confirmar();
            }
        });
    }

    public void setItens(List<T> itens, Function<T, String> conversorTexto) {
        this.listaCompleta = itens;
        this.conversorTexto = conversorTexto;

        listView.setItems(FXCollections.observableArrayList(listaCompleta));
        configurarCelulasListView();
        configurarFiltro();
    }

    private void configurarCelulasListView() {
        listView.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(T item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : conversorTexto.apply(item));
            }
        });
    }

    private void configurarFiltro() {
        txtFiltro.textProperty().addListener((observable, oldValue, newValue) -> {
            filtrarLista(newValue);
        });
    }

    private void filtrarLista(String filtro) {
        if (filtro == null || filtro.isEmpty()) {
            listView.setItems(FXCollections.observableArrayList(listaCompleta));
            return;
        }
        String filtroLowerCase = filtro.toLowerCase();
        List<T> listaFiltrada = listaCompleta.stream()
                .filter(item -> conversorTexto.apply(item).toLowerCase().contains(filtroLowerCase))
                .collect(Collectors.toList());
        listView.setItems(FXCollections.observableArrayList(listaFiltrada));
    }

    @FXML
    private void confirmar() {
        this.itemSelecionado = listView.getSelectionModel().getSelectedItem();
        fecharJanela();
    }

    @FXML
    private void cancelar() {
        this.itemSelecionado = null;
        fecharJanela();
    }

    public T getItemSelecionado() {
        return itemSelecionado;
    }

    private void fecharJanela() {
        Stage stage = (Stage) btnConfirmar.getScene().getWindow();
        stage.close();
    }
}