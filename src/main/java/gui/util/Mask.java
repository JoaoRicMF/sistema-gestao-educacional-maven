package gui.util;

import javafx.application.Platform;
import javafx.scene.control.TextField;

/**
 *
 * @author João Ricardo
 */

public class Mask {

    public static void addCPFMask(TextField textField) {
        addMask(textField, "###.###.###-##");
    }

    public static void addRGMask(TextField textField) {
        addMask(textField, "##.###.###-#");
    }

    public static void addDateMask(TextField textField) {
        addMask(textField, "##/##/####");
    }

    public static void addPhoneMask(TextField textField) {
        addMask(textField, "(##) #####-####");
    }

    public static void addCEPMask(TextField textField) {
        addMask(textField, "#####-###");
    }

    public static void addMatriculaMask(TextField textField) {
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null || newValue.isEmpty()) {
                return;
            }
            String digitsOnly = newValue.replaceAll("[^0-9]", "");

            if (digitsOnly.length() > 9) {
                digitsOnly = digitsOnly.substring(0, 9);
            }

            final String finalValue = digitsOnly;
            Platform.runLater(() -> {
                textField.setText(finalValue);
                textField.positionCaret(finalValue.length());
            });
        });
    }
    public static void addSemestreMask(TextField textField) {
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null || newValue.isEmpty()) {
                return;
            }
            String digitsOnly = newValue.replaceAll("[^0-9]", "");

            if (digitsOnly.length() > 2) {
                digitsOnly = digitsOnly.substring(0, 2);
            }

            final String finalValue = digitsOnly;
            Platform.runLater(() -> {
                textField.setText(finalValue);
                textField.positionCaret(finalValue.length());
            });
        });
    }
    public static void addNotaMask(TextField textField) {
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null || newValue.isEmpty()) return;

            String value = newValue.replace(",", ".");

            if (!value.matches("\\d*\\.?\\d*")) {
                value = oldValue;
            }

            try {
                if (!value.isEmpty() && !value.equals(".")) {
                    double nota = Double.parseDouble(value);
                    if (nota > 10.0) {
                        value = "10";
                    } else if (value.contains(".") && value.substring(value.indexOf(".")).length() > 3) {
                        value = value.substring(0, value.indexOf(".") + 3);
                    }
                }
            } catch (NumberFormatException e) {
                value = oldValue;
            }

            final String finalValue = value;
            Platform.runLater(() -> {
                textField.setText(finalValue);
                textField.positionCaret(finalValue.length());
            });
        });
    }

    private static void addMask(TextField textField, String mask) {
        textField.setPromptText(mask);
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) return;
            String unmasked = newValue.replaceAll("[^0-9]", "");
            StringBuilder formatted = new StringBuilder();
            int unmaskedIndex = 0;

            for (char maskChar : mask.toCharArray()) {
                if (unmaskedIndex >= unmasked.length()) break;
                if (maskChar == '#') {
                    formatted.append(unmasked.charAt(unmaskedIndex++));
                } else {
                    formatted.append(maskChar);
                }
            }

            final String finalValue = formatted.toString();
            Platform.runLater(() -> {
                textField.setText(finalValue);
                textField.positionCaret(finalValue.length());
            });
        });
    }
}