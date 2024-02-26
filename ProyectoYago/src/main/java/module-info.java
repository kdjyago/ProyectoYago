module com.example.practica7_1 {
    requires javafx.controls;
    requires javafx.fxml;


    opens Controlador to javafx.fxml;
    exports Controlador;
}