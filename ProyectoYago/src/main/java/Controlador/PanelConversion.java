package Controlador;

import Modelo.ModeloPanel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class PanelConversion implements Initializable {
    @FXML
    private TextField val_tv;
    private ModeloPanel mp;
    private CalculadoraConversiones calculadora; // Referencia a CalculadoraConversiones
    private Stage secondStage;

    public void confirmar(ActionEvent actionEvent) {
        // Obtener el texto del TextField
        String texto = val_tv.getText();
        // Establecer el texto en resultLabel de CalculadoraConversiones
        calculadora.setResultLabelText(texto);
        // Cerrar la ventana
        if (secondStage != null) { // Verificar si secondStage es nulo
            secondStage.close(); // Cerrar la ventana solo si secondStage no es nulo
        }
    }

    public void volver(ActionEvent actionEvent) {
        // Cerrar la ventana
        secondStage.close();
    }

    public void setStage(Stage stage) {
        this.secondStage = stage;
    }

    public void setModelo(ModeloPanel modelo) {
        this.mp = modelo;
        // Establecer el texto en el TextField si el modelo tiene un texto no nulo
        if (mp.getTexto() != null) {
            val_tv.setText(mp.getTexto());
        }
    }

    public String getTexto() {
        return val_tv.getText();
    }

    public void setCalculadora(CalculadoraConversiones calculadora) {
        this.calculadora = calculadora;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }
}