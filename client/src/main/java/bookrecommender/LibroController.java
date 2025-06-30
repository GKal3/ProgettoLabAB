/**
 * Laboratory Project B: "BookRecommender", Academic Year 2025-2026.
 * @author Giulia Kalemi, 756143, Como.
 * @author Chiara Leone, 759095, Como.
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
 * Controller class for the FXML file associated with the "Book" screen.
 * Displays information about a selected book, including title, authors,
 * year, publisher, ratings, and user suggestions/notes.
 */
public class LibroController extends MainController {

    @FXML
    private Label tit, year, aut, catL, editor, user, note;

    @FXML
    private ListView<String> styleNotes, contNotes, pleNotes, orNotes, edNotes, listaSugg;

    @FXML
    private Rating ratCont, ratEd, ratPle, ratOr, ratSty, ratTot;

    @FXML
    private Button back;
    
    private Scene prev;

    private TrovatoController prevController;

    /**
     * Sets the previous scene, used when going back.
     * @param scene the previous scene.
     */
    public void setPrevScene (Scene scene) {
        this.prev = scene;
    }

    /**
     * Sets the controller for the previous screen.
     * @param controller the previous controller.
     */
    public void setPrevCont (TrovatoController controller) {
        this.prevController = controller;
    }

    /**
     * Handles the back button click to return to the previous scene.
     * @param event the action event triggered by the user.
     */
    @FXML
    void goBack (ActionEvent event) {
        if (prev != null) {
            
            if (prevController != null) {
                prevController.setClientConnection(conn);
            }
            Stage stage = (Stage) back.getScene().getWindow();
            stage.setScene(prev);
        }
    }

    /**
     * Displays the list of suggestions related to the book.
     * @param tit the list of suggestion titles to display.
     */
    public void showSugg (List<String> tit) {
        listaSugg.getItems().clear();
        listaSugg.getItems().addAll(tit);
    }

    /**
     * Displays user notes related to a specific category of the book.
     * @param notes the list of notes to display.
     * @param cat  the category of the notes (e.g., style, content, etc.).
     */
    public void showNotes(List<String> notes, String cat) {
        switch (cat.toLowerCase()) {
            case "style":
                styleNotes.getItems().clear();
                styleNotes.getItems().addAll(notes);
                break;
            case "content":
                contNotes.getItems().clear();
                contNotes.getItems().addAll(notes);
                break;
            case "pleasantness":
                pleNotes.getItems().clear();
                pleNotes.getItems().addAll(notes);
                break;
            case "originality":
                orNotes.getItems().clear();
                orNotes.getItems().addAll(notes);
                break;
            case "edition":
                edNotes.getItems().clear();
                edNotes.getItems().addAll(notes);
                break;
            default:
                break;
        }
    }

    /**
     * Loads and displays all details of a selected book.
     * @param book the title of the selected book.
     * @throws IOException if there is a communication error with the server.
     */
    @FXML
    void visualizzaLibro (String book) throws IOException{
        tit.setText(book);
        conn.sendMessage("VIS_RATE;" + book);
        
        int [] rate = new int[7];
        rate = conn.receiveRatings();
        String nUser = Integer.toString(rate[6]);

        ratSty.setRating(rate[0]);
        ratCont.setRating(rate[1]);
        ratPle.setRating(rate[2]);
        ratOr.setRating(rate[3]);
        ratEd.setRating(rate[4]);
        ratTot.setRating(rate[5]);

        ratCont.setMouseTransparent(true);
        ratEd.setMouseTransparent(true);
        ratPle.setMouseTransparent(true);
        ratOr.setMouseTransparent(true);
        ratSty.setMouseTransparent(true);
        ratTot.setMouseTransparent(true);
        
        user.setText(nUser);

        conn.sendMessage("VIS_SUGG;" + book);
        List<String> sugg = conn.receiveList();
        showSugg(sugg);

        
        conn.sendMessage("VIS_INFO;" + book);
        String[] info = conn.receiveInfo();
        aut.setText(info[0]);
        catL.setText(info[1].trim());
        editor.setText(info[2]);
        year.setText(info[3]);

        String[] cat = {"style", "content", "pleasantness", "originality", "edition"};
        for (String c : cat) {
            conn.sendMessage("VIS_NOTE;" + book + "," + c);
            List<String> notes = conn.receiveList();
            showNotes(notes, c);
        }
    }
}