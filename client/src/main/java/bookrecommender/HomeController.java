/**
 * Progetto laboratorio A: "BookRecommender", anno 2024-2025
 * @author Giulia Kalemi, Matricola 756143, sede di Como.
 * @author Chiara Leone, Matricola 759095, sede di Como.
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
 * Classe Controller del file FXML associato alla schermata "HomePage".
 * Permette la ricerca di titoli (per diversi filtri di ricerca).
 */
public class HomeController extends MainController {

    @FXML
    private TextField barraRicerca;
    @FXML
    private ChoiceBox<String> filtri;
    /**
     * Opzioni disponibili per il filtro di ricerca.
     */
    private String[] opz = {"Cerca per Titolo", "Cerca per Autore", "Cerca per Autore e Anno"};
    /** 
     * Percorso del file FXML che definisce la schermata "HomePage2" dell'applicazione.
     */
    private final URL linkHome2 = getClass().getResource("/fxml/HomePage2.fxml");
    /** 
     * Percorso del file FXML che definisce la schermata "ListaTrovato" dell'applicazione.
     */
    private final URL linkTrov = getClass().getResource("/fxml/ListaTrovato.fxml");
    /**
     * Metodo di inizializzazione della schermata.
     * Configura i filtri di ricerca nella ChoiceBox e aggiunge un listener per gestire
     * il cambio di scena in base alla selezione del filtro.
     */
    @FXML
    public void initialize () {
        filtri.getItems().addAll(opz);

        if (filtri.getValue() == null) {
            filtri.setValue(opz[0]);
        }

        filtri.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if ("Cerca per Autore e Anno".equals(newValue)) {
                try {
                    FXMLLoader loader = new FXMLLoader(linkHome2);
                    Parent root = loader.load();
                    Stage stage = (Stage) filtri.getScene().getWindow();
                    Scene scene = new Scene(root);
                    stage.setScene(scene);
                    stage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    /**
     * Metodo per eseguire una ricerca in base al testo inserito nella barra di ricerca
     * e al filtro selezionato nella ChoiceBox (carica una nuova scena per mostrare i risultati).
     * @param event l'evento generato dall'utente (invio sulla tastiera).
     * @throws IOException se si verifica un errore durante il caricamento del file FXML.
     */
    @FXML
    void cercaLibro (ActionEvent event) throws IOException {
        String ricerca = barraRicerca.getText().trim();
        List<String> lista = new ArrayList<>();
        String select = filtri.getValue();

        // Costruisci il comando da inviare al server
        String command = "";
        switch (select) {
            case "Cerca per Titolo":
                command = "CERCA_TITOLO;" + ricerca;
                break;
            case "Cerca per Autore":
                command = "CERCA_AUTORE;" + ricerca;
                break;
            case "Cerca per Autore e Anno":
                command = "CERCA_AUTORE_ANNO;" + ricerca;
                break;
            default:
                break;
        }

        ClientConnection conn = new ClientConnection("localhost", 12345);
        conn.sendMessage(command);
        lista = conn.receiveList();
        conn.close();

        FXMLLoader loader = new FXMLLoader(linkTrov);
        Parent root = loader.load();
        Stage stage = (Stage) barraRicerca.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);

        TrovatoController trovatoController = loader.getController();
        trovatoController.mostraRisultati(lista);
        trovatoController.setRicerca(ricerca);
    }
}