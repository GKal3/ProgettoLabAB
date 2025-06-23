/**
 * Progetto laboratorio A: "BookRecommender", anno 2024-2025
 * @author Giulia Kalemi, Matricola 756143, sede di Como.
 * @author Chiara Leone, Matricola 759095, sede di Como.
 */
package bookrecommender;

import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.controlsfx.control.Rating;
/**
 * Classe Controller del file FXML associato alla schermata "Valuta".
 * Permette agli utenti di aggiungere valutazioni su diversi aspetti di un libro 
 * e di salvare eventuali note aggiuntive.
 */
public class ValutaController extends MainController {
    
    @FXML
    private Button enter, fatto;
    @FXML
    private Label titLibro;
    @FXML
    private TextField note;
    @FXML
    private Rating valStile, valCont, valGrad, valOr, valEd, valFin;
    /**
     * Campi relativi a scene precedenti:
     * <ul>
     * <li><code>areaRScene</code>: L'area riservata a cui tornare.</li>
     * <li><code>libScene</code>: La libreria da cui si è arrivati (scena precedente).</li>
     * </ul>
     */
    private Scene areaRScene, libScene;
    /**
     * Variabili di istanza utilizzate per gestire informazioni relative all'utente e al libro:
     * <ul>
     * <li><code>user</code>: ID dell'utente.</li>
     * <li><code>titolo</code>: Titolo del libro da valutare.</li>
     * </ul>
     */
    private String user, titolo;
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
    /**
     * Imposta l'ID dell'utente autenticato.
     * @param id l'ID dell'utente.
     */
    public void setID (String id) {
        user = id;
    }
    /**
     * Imposta il testo della Label "titLibro" mostrato nell'interfaccia.
     * @param tit il titolo preso dalla schermata precedente.
     */
    @FXML
    void setTitolo (String tit) {
        this.titolo = tit;
        titLibro.setText(tit);
    }
    /**
     * Metodo di inizializzazione della schermata.
     * Limita il numero massimo di caratteri del campo note a 256.
     */
    @FXML
    public void initialize() {
        note.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length() > 256) {
                note.setText(oldValue);
            }
        });
    }
    /**
     * Registra la valutazione del libro con i valori inseriti e torna alla scena della libreria.
     * @param event l'evento generato dall'utente premendo il pulsante "fatto".
     */
    @FXML
    void addValuta (ActionEvent event) {
        int [] val = new int [6];
        val[0] = (int)valStile.getRating();
        val[1] = (int)valCont.getRating();
        val[2] = (int)valGrad.getRating();
        val[3] = (int)valOr.getRating();
        val[4] = (int)valEd.getRating();
        val[5] = (int)valFin.getRating();

        Valutazione v = new Valutazione();
        v.inserisciValutazioneLibro(user, titolo, val, note.getText());

        if (libScene != null) {
            Stage stage = (Stage) fatto.getScene().getWindow();
            stage.setScene(libScene);
            stage.show();
        }
    }
}