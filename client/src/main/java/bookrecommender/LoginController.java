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
     * URL del file CSV contenente i dati degli utenti registrati, recuperato tramite il class loader.
     */
    private final URL linkReg = getClass().getResource("/csv/UtentiRegistrati.dati.csv");
    /**
     * URL del file CSV contenente i dati delle librerie, recuperato tramite il class loader.
     */
    private final URL linkLib = getClass().getResource("/csv/Librerie.dati.csv");
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
        boolean risultato = false;
        try (BufferedReader read = new BufferedReader(new InputStreamReader(linkReg.openStream()))) {
            String riga;
            String nome = "";
            while ((riga = read.readLine()) != null) {
                String [] tipo = riga.split(",");
                
                if (tipo.length > 0) {
                    if (tipo[3].trim().replace("\"", "").equals(id.getText())
                            && tipo[4].trim().replace("\"", "").equals(new String(pass.getText()))) {
                        risultato = true;
                        nome = tipo[0];
                        break;
                    }
                }
            }
            if (risultato) {
                apriAreaRiservata(event, nome, id.getText());
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Attenzione");
                alert.setHeaderText("Credenziali errate");
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
            arController.mostraLib(lib);
            arController.setNome(nome);
            arController.setID(id); 

            stage.setScene(scene);
            stage.show();
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
        try (BufferedReader read = new BufferedReader(new InputStreamReader(linkLib.openStream()))) {
            String riga;
                while ((riga = read.readLine()) != null) {
                    String [] tipo = riga.split(",");
                    
                    if (tipo.length > 0 && id.equals(tipo[0].trim().replace("\"", ""))) {
                        lib.add(tipo[1].trim().replace("\"", ""));
                    }
                }
        } catch (IOException e) {
            e.printStackTrace();
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
        Stage stage = (Stage) reg.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}