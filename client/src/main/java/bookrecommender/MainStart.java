/**
 * Progetto laboratorio A: "BookRecommender", anno 2024-2025
 * @author Giulia Kalemi, Matricola 756143, sede di Como.
 * @author Chiara Leone, Matricola 759095, sede di Como.
 */
package bookrecommender;

import java.net.URL;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
/**
 *  Classe che si occupa dell'avvio e dell'inizializzazione della GUI.
 */
public class MainStart extends Application {
    /** 
     * Percorso del file FXML che definisce la schermata di benvenuto dell'applicazione.
     */
    private final URL linkBenv = getClass().getResource("/fxml/Benvenuto.fxml");
    /**
     * Metodo che si occupa dell'avvio dell'applicazione.
     * @param primaryStage lo stage principale dell'applicazione, fornito da JavaFX.
     * @throws Exception se si verifica un errore durante il caricamento del file FXML.
     */
    @Override
    public void start (Stage primaryStage) throws Exception {
        ClientConnection conn = new ClientConnection("localhost", 1024);

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(linkBenv);
        Scene scene = new Scene(loader.load());

        // Passa la connessione al controller
        Object controller = loader.getController();
        if (controller instanceof MainController) {
            ((MainController) controller).setClientConnection(conn);
        }

        primaryStage.setScene(scene);
        primaryStage.setTitle("BookRecommender");
        primaryStage.show();
    }
}
