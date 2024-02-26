package Controlador;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class CalculadoraCientifica implements Initializable {

    @FXML
    private TextField displayField;

    @FXML
    private TextField memoriaTV;

    private double numero1 = 0;
    private String operacion = "";
    private double memoria = 0;
    @FXML
    private VBox calculadoraC;

    @FXML
    private void addToDisplay(ActionEvent event) {
        String valor = ((Button) event.getSource()).getText();
        displayField.setText(displayField.getText() + valor);
    }

    @FXML
    private void processOperator(ActionEvent event) {
        String valor = ((Button) event.getSource()).getText();
        if (!valor.equals("=")) {
            if (!operacion.isEmpty()) {
                return;
            }
            operacion = valor;
            if (!displayField.getText().isEmpty()) {
                numero1 = Double.parseDouble(displayField.getText());
            }
            displayField.clear();
        } else {
            if (operacion.isEmpty() || displayField.getText().isEmpty()) {
                return;
            }
            double numero2 = Double.parseDouble(displayField.getText());
            double resultado = calcular(numero1, numero2, operacion);
            displayField.setText(String.valueOf(resultado));
            operacion = "";
        }
    }

    @FXML
    private void reset() {
        displayField.clear();
        operacion = "";
    }

    @FXML
    private void trigonometricFunction(ActionEvent event) {
        String valor = ((Button) event.getSource()).getText();
        double input = Double.parseDouble(displayField.getText());
        switch (valor) {
            case "sin":
                displayField.setText(String.valueOf(Math.sin(input)));
                break;
            case "cos":
                displayField.setText(String.valueOf(Math.cos(input)));
                break;
            case "tan":
                displayField.setText(String.valueOf(Math.tan(input)));
                break;
            default:
                // Operación desconocida
                break;
        }
    }

    @FXML
    private void exponentialFunction() {
        if (!displayField.getText().isEmpty()) {
            double input = Double.parseDouble(displayField.getText());
            double result = Math.exp(input);
            displayField.setText(String.valueOf(result));
        }
    }

    private double calcular(double numero1, double numero2, String operacion) {
        switch (operacion) {
            case "+":
                return numero1 + numero2;
            case "-":
                return numero1 - numero2;
            case "x":
                return numero1 * numero2;
            case "/":
                if (numero2 == 0) {
                    // División por cero
                    return Double.NaN;
                }
                return numero1 / numero2;
            default:
                return Double.NaN; // Operación desconocida
        }
    }

    @FXML
    private void addToMemory(ActionEvent event) {
        if (!displayField.getText().isEmpty()) {
            memoria += Double.parseDouble(displayField.getText());
        }
    }

    @FXML
    private void subtractFromMemory(ActionEvent event) {
        if (!displayField.getText().isEmpty()) {
            memoria -= Double.parseDouble(displayField.getText());
        }
    }

    @FXML
    private void MR(ActionEvent event) {
        memoriaTV.setText(String.valueOf(memoria));
    }

    @FXML
    private void MC(ActionEvent event) {
        memoria = 0;
        memoriaTV.setText("M vaciada");
    }

    @FXML
    private void D(ActionEvent event) {
        reset();
        memoria = 0;
        memoriaTV.clear();
    }

    @FXML
    public void calcN(ActionEvent actionEvent) throws IOException {
        VBox panel = FXMLLoader.load(getClass().getResource("/Vista/calculator.fxml"));
        calculadoraC.getChildren().setAll(panel);
        calculadoraC.getScene().getWindow().setWidth(310);
        calculadoraC.getScene().getWindow().setHeight(490);
    }

    @FXML
    public void calcC(ActionEvent actionEvent) throws IOException {
        VBox panel = FXMLLoader.load(getClass().getResource("/Vista/calculadora_conversiones.fxml"));
        calculadoraC.getChildren().setAll(panel);
        calculadoraC.getScene().getWindow().setWidth(310);
        calculadoraC.getScene().getWindow().setHeight(490);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}