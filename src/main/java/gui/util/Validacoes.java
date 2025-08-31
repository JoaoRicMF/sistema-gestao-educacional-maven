package gui.util;

import javafx.scene.control.TextField;

/**
 *
 * @author João Ricardo
 */
public class Validacoes {
    public static void addRequiredFieldValidator(TextField textField) {
        // Remove o estilo de erro assim que o usuário começa a digitar
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.isEmpty()) {
                textField.getStyleClass().remove("text-field-error");
            }
        });

        textField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            // Se o campo perdeu o foco (newValue is false)
            if (!newValue) {
                if (textField.getText() == null || textField.getText().trim().isEmpty()) {
                    textField.getStyleClass().add("text-field-error");
                } else {
                    textField.getStyleClass().remove("text-field-error");
                }
            }
        });
    }
}