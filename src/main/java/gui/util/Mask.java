package gui.util;

import javafx.scene.control.TextField;

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

    private static void addMask(TextField textField, String mask) {
        // Define o prompt text com o formato da máscara
        textField.setPromptText(mask);

        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            String unmasked = newValue.replaceAll("[^0-9]", "");
            StringBuilder formatted = new StringBuilder();
            int unmaskedIndex = 0;

            for (char maskChar : mask.toCharArray()) {
                if (unmaskedIndex >= unmasked.length()) {
                    break;
                }
                if (maskChar == '#') {
                    formatted.append(unmasked.charAt(unmaskedIndex++));
                } else {
                    formatted.append(maskChar);
                }
            }

            if (!formatted.toString().equals(textField.getText())) {
                textField.setText(formatted.toString());
            }
        });
    }
}