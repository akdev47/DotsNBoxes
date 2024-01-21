module src {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.ikonli.javafx;
    requires com.almasb.fxgl.all;

    exports BoardDrawingGame.logic;
    opens BoardDrawingGame.logic to javafx.fxml, javafx.graphics;
    exports BoardDrawingGame.view;
    opens BoardDrawingGame.view to javafx.fxml, javafx.graphics;
    exports BoardDrawingGame.Entities;
    opens BoardDrawingGame.Entities to javafx.fxml, javafx.graphics;
}