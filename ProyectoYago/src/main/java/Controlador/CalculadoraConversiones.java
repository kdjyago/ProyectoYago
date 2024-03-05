package Controlador;

import Modelo.ModeloPanel;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ResourceBundle;

public class CalculadoraConversiones implements Initializable {

    private final String API_BASE_URL = "https://www.frankfurter.app/";

    // Métodos de la API para obtener tasas de cambio
    private final String LATEST_RATES_ENDPOINT = "latest";

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
        conversionTypeComboBox.getItems().addAll("Longitud", "Temp", "Moneda");
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
            if (conversionType != null) {
                if (conversionType.equals("Longitud")) {
                    sourceValueComboBox.getItems().addAll("Metros", "KM", "Millas");
                    targetValueComboBox.getItems().addAll("Metros", "KM", "Millas");
                } else if (conversionType.equals("Temp")) {
                    sourceValueComboBox.getItems().addAll("Celsius", "Fahrenheit");
                    targetValueComboBox.getItems().addAll("Celsius", "Fahrenheit");
                } else if (conversionType.equals("Moneda")) {
                    // Realizar la solicitud HTTP para obtener las tasas de cambio
                    Task<Void> task = new Task<Void>() {
                        @Override
                        protected Void call() {
                            try {
                                JSONObject rates = getExchangeRates();
                                Platform.runLater(() -> {
                                    if (rates != null) {
                                        sourceValueComboBox.getItems().addAll(rates.keySet());
                                        targetValueComboBox.getItems().addAll(rates.keySet());
                                    }
                                });
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            return null;
                        }
                    };

                    new Thread(task).start();
                }
            }

            // Verificar si hay elementos en las listas antes de establecer sus valores predeterminados
            if (!sourceValueComboBox.getItems().isEmpty()) {
                sourceValueComboBox.setValue(sourceValueComboBox.getItems().get(0));
            }
            if (!targetValueComboBox.getItems().isEmpty()) {
                targetValueComboBox.setValue(targetValueComboBox.getItems().get(0));
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    // Método para obtener tasas de cambio de la API
    private JSONObject getExchangeRates() throws IOException {
        URL url = new URL(API_BASE_URL + LATEST_RATES_ENDPOINT);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();
            return new JSONObject(response.toString()).getJSONObject("rates");
        } else {
            System.err.println("Error al obtener tasas de cambio: " + responseCode);
            return null;
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
            } else if (conversionTypeComboBox.getValue().equals("Temp")) {
                result = convertTemperature(value, fromUnit, toUnit);
            } else {
                result = convertCurrency(value, fromUnit, toUnit);
            }
            resultLabel.setText(String.format("%.2f %s = %.2f %s", value, fromUnit, result, toUnit));
        } catch (NumberFormatException e) {
            resultLabel.setText("Por favor, ingrese un valor numérico.");
        }
    }

    // Métodos de conversión existentes para longitud y temperatura

    private double convertCurrency(double value, String fromCurrency, String toCurrency) {
        // Obtener tasas de cambio actualizadas
        try {
            JSONObject rates = getExchangeRates();
            if (rates != null) {
                // Verificar si las claves de moneda existen
                if (rates.has(fromCurrency) && rates.has(toCurrency)) {
                    double fromRate = rates.getDouble(fromCurrency);
                    double toRate = rates.getDouble(toCurrency);
                    return value * (toRate / fromRate);
                } else {
                    // Manejar el caso en que las claves de moneda no existan
                    throw new IOException("Las claves de moneda no existen.");
                }
            } else {
                throw new IOException("No se pudieron obtener las tasas de cambio.");
            }
        } catch (IOException e) {
            e.printStackTrace();
            return 0; // Manejar el error apropiadamente en tu aplicación
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
        if (controlador.getCheckLong().isSelected() && controlador.getCheckTemp().isSelected() && controlador.getCheckMoneda().isSelected()){
            conversionTypeComboBox.getItems().addAll("Longitud", "Temp", "Moneda");
            conversionTypeComboBox.setValue("Longitud");
            conversionTypeComboBox.setOnAction(event -> updateUnits());
            updateUnits();
        } else if (controlador.getCheckLong().isSelected() && controlador.getCheckTemp().isSelected()){
            conversionTypeComboBox.getItems().addAll("Longitud", "Temp");
            conversionTypeComboBox.setValue("Longitud");
            conversionTypeComboBox.setOnAction(event -> updateUnits());
            updateUnits();
        } else if (controlador.getCheckLong().isSelected() && controlador.getCheckMoneda().isSelected()){
            conversionTypeComboBox.getItems().addAll("Longitud", "Moneda");
            conversionTypeComboBox.setValue("Longitud");
            conversionTypeComboBox.setOnAction(event -> updateUnits());
            updateUnits();
        } else if (controlador.getCheckTemp().isSelected() && controlador.getCheckMoneda().isSelected()){
            conversionTypeComboBox.getItems().addAll("Temp", "Moneda");
            conversionTypeComboBox.setValue("Temp");
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
        } else if (controlador.getCheckMoneda().isSelected()){
            conversionTypeComboBox.getItems().addAll("Moneda");
            conversionTypeComboBox.setValue("Moneda");
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