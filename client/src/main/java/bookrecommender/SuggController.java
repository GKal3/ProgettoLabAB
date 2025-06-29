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
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
/**
 * Classe Controller del file FXML associato alla schermata "Sugg".
 * Permette agli utenti di aggiungere fino a un massimo di 3 suggerimenti
 * per il titolo selezionato.
 */
public class SuggController extends MainController {
    
    @FXML
    private Button enter, fine, sugg;
    @FXML
    private TextField cerca;
    @FXML
    private ListView<String> listaLibri;
    @FXML
    private Label titolo;
    /**
     * Campi relativi a scene precedenti:
     * <ul>
     * <li><code>areaRScene</code>: L'area riservata a cui tornare.</li>
     * <li><code>libScene</code>: La libreria da cui si è arrivati (scena precedente).</li>
     * </ul>
     */
    private Scene areaRScene, libScene;

    private ARController arController;
    private LibController precController;
    /**
     * Variabili di istanza utilizzate per gestire informazioni relative all'utente e alla libreria:
     * <ul>
     * <li><code>user</code>: ID dell'utente.</li>
     * <li><code>tit</code>: Titolo del libro.</li>
     * <li><code>selectTit</code>: Titolo del suggerimento selezionato.</li>
     * </ul>
     */
    private String user, tit, selectTit, lib;
    /**
     * Imposta la scena da utilizzare per tornare all'area riservata.
     * @param scene la scena dell'area riservata.
     */
    public void setARScene (Scene scene) {
        this.areaRScene = scene;
    }
    /**
     * Imposta la scena precedente relativa alla libreria.
     * @param scene la scena della libreria.
     */
    public void setLibScene (Scene scene) {
        this.libScene = scene;
    }

    public void setARController (ARController controller) {
        this.arController = controller;
    }

    public void setPrecController (LibController controller) {
        this.precController = controller;
    }
    /**
     * Metodo per tornare all'area riservata.
     * @param event l'evento generato dall'utente con il click sul Button "enter"
     */
    @FXML
    void apriAreaRiservata (ActionEvent event) {
        if (areaRScene != null) {
            if (arController != null) {
                arController.setClientConnection(conn);
            }
            Stage stage = (Stage) enter.getScene().getWindow();
            stage.setScene(areaRScene);
        }
    }

    @FXML
    void backLib (ActionEvent event) {
        if (libScene != null) {
            if (precController != null) {
                precController.setClientConnection(conn);
            }
            Stage stage = (Stage) fine.getScene().getWindow();
            stage.setScene(libScene);
        }
    }
    /**
     * Imposta il testo della Label "titolo" mostrato nell'interfaccia.
     * @param tit1 il titolo preso dalla schermata precedente.
     */
    @FXML
    void setTitolo (String tit1) {
        this.tit = tit1;
        titolo.setText(tit1);
    }
    /**
     * Imposta l'ID dell'utente autenticato.
     * @param id l'ID dell'utente.
     */
    public void setID (String id) {
        user = id;
    }

    public void setLib (String libName) {
        lib = libName;
    }
    /**
     * Metodo di inizializzazione della schermata.
     * Configura un listener per aggiornare la variabile <code>selectTit</code>
     * in base all'elemento selezionato nella lista.
     */
    @FXML
    void initialize() {
        fine.setVisible(false);
        listaLibri.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            selectTit = newValue;
        });
    }
    /**
     * Aggiunge un suggerimento per il titolo selezionato.
     * <p>
     * Verifica che il titolo selezionato non coincida con quello in valutazione
     * e che non siano già presenti 3 suggerimenti (con relativi alert).
     * </p>
     * @param event l'evento generato dall'utente cliccando sul Button "sugg".
     */
    @FXML
    void addSugg (ActionEvent event) {
        if (selectTit != null && !selectTit.isEmpty()) {
            if (selectTit.equalsIgnoreCase(tit)) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setHeaderText("Unable to add suggestion");
                alert.setContentText("Please select a different book as a suggestion; it can't be the same");
                alert.showAndWait();
            } else {
                try {
                    conn.sendMessage("INS_SUGG;" + user + "," + tit + "," + selectTit);
                    String ans = conn.receiveMessage();
                    if(ans.equals("SUGG_INS")) {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Success");
                        alert.setHeaderText("Suggestion added correctly!");
                        alert.showAndWait();
                        fine.setVisible(true);
                    } else {
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle("Warning");
                        alert.setHeaderText("Unable to add suggestion");
                        alert.setContentText("This book already has 3 suggestions.");
                        alert.showAndWait();
                        fine.setVisible(false);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText("No book selected");
            alert.setContentText("Please select a book to add.");
            alert.showAndWait();
        }
    }
    /**
     * Metodo per eseguire una ricerca in base al testo inserito.
     * Visualizza i risultati nella "listaLibri".
     * @param event l'evento generato dall'utente (invio sulla tastiera).
     */
    @FXML
    void cercaTitolo(ActionEvent event) {
        listaLibri.getItems().clear();
        String testoRicerca = cerca.getText().trim().toLowerCase();
        List<String> risultati = new ArrayList<>();
        try {
            conn.sendMessage("CERCA_LIB;" + testoRicerca + "," + user + "," + lib);
            risultati = conn.receiveList();
        } catch (IOException e) {
            e.printStackTrace();
        }
        listaLibri.getItems().addAll(risultati);
    }
}