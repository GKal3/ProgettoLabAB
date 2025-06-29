/**
 * Progetto laboratorio A: "BookRecommender", anno 2024-2025
 * @author Giulia Kalemi, Matricola 756143, sede di Como.
 * @author Chiara Leone, Matricola 759095, sede di Como.
 */
package bookrecommender;

import java.io.*;
import java.net.URL;
import java.util.*;
import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
/**
 * Classe Controller del file FXML associato alla schermata di login.
 * Permette agli utenti di autenticarsi, accedere all'area riservata
 * o alla schermata per registrarsi come nuovi utenti.
 */
public class LoginController extends MainController {
    
    @FXML
    private Button enter;
    @FXML
    private TextField id;
    @FXML
    private PasswordField pass;
    @FXML
    private Label reg;
   
    /** 
     * Percorso del file FXML che definisce la schermata "AreaRiservata" dell'applicazione.
     */
    private final URL linkAR = getClass().getResource("/fxml/AreaRiservata.fxml");
    /** 
     * Percorso del file FXML che definisce la schermata "Reg" dell'applicazione.
     */
    private final URL linkR = getClass().getResource("/fxml/Reg.fxml");
    /**
     * Effettua il login dell'utente confrontando le credenziali inserite
     * con quelle presenti nel file CSV degli utenti registrati.
     * @param event l'evento generato dal click sul Button "enter".
     */
    @FXML
    void login (ActionEvent event) {
        try {
            if (id.getText().isEmpty() || pass.getText().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setHeaderText("Please fill in all fields");
                alert.showAndWait();
                return;
            }
            conn.sendMessage("LOGIN;" + id.getText() + "," + pass.getText());
            String [] data = conn.receiveInfo();
            if (data != null && data.length > 0 && !data[0].isEmpty()) {
                String name = data[0];
                apriAreaRiservata(event, name, id.getText());
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Warning");
                alert.setHeaderText("Invalid credentials");
                alert.showAndWait();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * Apre la schermata dell'area riservata per l'utente autenticato.
     * @param event l'evento generato dal click sul pulsante "enter".
     * @param nome il nome dell'utente autenticato.
     * @param id l'ID dell'utente autenticato.
     */
    @FXML
    void apriAreaRiservata (ActionEvent event, String nome, String id) {
        List<String> lib = nomeLib(id);
        try {
            FXMLLoader loader = new FXMLLoader(linkAR);
            Parent root = loader.load();
            Stage stage = (Stage) enter.getScene().getWindow();

            Scene scene = new Scene(root);
            ARController arController = loader.getController();
            arController.setClientConnection(conn);
            arController.mostraLib(lib);
            arController.setNome(nome);
            arController.setID(id); 

            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * Recupera i nomi delle librerie personali di un utente dato il suo ID.
     * @param id l'ID dell'utente.
     * @return una lista contenente i nomi delle librerie.
     */
    private List<String> nomeLib (String id) {
        List<String> lib = new ArrayList<>();
        try {
            conn.sendMessage("VIS_LIB_LIST;" + id);
            lib = conn.receiveList();
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>(); 
        }
        return lib;
    }
    /**
     * Apre la schermata di registrazione per i nuovi utenti.
     * @param event l'evento generato dal click sull'etichetta "Registrati".
     * @throws IOException in caso di errore nel caricamento del file FXML.
     */
    @FXML
    void apriReg (MouseEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(linkR);
        Parent root = loader.load();

        RegController regController = loader.getController();
        regController.setClientConnection(conn);

        Stage stage = (Stage) reg.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
    }
}