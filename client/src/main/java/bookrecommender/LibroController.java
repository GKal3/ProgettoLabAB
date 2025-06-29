/**
 * Progetto laboratorio A: "BookRecommender", anno 2024-2025
 * @author Giulia Kalemi, Matricola 756143, sede di Como.
 * @author Chiara Leone, Matricola 759095, sede di Como.
 */
package bookrecommender;

import java.io.IOException;
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
    private ListView<String> styleNotes, contNotes, pleNotes, orNotes, edNotes, listaSugg;
    @FXML
    private Rating valCont, valEd, valGrad, valOr, valStile, valTot;  
    @FXML
    private Button back;
    /**
     * Campo in cui viene salvata la scena precedente.
     */
    private Scene prec;

    private TrovatoController precController;
    /**
     * Imposta la scena precedente da utilizzare per tornare indietro.
     * @param scene la scena precedente.
     */
    public void setScenaPrec (Scene scene) {
        this.prec = scene;
    }

    public void setPrecController(TrovatoController controller) {
        this.precController = controller;
    }

    /**
     * Metodo per tornare alla scena precedente.
     * @param event l'evento generato dall'utente con il click sul Button "back".
     */
    @FXML
    void indietro (ActionEvent event) {
        if (prec != null) {
            
            if (precController != null) {
                precController.setClientConnection(conn);
            }
            Stage stage = (Stage) back.getScene().getWindow();
            stage.setScene(prec);
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
    public void mostraNotes(List<String> note, String cat) {
        switch (cat.toLowerCase()) {
            case "style":
                styleNotes.getItems().clear();
                styleNotes.getItems().addAll(note);
                break;
            case "content":
                contNotes.getItems().clear();
                contNotes.getItems().addAll(note);
                break;
            case "pleasantness":
                pleNotes.getItems().clear();
                pleNotes.getItems().addAll(note);
                break;
            case "originality":
                orNotes.getItems().clear();
                orNotes.getItems().addAll(note);
                break;
            case "edition":
                edNotes.getItems().clear();
                edNotes.getItems().addAll(note);
                break;
            default:
                System.out.println("Categoria non riconosciuta: " + cat);
        }
    }
    /**
     * Imposta i dettagli del libro (titolo, valutazioni, suggerimenti, note, etc).
     * @param libro il titolo del libro da visualizzare.
     * @throws IOException 
     */
    @FXML
    void visualizzaLibro (String libro) throws IOException{
        titolo.setText(libro);
        conn.sendMessage("VISUALIZZA_VALUTAZIONI;" + libro);
        
        int [] val = new int[7];
        val = conn.receiveRatings();
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

        conn.sendMessage("VISUALIZZA_SUGGERIMENTI;" + libro);
        List<String> sugg = conn.receiveList();
        mostraSugg(sugg);

        
        conn.sendMessage("VISUALIZZA_INFO;" + libro);
        String[] info = conn.receiveInfo();
        autori.setText(info[0]);
        categoria.setText(info[1].trim());
        editore.setText(info[2]);
        anno.setText(info[3]);

        String[] cat = {"style", "content", "pleasantness", "originality", "edition"};
        for (String c : cat) {
            conn.sendMessage("VISUALIZZA_NOTE;" + libro + "," + c);
            List<String> notes = conn.receiveList();
            mostraNotes(notes, c);
        }
    }
}