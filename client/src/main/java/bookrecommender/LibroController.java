/**
 * Progetto laboratorio A: "BookRecommender", anno 2024-2025
 * @author Giulia Kalemi, Matricola 756143, sede di Como.
 * @author Chiara Leone, Matricola 759095, sede di Como.
 */
package bookrecommender;

import java.util.List;
import org.controlsfx.control.Rating;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.Scene;
import javafx.stage.Stage;
/**
 * Classe Controller del file FXML associato alla schermata "Libro".
 * Permette di visualizzare le informazioni relative a un libro:
 * titolo, autori, anno, editore, valutazioni e suggerimenti.
 */
public class LibroController extends MainController {

    @FXML
    private Label titolo, anno, autori, categoria, editore, utenti, note;
    @FXML
    private ListView<String> listaNote, listaSugg;
    @FXML
    private Rating valCont, valEd, valGrad, valOr, valStile, valTot;  
    @FXML
    private Button back;
    /**
     * Campo in cui viene salvata la scena precedente.
     */
    private Scene prec;
    /**
     * Imposta la scena precedente da utilizzare per tornare indietro.
     * @param scene la scena precedente.
     */
    public void setScenaPrec (Scene scene) {
        this.prec = scene;
    }
    /**
     * Metodo per tornare alla scena precedente.
     * @param event l'evento generato dall'utente con il click sul Button "back".
     */
    @FXML
    void indietro (ActionEvent event) {
        if (prec != null) {
            Stage stage = (Stage) back.getScene().getWindow();
            stage.setScene(prec);
            stage.show();
        }
    }
    /**
     * Mostra i suggerimenti relativi al libro sulla lista.
     * @param titoli lista dei titoli da visualizzare.
     */
    public void mostraSugg (List<String> titoli) {
        listaSugg.getItems().clear();
        listaSugg.getItems().addAll(titoli);
    }
    /**
     * Mostra le note degli utenti relative al libro sulla lista.
     * @param note Lista di note da visualizzare.
     */
    public void mostraNotes (List<String> note) {
        listaNote.getItems().clear();
        listaNote.getItems().addAll(note);
    }
    /**
     * Imposta i dettagli del libro (titolo, valutazioni, suggerimenti, note, etc).
     * @param libro il titolo del libro da visualizzare.
     */
    @FXML
    void visualizzaLibro (String libro) {
        titolo.setText(libro);

        Visualizza v = new Visualizza();
        int [] val = new int[7];
        val = v.recapVal(libro);
        String nUser = Integer.toString(val[6]);

        valStile.setRating(val[0]);
        valCont.setRating(val[1]);
        valGrad.setRating(val[2]);
        valOr.setRating(val[3]);
        valEd.setRating(val[4]);
        valTot.setRating(val[5]);

        valCont.setMouseTransparent(true);
        valEd.setMouseTransparent(true);
        valGrad.setMouseTransparent(true);
        valOr.setMouseTransparent(true);
        valStile.setMouseTransparent(true);
        valTot.setMouseTransparent(true);
        
        utenti.setText(nUser);

        List<String> sugg = v.recapSugg(libro);
        mostraSugg(sugg);

        List<String> notes = v.note(libro);
        mostraNotes(notes);

        String[] info = v.infoLibro(libro);
        autori.setText(info[0]);
        categoria.setText(info[1].trim());
        editore.setText(info[2]);
        anno.setText(info[3]);
    }
}