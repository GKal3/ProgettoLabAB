/**
 * Progetto laboratorio A: "BookRecommender", anno 2024-2025
 * @author Giulia Kalemi, Matricola 756143, sede di Como.
 * @author Chiara Leone, Matricola 759095, sede di Como.
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
 * Class Controller del file FXML associato alla schermata per la registrazione.
 * Permette ai nuovi utenti di registrarsi e accedere all'area riservata.
 */
public class RegController extends MainController {
    
    @FXML
    private TextField nome,  cognome, cf, address, id;
    @FXML
    private PasswordField pass;
    @FXML
    private Button fineReg;
    /** 
     * Percorso del file FXML che definisce la schermata "AreaRiservata" dell'applicazione.
     */
    private final URL linkAR = getClass().getResource("/fxml/AreaRiservata.fxml");
   
   

   
    /**
     * Registra un nuovo utente nel sistema, salvando i dati nel file CSV,
     * e apre la schermata dell'area riservata per l'utente appena registrato.
     * @param event l'evento generato dal click sul Button "fineReg".
     */
    

    @FXML
    public void initialize() {
        addValidationListener(nome);
        addValidationListener(cognome);
        addValidationListener(cf);
        addValidationListener(address);
        addValidationListener(id);
        addValidationListener(pass);
    }

    private void addValidationListener (TextField field) {
        field.textProperty().addListener((obs, oldText, newText) -> {
            boolean valido = false;
            if (field == nome) {
                valido = isNomeValido(newText);
            } else if (field == cognome) {
                valido = isCognomeValido(newText);
            } else if (field == cf) {
                valido = isCFValido(newText);
            } else if (field == address) {
                valido = isEmailValida(newText);
            } else if (field == id) {
                valido = newText != null && newText.length() >= 5;
            } else if (field == pass) {
                valido = isPasswordValida(newText);
            }
            if (valido) {
                field.getStyleClass().removeAll("error");
            } else {
                if (!field.getStyleClass().contains("error")) {
                    field.getStyleClass().add("error");
                }
            }
        });
    }

    private boolean isNomeValido(String nome) {
        return nome != null && nome.matches("[A-Za-zàèéìòù' ]{2,}");
    }

    private boolean isCognomeValido(String cognome) {
        return cognome != null && cognome.matches("[A-Za-zàèéìòù' ]{2,}");
    }

    private boolean isCFValido(String cf) {
        return cf != null && cf.matches("[A-Z0-9]{16}");
    }

    private boolean isEmailValida(String email) {
        return email != null && email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$");
    }

    private boolean isPasswordValida(String pass) {
        return pass != null && pass.length() >= 8 && pass.matches(".*[A-Za-z].*") && pass.matches(".*\\d.*");
    }

    private boolean isValid() {
        String nomeCompleto = nome.getText() + " " + cognome.getText();
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

        return isNomeValido(nome.getText()) &&
            isCognomeValido(cognome.getText()) &&
            isCFValido(cf.getText()) &&
            isEmailValida(address.getText()) &&
            id.getText() != null && id.getText().length() >= 5 &&
            isPasswordValida(pass.getText());
    }





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

        String nomeCompleto = nome.getText() + " " + cognome.getText();
        String cfValue = cf.getText();
        String emailValue = address.getText();
        String idValue = id.getText();
        String passValue = pass.getText();

        try {
            conn.sendMessage("REG;" + nomeCompleto + "," + cfValue + "," + emailValue + "," + idValue + "," + passValue);
            apriAreaRiservata(nomeCompleto, idValue);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Apre la schermata dell'area riservata per l'utente registrato.
     * @param nome il nome completo dell'utente registrato.
     * @param id l'ID dell'utente registrato.
     */

    @FXML
    void apriAreaRiservata (String nome, String id) {
        try {
            FXMLLoader loader = new FXMLLoader(linkAR);
            Parent root = loader.load();
            Stage stage = (Stage) fineReg.getScene().getWindow();
            Scene scene = new Scene(root);

            ARController arController = loader.getController();
            arController.setClientConnection(conn);
            arController.setNome(nome);
            arController.setID(id); 

            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}