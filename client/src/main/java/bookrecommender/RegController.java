/**
 * Laboratory Project B: "BookRecommender", Academic Year 2025-2026.
 * @author Giulia Kalemi, 756143, Como.
 * @author Chiara Leone, 759095, Como.
 */
package bookrecommender;

import java.io.*;
import java.net.URL;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

/**
 * Controller class for the FXML file associated with the "Reg" screen.
 * Allows new users to register and access the reserved area.
 */
public class RegController extends MainController {
    
    @FXML
    private TextField name, surname, cf, address, id;

    @FXML
    private PasswordField pass;

    @FXML
    private Button reg;

    /**
     * Path to the FXML file for the "Reserved Area" screen.
     */
    private final URL linkAR = getClass().getResource("/fxml/AreaRiservata.fxml");
   
    /**
     * Initializes the controller.
     * Adds input validation listeners to all relevant text fields.
     */
    @FXML
    public void initialize() {
        addValidationListener(name);
        addValidationListener(surname);
        addValidationListener(cf);
        addValidationListener(address);
        addValidationListener(id);
        addValidationListener(pass);
    }

    private boolean isNameValid(String n) {
        return n != null && n.matches("[A-Za-zàèéìòù' ]{2,}");
    }

    private boolean isSurValid(String sur) {
        return sur != null && sur.matches("[A-Za-zàèéìòù' ]{2,}");
    }

    private boolean isCFValid(String cf) {
        return cf != null && cf.matches("[A-Z0-9]{16}");
    }

    private boolean isEmailValid(String email) {
        return email != null && email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$");
    }

    private boolean isPasswordValid(String pass) {
        return pass != null && pass.length() >= 8 && pass.matches(".*[A-Za-z].*") && pass.matches(".*\\d.*");
    }

    /**
     * Checks whether all input fields are valid and if the user is not already registered.
     * @return true if all validations pass and the user is unique; false otherwise
     */
    private boolean isValid() {
        String cfValue = cf.getText();
        String emailValue = address.getText();
        String idValue = id.getText();

        try {
            conn.sendMessage("CHECK_REG;" + idValue + "," + cfValue + "," + emailValue);
            String response = conn.receiveMessage();

            if ("USER_EXISTS".equalsIgnoreCase(response)) {
                return false;
            }
            if ("EMAIL_EXISTS".equalsIgnoreCase(response)) {
                return false;
            }
            if ("CF_EXISTS".equalsIgnoreCase(response)) {
                return false;
            }
        } catch (IOException e) {
            return false;
        }

        return isNameValid(name.getText()) &&
            isSurValid(surname.getText()) &&
            isCFValid(cf.getText()) &&
            isEmailValid(address.getText()) &&
            id.getText() != null && id.getText().length() >= 5 &&
            isPasswordValid(pass.getText());
    }

    /**
     * Adds a listener to a text field that checks for valid input and
     * applies or removes the "error" CSS class accordingly.
     * @param field the input field to validate
     */
    private void addValidationListener (TextField field) {
        field.textProperty().addListener((obs, oldText, newText) -> {
            boolean valid = false;
            if (field == name) {
                valid = isNameValid(newText);
            } else if (field == surname) {
                valid = isSurValid(newText);
            } else if (field == cf) {
                valid = isCFValid(newText);
            } else if (field == address) {
                valid = isEmailValid(newText);
            } else if (field == id) {
                valid = newText != null && newText.length() >= 5;
            } else if (field == pass) {
                valid = isPasswordValid(newText);
            }
            if (valid) {
                field.getStyleClass().removeAll("error");
            } else {
                if (!field.getStyleClass().contains("error")) {
                    field.getStyleClass().add("error");
                }
            }
        });
    }

    /**
     * Handles the registration process by sending their data to the server and,
     * if successful, it leads to the reserved area screen.
     * @param event the event triggered by clicking the "reg" button
     */
    @FXML
    void registrazione(ActionEvent event) {
        if (!isValid()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Registration Error");
            alert.setHeaderText("Invalid Data");
            alert.setContentText("Please check the entered fields and try again.");
            alert.showAndWait();
            return;
        }

        String fullName = name.getText() + " " + surname.getText();
        String cfValue = cf.getText();
        String emailValue = address.getText();
        String idValue = id.getText();
        String passValue = pass.getText();

        try {
            conn.sendMessage("REG;" + fullName + "," + cfValue + "," + emailValue + "," + idValue + "," + passValue);
            openAR(fullName, idValue);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Opens the reserved area screen for the newly registered user.
     * @param name the full name of the registered user
     * @param id the user ID
     */
    @FXML
    void openAR (String nameS, String id) {
        try {
            FXMLLoader loader = new FXMLLoader(linkAR);
            Parent root = loader.load();
            Stage stage = (Stage) reg.getScene().getWindow();
            Scene scene = new Scene(root);

            ARController arController = loader.getController();
            arController.setClientConnection(conn);
            arController.setName(nameS);
            arController.setID(id); 

            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}