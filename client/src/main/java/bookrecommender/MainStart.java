/**
 * Laboratory Project B: "BookRecommender", Academic Year 2025-2026.
 * @author Giulia Kalemi, 756143, Como.
 * @author Chiara Leone, 759095, Como.
 */
package bookrecommender;

import java.net.URL;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *  Main class that starts the BookRecommender application.
 *  It initializes the JavaFX application and loads the welcome screen.
 */
public class MainStart extends Application {

    /**
     * Path to the FXML file that defines the welcome screen of the application.
     */
    private final URL linkWel = getClass().getResource("/fxml/Benvenuto.fxml");
    
    /**
     * Object used to manage the client-server communication.
     */
    private ClientConnection conn;

    /**
     * Launches the application.
     * @param primaryStage the main stage of the application, provided by JavaFX.
     * @throws Exception if an error occurs while loading the FXML file.
     */
    @Override
    public void start (Stage primaryStage) throws Exception {
        conn = new ClientConnection("localhost", 10001);

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(linkWel);
        Scene scene = new Scene(loader.load());
        
        MainController mainController = loader.getController();
        mainController.setClientConnection(conn); 

        primaryStage.setScene(scene);
        primaryStage.setTitle("BookRecommender");
        primaryStage.show();
    }

    /**
     * Stops the application and closes the client connection if it exists.
     * @throws Exception if an error occurs during application shutdown.
     */
    @Override
    public void stop() throws Exception {
        if (conn != null) {
            try {
                conn.close(); 
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        super.stop();
    }
}
