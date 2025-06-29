/**
 * Progetto laboratorio A: "BookRecommender", anno 2024-2025
 * @author Giulia Kalemi, Matricola 756143, sede di Como.
 * @author Chiara Leone, Matricola 759095, sede di Como.
 */
package bookrecommender;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
/**
 * Classe Controller del file FXML associato alla schermata "NuovaLib2".
 * Permette di selezionare e aggiungere libri alla libreria selezionata o creata dall'utente.
 */
public class NuovaLib2Controller extends MainController {
    
    @FXML
    private TextField nomeLib, cerca;
    @FXML
    private Button enter, libri, fine;
    @FXML
    private ListView<String> listaLibri;


    private final URL linkAR = getClass().getResource("/fxml/AreaRiservata.fxml");
    /**
     * Variabili di istanza utilizzate per gestire informazioni relative all'utente e alla libreria:
     * <ul>
     * <li><code>user</code>: ID dell'utente.</li>
     * <li><code>nomeTit</code>: Nome della libreria selezionata o creata.</li>
     * <li><code>selectTit</code>: Titolo del libro selezionato dall'utente.</li>
     * </ul>
     */
    private String user, name, nomeTit, selectTit;
    /**
     * Scena dell'area riservata a cui tornare.
     */
    private Scene areaRScene;
    
    
    public void setName (String ns) {
        name = ns.replace("\"", "");
    }
    /**
     * Imposta l'ID dell'utente autenticato.
     * @param id l'ID dell'utente.
     */
    public void setID (String id) {
        user = id;
    }
    /**
     * Metodo per tornare all'area riservata.
     * @param event l'evento generato dall'utente con il click sul Button "enter", o "fine".
     */
    @FXML
    void apriAreaRiservata (ActionEvent event) {
        List<String> lib = new ArrayList<>();
        try {
            conn.sendMessage("VIS_LIB_LIST;" + user);
            lib = conn.receiveList();
            FXMLLoader loader = new FXMLLoader(linkAR);
            Parent root = loader.load();
            Stage stage = (Stage) enter.getScene().getWindow();

            Scene scene = new Scene(root);
            ARController arController = loader.getController();
            arController.setClientConnection(conn);
            arController.mostraLib(lib);
            arController.setNome(name);
            arController.setID(user); 

            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * Imposta il nome della libreria scelto nella schermata
     * precedente e lo disabilita per evitare modifiche.
     * @param name il nome della libreria da impostare.
     */
    public void setTit (String name) {
        nomeLib.setText(name);
        nomeLib.setDisable(true);
        this.nomeTit = name;
    }
    /**
     * Metodo per eseguire una ricerca in base al testo inserito.
     * Visualizza i risultati nella "listaLibri".
     * @param event l'evento generato dall'utente (invio sulla tastiera).
     */
    @FXML
    void cercaTitolo (ActionEvent event) {
        listaLibri.getItems().clear();
        String testoRicerca = cerca.getText().trim().toLowerCase();
        List<String> risultati = new ArrayList<>();
        try {
            conn.sendMessage("CERCA_TITOLO;" + testoRicerca);
            risultati = conn.receiveList();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Select and add");
        alert.setHeaderText(null);
        alert.setContentText("Select a title and click the plus button to add it to your library.");
        alert.showAndWait();
        listaLibri.getItems().addAll(risultati);
    }
    /**
     * Metodo di inizializzazione della schermata.
     * Aggiunge un listener per rilevare la selezione di un libro nella lista.
     */
    @FXML
    void initialize() {
        
        fine.setVisible(false);

        listaLibri.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            selectTit = newValue;
        });
    }
    /**
     * Aggiunge il libro selezionato alla libreria.
     * Mostra un messaggio di successo o di errore in base all'esito
     * dell'operazione.
     * @param event l'evento generato dal click sul Button "libri".
     */
    @FXML
    void addLib (ActionEvent event) {
        String result;
        try {
            if (selectTit != null && !selectTit.isEmpty()) {
                conn.sendMessage("REGISTRA_LIBRERIA;" + user + "," + nomeTit + "," + selectTit);
                result = conn.receiveMessage();
                if (result.equals("LIBRERIA_REGISTRATA")) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Success");
                    alert.setHeaderText("Librery created successfully!");
                    alert.setContentText("Added: " + selectTit);
                    alert.showAndWait();
                    fine.setVisible(true);
                } else if (result.equals("LIBRERIA_AGGIORNATA")) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Success");
                    alert.setHeaderText("Book added correctly!");
                    alert.showAndWait();
                    fine.setVisible(true);
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Error adding book");
                    alert.setContentText(result);
                    alert.showAndWait();
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setHeaderText("No book selected");
                alert.setContentText("Please select a book to add to the library.");
                alert.showAndWait();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
