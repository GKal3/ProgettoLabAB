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
import javafx.stage.Stage;
/**
 * Classe Controller del file FXML associato alla schermata "NuovaLib".
 * Permette di impostare il titolo per la libreria che l'utente intende creare.
 */
public class NuovaLibController extends MainController {
    
    @FXML
    private Button enter, crea;
    @FXML
    private TextField nomeLib;
    /**
     * Campo in cui viene salvata la scena precedente.
     */
    private Scene prec;
    /**
     * Identificativo dell'utente autenticato.
     */
    private String user;
    /** 
     * Percorso del file FXML che definisce la schermata "NuovaLib2" dell'applicazione.
     */
    private final URL linkNew2 = getClass().getResource("/fxml/NuovaLib2.fxml");
    /**
     * Imposta l'ID dell'utente autenticato.
     * @param id l'ID dell'utente.
     */
    public void setID (String id) {
        user = id;
    }
    /**
     * Imposta la scena precedente da utilizzare per tornare indietro.
     * @param scene la scena precedente.
     */
    public void setScenaPrec (Scene scene) {
        this.prec = scene;
    }
    /**
     * Metodo per tornare alla scena precedente.
     * @param event l'evento generato dall'utente con il click sul Button "enter".
     */
    @FXML
    void apriAreaRiservata (ActionEvent event){
        if (prec != null) {
            Stage stage = (Stage) enter.getScene().getWindow();
            stage.setScene(prec);
        }
    }
    /**
     * Gestisce l'aggiunta di titoli a una libreria.
     * <p>
     * Se la libreria con il nome specificato esiste già, viene mostrato un messaggio 
     * informativo all'utente indicando che i titoli saranno aggiunti alla libreria esistente.
     * In caso contrario, viene caricata la schermata per aggiungere nuovi titoli ("NuovaLib2").
     * </p>
     * @param event l'evento generato dall'utente con il click sul Button "crea".
     */
    @FXML
    void addTit (ActionEvent event) {
        try {
            conn.sendMessage("VIS_LIB_LIST;" + user);
            List<String> libList = conn.receiveList();
            if (libList.contains(nomeLib.getText())) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Attenzione");
                alert.setHeaderText("Nome già in utilizzo");
                alert.setContentText("I titoli verranno aggiunti alla libreria pre-esistente.");
                alert.showAndWait();
            }
            FXMLLoader loader = new FXMLLoader(linkNew2);
            Parent root = loader.load();
            Stage stage = (Stage) nomeLib.getScene().getWindow();
            
            NuovaLib2Controller nLib2Controller = loader.getController();
            nLib2Controller.setClientConnection(conn);
            nLib2Controller.setTit(nomeLib.getText());
            nLib2Controller.setARScene(prec);
            nLib2Controller.setID(user);

            Scene scene = new Scene(root);
            stage.setScene(scene);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
