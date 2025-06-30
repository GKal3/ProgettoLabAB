/**
 * Laboratory Project B: "BookRecommender", Academic Year 2025-2026.
 * @author Giulia Kalemi, 756143, Como.
 * @author Chiara Leone, 759095, Como.
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
 * Controller class for the FXML file associated with the "NuovaLib2" screen.
 * Allows the user to search for books and add them to a selected or newly created library.
 */
public class NuovaLib2Controller extends MainController {
    
    @FXML
    private TextField libName, searchBar;

    @FXML
    private Button enter, book, done;

    @FXML
    private ListView<String> BookList;

    /**
     * Path to the FXML file that defines the reserved area screen of the application.
     */
    private final URL linkAR = getClass().getResource("/fxml/AreaRiservata.fxml");

    private String user, name, libTit, selectTit;
    
    private Scene areaRScene;
    
    /**
     * Sets the full name of the user.
     * @param ns the raw full name string
     */
    public void setName (String ns) {
        name = ns.replace("\"", "");
    }

    /**
     * Sets the authenticated user's ID.
     * @param id the user ID
     */
    public void setID (String id) {
        user = id;
    }

    /**
     * Initializes the screen.
     * Adds a listener to track the currently selected book from the list.
     */
    @FXML
    void initialize() {
        done.setVisible(false);

        BookList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            selectTit = newValue;
        });
    }

    /**
     * Handles the action of opening the reserved area screen.
     * Retrieves the user's updated library list and sets it in the next controller.
     * @param event the button click event (pressing the "done" button)
     */
    @FXML
    void openAR (ActionEvent event) {
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
            arController.showLib(lib);
            arController.setName(name);
            arController.setID(user); 

            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets the name of the selected or created library and disables editing.
     * @param name the name of the library
     */
    public void setTit (String name) {
        libName.setText(name);
        libName.setDisable(true);
        this.libTit = name;
    }

    /**
     * Searches for books matching the entered text and displays results in the list.
     * @param event the Enter key press triggering the search
     */
    @FXML
    void cercaTitolo (ActionEvent event) {
        BookList.getItems().clear();
        String search = searchBar.getText().trim().toLowerCase();
        List<String> results = new ArrayList<>();
        try {
            conn.sendMessage("SEARCH_TITLE;" + search);
            results = conn.receiveList();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Select and add");
        alert.setHeaderText(null);
        alert.setContentText("Select a title and click the plus button to add it to your library.");
        alert.showAndWait();
        BookList.getItems().addAll(results);
    }

    /**
     * Adds the selected book to the user's library.
     * Displays success or error messages based on the server response.
     * @param event the button click event (pressing the "book" button)
     */
    @FXML
    void addBook (ActionEvent event) {
        String result;
        try {
            if (selectTit != null && !selectTit.isEmpty()) {
                conn.sendMessage("REG_LIB;" + user + "," + libTit + "," + selectTit);
                result = conn.receiveMessage();
                if (result.equals("LIBRERIA_REGISTRATA")) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Success");
                    alert.setHeaderText("Librery created successfully!");
                    alert.setContentText("Added: " + selectTit);
                    alert.showAndWait();
                    done.setVisible(true);
                } else if (result.equals("LIBRERIA_AGGIORNATA")) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Success");
                    alert.setHeaderText("Book added correctly!");
                    alert.showAndWait();
                    done.setVisible(true);
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
