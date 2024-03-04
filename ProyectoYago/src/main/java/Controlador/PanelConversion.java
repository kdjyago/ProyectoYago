package Controlador;

import Modelo.ModeloPanel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class PanelConversion implements Initializable {
    private ModeloPanel mp;
    private CalculadoraConversiones calculadora; // Referencia a CalculadoraConversiones
    private Stage secondStage;
    @FXML
    private Button confirmar_val_btn;
    @FXML
    private Button volver_val_btn;
    @FXML
    private CheckBox checkLong;
    @FXML
    private CheckBox checkTemp;

    @FXML
    public void confirmar(ActionEvent actionEvent) {
        // Establecer el texto en resultLabel de CalculadoraConversiones
        //calculadora.setResultLabelText(texto);
        // Cerrar la ventana
        if (secondStage != null) { // Verificar si secondStage es nulo
            secondStage.close(); // Cerrar la ventana solo si secondStage no es nulo
        }
    }

    @FXML
    public void volver(ActionEvent actionEvent) {
        // Cerrar la ventana
        secondStage.close();
    }

    public void setStage(Stage stage) {
        this.secondStage = stage;
    }

    public void setModelo(ModeloPanel modelo) {
        this.mp = modelo;
    }



    public void setCalculadora(CalculadoraConversiones calculadora) {
        this.calculadora = calculadora;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    public CheckBox getCheckLong() {
        return checkLong;
    }

    public CheckBox getCheckTemp() {
        return checkTemp;
    }
}