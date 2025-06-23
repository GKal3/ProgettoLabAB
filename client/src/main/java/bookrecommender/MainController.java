/**
 * Progetto laboratorio A: "BookRecommender", anno 2024-2025
 * @author Giulia Kalemi, Matricola 756143, sede di Como.
 * @author Chiara Leone, Matricola 759095, sede di Como.
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
 * Classe Controller del file FXML associato alla schermata di benvenuto.
 */
public class MainController {
    @FXML
    private Button home, access;
    /** 
     * Percorso del file FXML che definisce la schermata "HomePage" dell'applicazione.
     */
    private final URL linkHome = getClass().getResource("/fxml/HomePage.fxml");
    /** 
     * Percorso del file FXML che definisce la schermata di login dell'applicazione.
     */
    private final URL linkLog = getClass().getResource("/fxml/Login.fxml");
    
    private ClientConnection conn;
    /**
     * Permette di impostare la connessione dal MainStart.
     * @param conn la connessione client-server da usare nel controller
     */
    public void setClientConnection(ClientConnection conn) {
        this.conn = conn;
    }
    /**
     * Gestisce l'azione di apertura della schermata HomePage.
     * @param event l'evento generato dal clic sul pulsante "home".
     * @throws IOException se si verifica un errore durante il caricamento del file FXML.
     */
    @FXML
    void apriHomePage (ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(linkHome);
        Parent root = loader.load();
        Stage stage = (Stage) home.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    /**
     * Gestisce l'azione di apertura della schermata Login.
     * @param event l'evento generato dal clic sul pulsante "access".
     * @throws IOException se si verifica un errore durante il caricamento del file FXML.
     */
    @FXML
    void apriLogin (ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(linkLog);
        Parent root = loader.load();
        Stage stage = (Stage) access.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
