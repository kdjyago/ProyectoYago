module com.example.practica7_1 {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.json;


    opens Controlador to javafx.fxml;
    exports Controlador;
}