/**
 * Progetto laboratorio A: "BookRecommender", anno 2024-2025
 * @author Giulia Kalemi, Matricola 756143, sede di Como.
 * @author Chiara Leone, Matricola 759095, sede di Como.
 */
package bookrecommender;

import java.io.*;
import java.net.URL;
import java.nio.file.*;
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
   
   
   //METODO AL MOMENTO COMMENTATO REG "VECCHIO"
   
    /**
     * Registra un nuovo utente nel sistema, salvando i dati nel file CSV,
     * e apre la schermata dell'area riservata per l'utente appena registrato.
     * @param event l'evento generato dal click sul Button "fineReg".
     */
    /*@FXML
    void registrazione (ActionEvent event) {
      String [] data = {
           nome.getText() + " " + cognome.getText(), cf.getText(), address.getText(), id.getText(), pass.getText()
     };
        try {
            conn.sendMessage("REG;" + data[0] + "," + data[1] + "," + data[2] + "," + data[3] + "," + data[4]);
            apriAreaRiservata(data[0], data[3]);
        } catch (IOException e) {
            e.printStackTrace();
        }
    } */

@FXML
public void initialize() {
    addValidationListener(nome);
    addValidationListener(cognome);
    addValidationListener(cf);
    addValidationListener(address);
    addValidationListener(id);
    addValidationListener(pass);
}

private void addValidationListener(TextField field) {
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

//metodo al momento commentato 1
/*private void addValidationListener(TextField field) {
    field.textProperty().addListener((obs, oldText, newText) -> {
        if (isValid(newText)) {
            field.getStyleClass().removeAll("error");
        } else {
            if (!field.getStyleClass().contains("error")) {
                field.getStyleClass().add("error");
            }
        }
    });
}
*/
//metodo al momento commentato 1
// Metodo per validare singolo campo (usato dal listener)
/*private boolean isValid(String text) {
    return text != null && text.length() >= 2;
}

private boolean isValid() {
    String nomeCompleto = nome.getText() + " " + cognome.getText();
    String cfValue = cf.getText();
    String emailValue = address.getText();
    String idValue = id.getText();

    try {
        // Controllo lato server per username, CF, email
        conn.sendMessage("CHECK_REG;" + idValue + ";" + cfValue + ";" + emailValue);
        String response = conn.receiveMessage();

        if ("USER_EXISTS".equalsIgnoreCase(response)) {
            return false;
        }
        if ("CF_EMAIL_EXISTS".equalsIgnoreCase(response)) {
            return false;
        }
        if ("CF_EXISTS".equalsIgnoreCase(response)) {
            return false;
        }
    } catch (IOException e) {
        return false;
    }

    // Controlli locali (esempio: almeno 5 caratteri per ogni campo)
    return nome.getText() != null && nome.getText().length() >= 2 &&
           cognome.getText() != null && cognome.getText().length() >= 2 &&
           cf.getText() != null && cf.getText().length() >= 5 &&
           address.getText() != null && address.getText().length() >= 5 &&
           id.getText() != null && id.getText().length() >= 5 &&
           pass.getText() != null && pass.getText().length() >= 5;
}
*/


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
        conn.sendMessage("REG;" + idValue + ";" + cfValue + ";" + emailValue);
        String response = conn.receiveMessage();

        if ("USER_EXISTS".equalsIgnoreCase(response)) {
            return false;
        }
        if ("CF_EMAIL_EXISTS".equalsIgnoreCase(response)) {
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
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}