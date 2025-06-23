/**
 * Progetto laboratorio A: "BookRecommender", anno 2024-2025
 * @author Giulia Kalemi, Matricola 756143, sede di Como.
 * @author Chiara Leone, Matricola 759095, sede di Como.
 */
package bookrecommender;

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
    /**
     * Variabili di istanza utilizzate per gestire informazioni relative all'utente e alla libreria:
     * <ul>
     * <li><code>user</code>: ID dell'utente.</li>
     * <li><code>tit</code>: Titolo del libro.</li>
     * <li><code>selectTit</code>: Titolo del suggerimento selezionato.</li>
     * </ul>
     */
    private String user, tit, selectTit;
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
    /**
     * Metodo per tornare all'area riservata.
     * @param event l'evento generato dall'utente con il click sul Button "enter"
     */
    @FXML
    void apriAreaRiservata (ActionEvent event) {
        if (areaRScene != null) {
            Stage stage = (Stage) enter.getScene().getWindow();
            stage.setScene(areaRScene);
            stage.show();
        }
    }

    @FXML
    void backLib (ActionEvent event) {
        // Torna alla scena precedente
        if (libScene != null) {
            Stage stage = (Stage) fine.getScene().getWindow();
            stage.setScene(libScene);
            stage.show();
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
    /**
     * Metodo di inizializzazione della schermata.
     * Configura un listener per aggiornare la variabile <code>selectTit</code>
     * in base all'elemento selezionato nella lista.
     */
    @FXML
    void initialize() {
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
                alert.setTitle("Attenzione");
                alert.setHeaderText("Impossibile aggiungere suggerimento");
                alert.setContentText("Il titolo e il suggerimento non possono coincidere.");
                alert.showAndWait();
            } else {
                Valutazione v = new Valutazione();
                boolean tre = v.inserisciSuggerimentoLibri(user, tit, selectTit);
                if(tre){
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Successo");
                    alert.setHeaderText("Suggerimento aggiunto correttamente!");
                    alert.showAndWait();
                } else {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Attenzione");
                    alert.setHeaderText("Impossibile aggiungere suggerimento");
                    alert.setContentText("Sono stati rilevati già 3 suggerimenti per questo titolo.");
                    alert.showAndWait();
                }
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Attenzione");
            alert.setHeaderText("Nessun libro selezionato");
            alert.setContentText("Per favore seleziona un libro dalla lista.");
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
        Ricerca r = new Ricerca();
        listaLibri.getItems().clear();
        listaLibri.getItems().addAll(r.cercaTitolo(cerca.getText().trim().toLowerCase()));
    }
}