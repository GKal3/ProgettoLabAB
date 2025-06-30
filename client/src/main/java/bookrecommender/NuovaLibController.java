/**
 * Laboratory Project B: "BookRecommender", Academic Year 2025-2026.
 * @author Giulia Kalemi, 756143, Como.
 * @author Chiara Leone, 759095, Como.
 */
package bookrecommender;

import java.io.*;
import java.net.URL;
import java.util.*;
import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

/**
 * ontroller class for the FXML file associated with the "NuovaLib" screen.
 * Allows the user to define the title of the library they want to create.
 */
public class NuovaLibController extends MainController {
    
    @FXML
    private Button enter, create;

    @FXML
    private TextField libName;

    private Scene areaRScene;

    private ARController arController;
    
    private String user, name;

    /**
     * Path to the FXML file for the "NuovaLib2" screen.
     */
    private final URL linkNew2 = getClass().getResource("/fxml/NuovaLib2.fxml");

    /**
     * Sets the authenticated user's ID.
     * @param id the user ID
     */
    public void setID (String id) {
        user = id;
    }

    /**
     * Sets the full name of the user.
     * @param ns the raw full name string
     */
    public void setName (String ns) {
        name = ns.replace("\"", "");
    }

    /**
     * Stores the previous scene for future navigation.
     * @param scene the previous scene
     */
    public void setPrevScene (Scene scene) {
        this.areaRScene = scene;
    }

    /**
     * Sets the reference to the previous controller (ARController).
     * @param controller the ARController instance
     */
    public void setARController(ARController controller) {
        this.arController = controller;
    }

    /**
     * Handles the button click event to open the reserved area.
     * @param event the button click event on "enter"
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
     * Handles the creation of a new library and book insertion.
     * <p>
     * If a library with the same name already exists, a warning is shown
     * and books will be added to that existing library.
     * Otherwise, it proceeds to the next screen to add titles to the new library.
     * </p>
     * @param event the button click event on "create"
     */
    @FXML
    void addTit (ActionEvent event) {
        try {
            conn.sendMessage("VIS_LIB_LIST;" + user);
            List<String> libList = conn.receiveList();
            if (libName.getText().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Invalid library name");
                alert.setContentText("Enter a valid name for the library.");
                alert.showAndWait();
                return;
            } else if (libList.stream().anyMatch(lib -> lib.equalsIgnoreCase(libName.getText()))) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Warning");
                alert.setHeaderText("Library already exists");
                alert.setContentText("Titles will be added to the already existing library.");
                alert.showAndWait();
            }
            FXMLLoader loader = new FXMLLoader(linkNew2);
            Parent root = loader.load();
            Stage stage = (Stage) libName.getScene().getWindow();
            
            NuovaLib2Controller nLib2Controller = loader.getController();
            nLib2Controller.setClientConnection(conn);
            nLib2Controller.setTit(libName.getText());
            nLib2Controller.setID(user);
            nLib2Controller.setName(name);

            Scene scene = new Scene(root);
            stage.setScene(scene);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}