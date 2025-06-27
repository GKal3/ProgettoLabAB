/**
 * Progetto laboratorio A: "BookRecommender", anno 2024-2025
 * @author Giulia Kalemi, Matricola 756143, sede di Como.
 * @author Chiara Leone, Matricola 759095, sede di Como.
 */
package bookrecommender;

import java.io.IOException;
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
    /**
     * Variabili di istanza utilizzate per gestire informazioni relative all'utente e alla libreria:
     * <ul>
     * <li><code>user</code>: ID dell'utente.</li>
     * <li><code>nomeTit</code>: Nome della libreria selezionata o creata.</li>
     * <li><code>selectTit</code>: Titolo del libro selezionato dall'utente.</li>
     * </ul>
     */
    private String user, nomeTit, selectTit;
    /**
     * Scena dell'area riservata a cui tornare.
     */
    private Scene areaRScene;
    /**
     * Imposta la scena da utilizzare per tornare all'area riservata.
     * @param scene la scena dell'area riservata.
     */
    public void setARScene(Scene scene) {
        this.areaRScene = scene;
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
        if (areaRScene != null) {
            Stage stage = (Stage) enter.getScene().getWindow();
            stage.setScene(areaRScene);
            stage.show();
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
        listaLibri.getItems().addAll(risultati);
    }
    /**
     * Metodo di inizializzazione della schermata.
     * Aggiunge un listener per rilevare la selezione di un libro nella lista.
     */
    @FXML
    void initialize() {
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
        /*
        if (selectTit != null && !selectTit.isEmpty()) {
            Librerie l = new Librerie();
            boolean add = l.registraLibreria(user, nomeTit, selectTit);
            if(add){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Successo");
                alert.setHeaderText("Libreria creata correttamente!");
                alert.setContentText("Aggiunta: " + selectTit);
                alert.showAndWait();
            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Successo");
                alert.setHeaderText("Titolo aggiunto correttamente");
                alert.showAndWait();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Attenzione");
            alert.setHeaderText("Nessun libro selezionato");
            alert.setContentText("Per favore seleziona un libro dalla lista.");
            alert.showAndWait();
        }
        */
        String result;
        try {
            if (selectTit != null && !selectTit.isEmpty()) {
                conn.sendMessage("REGISTRA_LIBRERIA;" + user + "," + nomeTit + "," + selectTit);
                result = conn.receiveMessage();
                if (result.equals("LIBRERIA_REGISTRATA")) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Successo");
                    alert.setHeaderText("Libreria creata correttamente!");
                    alert.setContentText("Aggiunta: " + selectTit);
                    alert.showAndWait();
                } else if (result.equals("LIBRERIA_AGGIORNATA")) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Successo");
                    alert.setHeaderText("Titolo aggiunto correttamente");
                    alert.showAndWait();
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Errore");
                    alert.setHeaderText("Errore durante l'aggiunta del libro");
                    alert.setContentText(result);
                    alert.showAndWait();
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Attenzione");
                alert.setHeaderText("Nessun libro selezionato");
                alert.setContentText("Per favore seleziona un libro dalla lista.");
                alert.showAndWait();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
