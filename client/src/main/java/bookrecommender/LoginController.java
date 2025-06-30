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
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * Controller class for the FXML file associated with the login screen.
 * Allows users to authenticate, access their private area,
 * or navigate to the registration screen for new users.
 */
public class LoginController extends MainController {
    
    @FXML
    private Button enter;

    @FXML
    private TextField id;

    @FXML
    private PasswordField pass;

    @FXML
    private Label reg;
   
    /**
     * Path to the FXML file that defines the "Reserved Area" screen.
     */
    private final URL linkAR = getClass().getResource("/fxml/AreaRiservata.fxml");

    /**
     * Path to the FXML file that defines the registration screen.
     */
    private final URL linkR = getClass().getResource("/fxml/Reg.fxml");
    
    /**
     * Handles the login process by checking the provided credentials.
     * If the credentials are valid, it calls the method to open the reserved area.
     * @param event event the event triggered by clicking the "enter" button.
     */
    @FXML
    void login (ActionEvent event) {
        try {
            if (id.getText().isEmpty() || pass.getText().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setHeaderText("Please fill in all fields");
                alert.showAndWait();
                return;
            }
            conn.sendMessage("LOGIN;" + id.getText() + "," + pass.getText());
            String [] data = conn.receiveInfo();
            if (data != null && data.length > 0 && !data[0].isEmpty()) {
                String name = data[0];
                openAR(event, name, id.getText());
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Warning");
                alert.setHeaderText("Invalid credentials");
                alert.showAndWait();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Opens the reserved area screen for the authenticated user.
     * @param event the event triggered by clicking the "enter" button.
     * @param name the name of the authenticated user.
     * @param id the ID of the authenticated user.
     */
    @FXML
    void openAR (ActionEvent event, String name, String id) {
        List<String> lib = libNames(id);
        try {
            FXMLLoader loader = new FXMLLoader(linkAR);
            Parent root = loader.load();
            Stage stage = (Stage) enter.getScene().getWindow();

            Scene scene = new Scene(root);
            ARController arController = loader.getController();
            arController.setClientConnection(conn);
            arController.showLib(lib);
            arController.setName(name);
            arController.setID(id); 

            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Retrieves the names of the personal libraries of a user based on their ID.
     * @param id the user ID.
     * @return a list containing the names of the user's libraries.
     */
    private List<String> libNames (String id) {
        List<String> lib = new ArrayList<>();
        try {
            conn.sendMessage("VIS_LIB_LIST;" + id);
            lib = conn.receiveList();
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>(); 
        }
        return lib;
    }

    /**
     * Opens the registration screen for new users.
     * @param event the event triggered by clicking the "reg" label.
     * @throws IOException if an error occurs while loading the FXML file.
     */
    @FXML
    void openReg (MouseEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(linkR);
        Parent root = loader.load();

        RegController regController = loader.getController();
        regController.setClientConnection(conn);

        Stage stage = (Stage) reg.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
    }
}