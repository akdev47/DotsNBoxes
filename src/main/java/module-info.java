module com.example.dotsandboxes {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.ikonli.javafx;
    requires com.almasb.fxgl.all;

    opens com.example.dotsandboxes to javafx.fxml;
    opens BoardDrawingGame.src to javafx.graphics;
    exports com.example.dotsandboxes;
}