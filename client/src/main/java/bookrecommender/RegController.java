/**
 * Progetto laboratorio A: "BookRecommender", anno 2024-2025
 * @author Giulia Kalemi, Matricola 756143, sede di Como.
 * @author Chiara Leone, Matricola 759095, sede di Como.
 */
package bookrecommender;

import java.io.*;
import java.net.URL;
import java.nio.file.*;
import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
/**
 * Class Controller del file FXML associato alla schermata per la registrazione.
 * Permette ai nuovi utenti di registrarsi e accedere all'area riservata.
 */
public class RegController extends MainController {
    
    @FXML
    private TextField nome,  cognome, cf, address, id;
    @FXML
    private PasswordField pass;
    @FXML
    private Button fineReg;
    /** 
     * Percorso del file FXML che definisce la schermata "AreaRiservata" dell'applicazione.
     */
    private final URL linkAR = getClass().getResource("/fxml/AreaRiservata.fxml");
    /**
     * Percorso al file CSV contenente i dati degli utenti registrati.
     */
    private final Path file = Paths.get("src/main/resources/csv/UtentiRegistrati.dati.csv");
    /**
     * Registra un nuovo utente nel sistema, salvando i dati nel file CSV,
     * e apre la schermata dell'area riservata per l'utente appena registrato.
     * @param event l'evento generato dal click sul Button "fineReg".
     */
    @FXML
    void registrazione (ActionEvent event) {
        String utente =  '"' + nome.getText() + " " + cognome.getText() + '"' + ","
                        + '"' + cf.getText() + '"' + ","
                        + '"' + address.getText() + '"' + ","
                        + '"' + id.getText() + '"' + ","
                        + '"' + pass.getText() + '"';
        try (BufferedWriter wr = new BufferedWriter (new FileWriter(file.toFile(), true))) {
            wr.write(utente);
            wr.newLine();
        } catch (IOException e) {
            e.printStackTrace();
            
        }
        String nomeCom = nome.getText() + " " + cognome.getText();
        apriAreaRiservata(nomeCom, id.getText());
    }
    /**
     * Apre la schermata dell'area riservata per l'utente registrato.
     * @param nome il nome completo dell'utente registrato.
     * @param id l'ID dell'utente registrato.
     */
    @FXML
    void apriAreaRiservata (String nome, String id) {
        try {
            FXMLLoader loader = new FXMLLoader(linkAR);
            Parent root = loader.load();
            Stage stage = (Stage) fineReg.getScene().getWindow();
            Scene scene = new Scene(root);

            ARController arController = loader.getController();
            arController.setNome(nome);
            arController.setID(id);

            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}