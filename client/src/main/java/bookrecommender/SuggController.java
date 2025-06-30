/**
 * Laboratory Project B: "BookRecommender", Academic Year 2025-2026.
 * @author Giulia Kalemi, 756143, Como.
 * @author Chiara Leone, 759095, Como.
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
 * Controller class for the FXML file associated with the "Sugg" screen.
 * Allows users to add up to 3 suggestions for the selected book.
 */
public class SuggController extends MainController {
    
    @FXML
    private Button enter, done, sugg;

    @FXML
    private TextField searchBar;

    @FXML
    private ListView<String> BookList;

    @FXML
    private Label title;
    
    private Scene areaRScene, libScene;

    private ARController arController;

    private LibController prevController;
    
    private String user, tit, selectTit, lib;

    /**
     * Sets the scene to return to the reserved area.
     * @param scene the reserved area scene
     */
    public void setARScene (Scene scene) {
        this.areaRScene = scene;
    }

    /**
     * Sets the previous library scene.
     * @param scene the library scene
     */
    public void setLibScene (Scene scene) {
        this.libScene = scene;
    }

    /**
     * Sets the reference to the ARController controller.
     * @param controller the ARController instance
     */
    public void setARController (ARController controller) {
        this.arController = controller;
    }

    /**
     * Sets the reference to the previous controller
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
     * Returns to the library screen.
     * @param event the event triggered by clicking the "done" button
     */
    @FXML
    void backLib (ActionEvent event) {
        if (libScene != null) {
            if (prevController != null) {
                prevController.setClientConnection(conn);
            }
            Stage stage = (Stage) done.getScene().getWindow();
            stage.setScene(libScene);
        }
    }

    /**
     * Sets the title label with the current book title.
     * @param tit1 the book title passed from the previous screen
     */
    @FXML
    void setTitle (String tit1) {
        this.tit = tit1;
        title.setText(tit1);
    }

    /**
     * Sets the ID of the authenticated user.
     * @param id the user ID
     */
    public void setID (String id) {
        user = id;
    }

    /**
     * Sets the name of the library from which the suggestion request originates.
     * @param libName the name of the source library
     */
    public void setLib (String libName) {
        lib = libName;
    }

    /**
     * Initializes the screen.
     * Configures a listener on the list view to store the selected suggestion title.
     */
    @FXML
    void initialize() {
        done.setVisible(false);
        BookList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            selectTit = newValue;
        });
    }

    /**
     * Adds a suggestion for the currently selected title.
     * <p>
     * Validates that the selected title is different from the current one
     * and that the suggestion limit (3) has not been reached.
     * </p>
     * @param event the event triggered by clicking the "sugg" button
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
                        done.setVisible(true);
                    } else {
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle("Warning");
                        alert.setHeaderText("Unable to add suggestion");
                        alert.setContentText("This book already has 3 suggestions.");
                        alert.showAndWait();
                        done.setVisible(false);
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
     * Searches for titles matching the entered text.
     * Displays the results in the ListView.
     * @param event the event triggered when pressing Enter in the search field
     */
    @FXML
    void cercaTitolo (ActionEvent event) {
        BookList.getItems().clear();
        String search = searchBar.getText().trim().toLowerCase();
        List<String> results = new ArrayList<>();
        try {
            conn.sendMessage("SEARCH_LIB;" + search + "," + user + "," + lib);
            results = conn.receiveList();
        } catch (IOException e) {
            e.printStackTrace();
        }
        BookList.getItems().addAll(results);
    }
}