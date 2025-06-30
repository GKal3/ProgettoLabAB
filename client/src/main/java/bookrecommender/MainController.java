/**
 * Laboratory Project B: "BookRecommender", Academic Year 2025-2026.
 * @author Giulia Kalemi, 756143, Como.
 * @author Chiara Leone, 759095, Como.
 */
package bookrecommender;

import java.io.*;
import java.net.URL;
import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

/**
 * Controller class for the FXML file associated with the welcome screen.
 */
public class MainController {

    @FXML
    private Button home, access;

    /**
     * Path to the FXML file that defines the application's HomePage screen.
     */
    private final URL linkHome = getClass().getResource("/fxml/HomePage.fxml");

    /**
     * Path to the FXML file that defines the application's login screen.
     */
    private final URL linkLog = getClass().getResource("/fxml/Login.fxml");
    
    /**
     * The client-server connection object.
     * Declared as protected to be accessible from other controllers.
     */
    protected ClientConnection conn;

    /**
     * Sets the client-server connection, called from the main application (MainStart).
     * @param conn the client-server connection to be used in the controller.
     */
    public void setClientConnection(ClientConnection conn) {
        this.conn = conn;
    }

    /**
     * Handles the action of opening the HomePage screen.
     * @param event the event triggered by clicking the "home" button.
     * @throws IOException if an error occurs while loading the FXML file.
     */
    @FXML
    void openHomePage (ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(linkHome);
        Parent root = loader.load();

        HomeController homeController = loader.getController();
        homeController.setClientConnection(conn); 
        Stage stage = (Stage) home.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
    }

    /**
     * Handles the action of opening the Login screen.
     * @param event the event triggered by clicking the "access" button.
     * @throws IOException if an error occurs while loading the FXML file.
     */
    @FXML
    void openLogin (ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(linkLog);
        Parent root = loader.load();
        
        LoginController loginController = loader.getController();
        loginController.setClientConnection(conn); 

        Stage stage = (Stage) access.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
    }
}
