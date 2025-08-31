package gui.util;

import javafx.collections.FXCollections;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 *
 * @author João Ricardo
 */

public class Buscar<T> extends Dialog<T> {

    private final TextField txtFiltro = new TextField();
    private final ListView<T> listView = new ListView<>();
    private final List<T> listaCompleta;
    private final Function<T, String> conversorTexto;

    public Buscar(String titulo, String headerText, List<T> itens, Function<T, String> conversorTexto) {
        this.listaCompleta = itens;
        this.conversorTexto = conversorTexto;

        setTitle(titulo);
        setHeaderText(headerText);

        configurarLayout();
        configurarFiltro();
        configurarListView();
        configurarBotoes();
    }

    private void configurarLayout() {
        VBox content = new VBox(10);
        txtFiltro.setPromptText("Digite para filtrar...");

        // Faz a ListView crescer para preencher o espaço
        VBox.setVgrow(listView, Priority.ALWAYS);

        content.getChildren().addAll(new Label("Filtro:"), txtFiltro, listView);
        getDialogPane().setContent(content);
        getDialogPane().setPrefSize(400, 300);
    }

    private void configurarFiltro() {
        txtFiltro.textProperty().addListener((observable, oldValue, newValue) -> {
            filtrarLista(newValue);
        });
    }

    private void configurarListView() {
        listView.getItems().setAll(listaCompleta);

        // Define como cada item da lista será exibido
        listView.setCellFactory(lv -> new ListCell<T>() {
            @Override
            protected void updateItem(T item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : conversorTexto.apply(item));
            }
        });

        // Define o resultado ao dar duplo clique em um item
        listView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && listView.getSelectionModel().getSelectedItem() != null) {
                setResult(listView.getSelectionModel().getSelectedItem());
                close();
            }
        });
    }

    private void configurarBotoes() {
        ButtonType buttonTypeConfirmar = new ButtonType("Confirmar", ButtonBar.ButtonData.OK_DONE);
        getDialogPane().getButtonTypes().addAll(buttonTypeConfirmar, ButtonType.CANCEL);

        // Habilita o botão "Confirmar" apenas se um item estiver selecionado
        Node btnConfirmar = getDialogPane().lookupButton(buttonTypeConfirmar);
        btnConfirmar.setDisable(true);
        listView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            btnConfirmar.setDisable(newVal == null);
        });

        // Converte o resultado quando o botão "Confirmar" é pressionado
        setResultConverter(dialogButton -> {
            if (dialogButton == buttonTypeConfirmar) {
                return listView.getSelectionModel().getSelectedItem();
            }
            return null;
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
}