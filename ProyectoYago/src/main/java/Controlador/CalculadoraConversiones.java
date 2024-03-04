package Controlador;

import Modelo.ModeloPanel;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class CalculadoraConversiones implements Initializable {

    @FXML
    private ComboBox<String> conversionTypeComboBox;
    @FXML
    private ComboBox<String> sourceValueComboBox;
    @FXML
    private ComboBox<String> targetValueComboBox;
    @FXML
    private TextField valueTextField;
    @FXML
    private Label resultLabel;
    @FXML
    private VBox calculadoraCN;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        conversionTypeComboBox.getItems().addAll("Longitud", "Temp");
        conversionTypeComboBox.setValue("Longitud");
        conversionTypeComboBox.setOnAction(event -> updateUnits());
        updateUnits();
    }

    @FXML
    private void updateUnits() {
        sourceValueComboBox.getItems().clear();
        targetValueComboBox.getItems().clear();

        try {
            String conversionType = conversionTypeComboBox.getValue();
            if (conversionType.equals("Longitud")) {
                sourceValueComboBox.getItems().addAll("Metros", "KM", "Millas");
                targetValueComboBox.getItems().addAll("Metros", "KM", "Millas");
            } else if (conversionType.equals("Temp")) {
                sourceValueComboBox.getItems().addAll("Celsius", "Fahrenheit");
                targetValueComboBox.getItems().addAll("Celsius", "Fahrenheit");
            }

            // Verificar si hay elementos en las listas antes de establecer sus valores predeterminados
            if (!sourceValueComboBox.getItems().isEmpty()) {
                sourceValueComboBox.setValue(sourceValueComboBox.getItems().get(0));
            }
            if (!targetValueComboBox.getItems().isEmpty()) {
                targetValueComboBox.setValue(targetValueComboBox.getItems().get(0));
            }
        } catch (NullPointerException e) {
        }
    }




    @FXML
    private void convert(ActionEvent event) {
        String fromUnit = sourceValueComboBox.getValue();
        String toUnit = targetValueComboBox.getValue();

        try {
            double value = Double.parseDouble(valueTextField.getText());
            double result;

            if (conversionTypeComboBox.getValue().equals("Longitud")) {
                result = convertLength(value, fromUnit, toUnit);
            } else {
                result = convertTemperature(value, fromUnit, toUnit);
            }
            resultLabel.setText(String.format("%.2f %s = %.2f %s", value, fromUnit, result, toUnit));
        } catch (NumberFormatException e) {
            resultLabel.setText("Por favor, ingrese un valor numérico.");
        }
    }

    private double convertLength(double value, String fromUnit, String toUnit) {
        double result = 0;
        switch (fromUnit) {
            case "Metros":
                switch (toUnit) {
                    case "Metros":
                        result = value;
                        break;
                    case "KM":
                        result = value / 1000;
                        break;
                    case "Millas":
                        result = value / 1609.34;
                        break;
                }
                break;
            case "KM":
                switch (toUnit) {
                    case "Metros":
                        result = value * 1000;
                        break;
                    case "KM":
                        result = value;
                        break;
                    case "Millas":
                        result = value * 0.621371;
                        break;
                }
                break;
            case "Millas":
                switch (toUnit) {
                    case "Metros":
                        result = value * 1609.34;
                        break;
                    case "KM":
                        result = value * 1.60934;
                        break;
                    case "Millas":
                        result = value;
                        break;
                }
                break;
        }
        return result;
    }

    private double convertTemperature(double value, String fromUnit, String toUnit) {
        double result = 0;
        if (fromUnit.equals("Celsius") && toUnit.equals("Fahrenheit")) {
            result = (value * 9 / 5) + 32;
        } else if (fromUnit.equals("Fahrenheit") && toUnit.equals("Celsius")) {
            result = (value - 32) * 5 / 9;
        } else {
            result = value;
        }
        return result;
    }

    @FXML
    public void addToDisplay(ActionEvent event) {
        String value = ((Button) event.getSource()).getText();
        valueTextField.setText(valueTextField.getText() + value);
    }

    @FXML
    public void editC(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Vista/panel_conversion.fxml"));
        Pane root = (Pane) loader.load();
        PanelConversion controlador = loader.getController();
        controlador.setCalculadora(this); // Pasar la referencia de CalculadoraConversiones
        Scene scene = new Scene(root);
        Stage stageWindow = new Stage();
        stageWindow.setTitle("Panel");
        stageWindow.initModality(Modality.APPLICATION_MODAL);
        stageWindow.setScene(scene);
        // Pasar el texto al PanelConversion
        String texto = resultLabel.getText(); // Obtener el texto del Label
        ModeloPanel mp = new ModeloPanel(texto);
        controlador.setModelo(mp);
        // Establecer el stageWindow en el controlador PanelConversion
        controlador.setStage(stageWindow);
        // Mostrar la ventana
        stageWindow.showAndWait();
        // Actualizar el Label con el texto modificado

        conversionTypeComboBox.getItems().clear();
        if (controlador.getCheckLong().isSelected() && controlador.getCheckTemp().isSelected()){
            conversionTypeComboBox.getItems().addAll("Longitud", "Temp");
            conversionTypeComboBox.setValue("Longitud");
            conversionTypeComboBox.setOnAction(event -> updateUnits());
            updateUnits();
        } else if (controlador.getCheckLong().isSelected()){
            conversionTypeComboBox.getItems().addAll("Longitud");
            conversionTypeComboBox.setValue("Longitud");
            conversionTypeComboBox.setOnAction(event -> updateUnits());
            updateUnits();
        } else if (controlador.getCheckTemp().isSelected()){
            conversionTypeComboBox.getItems().addAll("Temp");
            conversionTypeComboBox.setValue("Temp");
            conversionTypeComboBox.setOnAction(event -> updateUnits());
            updateUnits();
        }
    }

    public void setResultLabelText(String text) {
        resultLabel.setText(text);
    }


    @FXML
    public void calcN(ActionEvent actionEvent) throws IOException {
        VBox panel = FXMLLoader.load(getClass().getResource("/Vista/calculator.fxml"));
        calculadoraCN.getChildren().setAll(panel);
        calculadoraCN.getScene().getWindow().setWidth(310);
        calculadoraCN.getScene().getWindow().setHeight(490);
    }

    @FXML
    public void calcC(ActionEvent actionEvent) throws IOException {
        VBox panel = FXMLLoader.load(getClass().getResource("/Vista/calculadora_cientifica.fxml"));
        calculadoraCN.getChildren().setAll(panel);
        calculadoraCN.getScene().getWindow().setWidth(310);
        calculadoraCN.getScene().getWindow().setHeight(490);
    }
}