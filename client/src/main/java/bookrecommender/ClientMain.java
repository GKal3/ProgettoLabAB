package bookrecommender;

import java.io.IOException;
import java.util.List;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ClientMain extends Application {
    private ClientConnection conn;

    @Override
    public void start(Stage primaryStage) {
        TextField usernameField = new TextField();
        PasswordField passwordField = new PasswordField();
        Button loginButton = new Button("Login");

        loginButton.setOnAction(e -> {
            try {
                conn = new ClientConnection("localhost", 12345);
                boolean logged = conn.login(usernameField.getText(), passwordField.getText());
                if (logged) {
                    System.out.println("Login riuscito");
                    List<String> libri = conn.getListaLibri();
                    libri.forEach(System.out::println);
                } else {
                    System.out.println("Login fallito");
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        VBox layout = new VBox(10, usernameField, passwordField, loginButton);
        primaryStage.setScene(new Scene(layout, 300, 200));
        primaryStage.show();
    }
}