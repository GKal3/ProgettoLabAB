/**
 * Laboratory Project B: "BookRecommender", Academic Year 2025-2026.
 * @author Giulia Kalemi, 756143, Como.
 * @author Chiara Leone, 759095, Como.
 */
package bookrecommender;

import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.util.*;
import org.controlsfx.control.Rating;

/**
 * Controller class for the FXML file associated with the "Valuta" screen.
 * Allows users to rate a book and add notes for each rating category.
 */
public class ValutaController extends MainController {
    
    @FXML
    private Button enter, done;

    @FXML
    private Label BookTit;

    @FXML
    private TextField styleNotes, contNotes, pleNotes, orNotes, edNotes;

    @FXML
    private Rating ratSty, ratCont, ratPle, ratOr, ratEd;
    
    private Scene areaRScene, libScene;

    private ARController arController;

    private LibController prevController;
    
    private String user, title;

    private List<TextField> noteFields;

    /**
     * Sets the scene for the reserved area.
     * @param scene the reserved area scene
     */
    public void setARScene (Scene scene) {
        this.areaRScene = scene;
    }

    /**
     * Sets the scene for the previous library view.
     * @param scene the library scene
     */
    public void setLibScene (Scene scene) {
        this.libScene = scene;
    }

    /**
     * Sets the controller for the reserved area.
     * @param controller the ARController instance
     */
    public void setARController (ARController controller) {
        this.arController = controller;
    }

    /**
     * Sets the controller for the previous library view.
     * @param controller the LibController instance
     */
    public void setPrevCont (LibController controller) {
        this.prevController = controller;
    }

    /**
     * Returns to the reserved area screen.
     * @param event the event triggered by clicking the "enter" button
     */
    @FXML
    void openAR (ActionEvent event) {
        if (areaRScene != null) {
            if (arController != null) {
                arController.setClientConnection(conn);
            }
            Stage stage = (Stage) enter.getScene().getWindow();
            stage.setScene(areaRScene);
        }
    }

    /**
     * Sets the ID of the authenticated user.
     * @param id the user ID
     */
    public void setID (String id) {
        user = id;
    }

    /**
     * Sets the title label with the current book title.
     * @param tit the book title passed from the previous screen
     */
    @FXML
    void setTitle (String tit) {
        this.title = tit;
        BookTit.setText(tit);
    }

    /**
     * Initializes the controller after the FXML file is loaded.
     * Adds listeners to text fields to limit input length to 256 characters.
     * Sets default rating values to 5.
     */
    @FXML
    public void initialize() {
        noteFields = Arrays.asList(styleNotes, contNotes, pleNotes, orNotes, edNotes);
        for (TextField noteField : noteFields) {
            noteField.textProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue.length() > 256) {
                    noteField.setText(oldValue);
                }
            });
        }
        ratSty.setRating(5);
        ratCont.setRating(5);
        ratPle.setRating(5);
        ratOr.setRating(5);
        ratEd.setRating(5);
    }

    /**
     * Submits the ratings and notes for the selected book,
     * and returns to the previous library scene
     * @param event the action event triggered by clicking the "done" button
     */
    @FXML
    void addRating (ActionEvent event) {
        int [] rate = new int [5];
        rate[0] = (int)ratSty.getRating();
        rate[1] = (int)ratCont.getRating();
        rate[2] = (int)ratPle.getRating();
        rate[3] = (int)ratOr.getRating();
        rate[4] = (int)ratEd.getRating();

        List<String> noteList = new ArrayList<>();
        for (TextField noteField : noteFields) {
            String noteText = noteField.getText().trim();
            if (!noteText.isEmpty()) {
                noteList.add(noteText);
            } else {
                noteList.add(null);
            }
        }
        
        String ans = null;
        try {
            conn.sendMessage("INS_RATE");      
            String[] userTit = { user, title }; 
            conn.sendObject(userTit);           
            conn.sendObject(rate);            
            conn.sendObject(noteList);
            ans = conn.receiveMessage();
            if ("VAL_INS".equalsIgnoreCase(ans)) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText(null);
                alert.setContentText("Rating submitted successfully!");
                alert.showAndWait();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Rating already exists. Unable to submit it.");
                alert.showAndWait();
            }
            if (libScene != null) {
                if (prevController != null) {
                    prevController.setClientConnection(conn);
                }
                Stage stage = (Stage) done.getScene().getWindow();
                stage.setScene(libScene);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}