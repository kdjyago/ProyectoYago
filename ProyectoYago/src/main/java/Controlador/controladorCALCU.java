package Controlador;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class controladorCALCU {

    @FXML
    private TextField displayField;

    private double num1 = 0;
    private String operator = "";
    private boolean start = true;
    private double memoryValue = 0;
    @FXML
    private VBox calculadoraN;

    @FXML
    public void addToDisplay(ActionEvent event) {
        String value = ((Button) event.getSource()).getText();
        displayField.setText(displayField.getText() + value);
    }

    @FXML
    public void reset(ActionEvent event) {
        displayField.setText("");
        operator = "";
        start = true;
    }

    @FXML
    public void processOperator(ActionEvent event) {
        String value = ((Button) event.getSource()).getText();

        if (!"=".equals(value)) {
            if ("+-".equals(value)) {
                // Cambiar el signo del número y actualizar el campo de texto
                if (!displayField.getText().isEmpty()) {
                    double number = Double.parseDouble(displayField.getText());
                    displayField.setText(String.valueOf(-number));
                }
                return;
            }

            if (!operator.isEmpty()) {
                return;
            }

            if (isValidOperator(value)) {
                operator = value;
                if (!displayField.getText().isEmpty()) {
                    num1 = Double.parseDouble(displayField.getText());
                } else {
                    return;
                }
                displayField.setText("");
            } else {
                return;
            }
        } else {
            if (operator.isEmpty()) {
                return;
            }
            if (!displayField.getText().isEmpty()) {
                double num2 = Double.parseDouble(displayField.getText());
                try {
                    double result = calculate(num1, num2, operator);
                    displayField.setText(String.valueOf(result));
                    operator = "";
                    start = true;
                } catch (IllegalArgumentException e) {
                    displayField.setText("Error: " + e.getMessage());
                }
            } else {
                return;
            }
        }
    }


    private boolean isValidOperator(String operator) {
        return operator.equals("+") || operator.equals("-") || operator.equals("x") || operator.equals("/");
    }

    @FXML
    public void addToMemory(ActionEvent event) {
        String text = displayField.getText();
        if (!text.isEmpty()) {
            memoryValue += Double.parseDouble(text);
            displayField.clear();
        }
    }

    @FXML
    public void subtractFromMemory(ActionEvent event) {
        String text = displayField.getText();
        if (!text.isEmpty()) {
            memoryValue -= Double.parseDouble(text);
            displayField.clear();
        }
    }

    @Deprecated
    public void recallMemory(ActionEvent event) {
        displayField.setText(String.valueOf(memoryValue));
    }

    @Deprecated
    public void clearMemory(ActionEvent event) {
        memoryValue = 0;
    }

    private double calculate(double num1, double num2, String operator) {
        switch (operator) {
            case "+":
                return num1 + num2;
            case "-":
                return num1 - num2;
            case "x":
                return num1 * num2;
            case "/":
                if (num2 == 0) {
                    throw new IllegalArgumentException("Cannot divide by zero");
                }
                return num1 / num2;
            default:
                throw new IllegalArgumentException("Invalid operator");
        }
    }

    @FXML
    public void calcCI(ActionEvent actionEvent) throws Exception {
        VBox panel = FXMLLoader.load(getClass().getResource("/Vista/calculadora_cientifica.fxml"));
        calculadoraN.getChildren().setAll(panel);
        calculadoraN.getScene().getWindow().setWidth(310);
        calculadoraN.getScene().getWindow().setHeight(490);
    }

    @FXML
    public void calcC(ActionEvent actionEvent) throws Exception {
        VBox panel = FXMLLoader.load(getClass().getResource("/Vista/calculadora_conversiones.fxml"));
        calculadoraN.getChildren().setAll(panel);
        calculadoraN.getScene().getWindow().setWidth(310);
        calculadoraN.getScene().getWindow().setHeight(490);
    }
}