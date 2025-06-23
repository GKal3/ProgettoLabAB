/**
 * Progetto laboratorio A: "BookRecommender", anno 2024-2025
 * @author Giulia Kalemi, Matricola 756143, sede di Como.
 * @author Chiara Leone, Matricola 759095, sede di Como.
 */
package bookrecommender;

import java.io.*;
import java.net.URL;
import java.util.*;
import javafx.collections.*;
import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
/**
 * Classe Controller del file FXML associato alla schermata "HomePage2".
 * Impostata per la ricerca avente come filtri "autore" e "anno".
 */
public class Home2Controller extends HomeController {

    @FXML
    private TextField barraRicerca;
    @FXML
    private ChoiceBox<String> filtri;
    @FXML
    private ComboBox<Integer> sceltaAnno;
    /**
     * Opzioni disponibili per il filtro di ricerca.
     */
    private String[] opz = {"Cerca per Titolo", "Cerca per Autore", "Cerca per Autore e Anno"};
    /** 
     * Percorso del file FXML che definisce la schermata "HomePage" dell'applicazione.
     */
    private final URL linkHome = getClass().getResource("/fxml/HomePage.fxml");
    /** 
     * Percorso del file FXML che definisce la schermata "ListaTrovato" dell'applicazione.
     */
    private final URL linkTrov = getClass().getResource("/fxml/ListaTrovato.fxml");
    /**
     * Metodo di inizializzazione della schermata.
     */
    @FXML
    public void initialize () {
        filtri.getItems().addAll(opz);

        if (filtri.getValue() == null) {
            filtri.setValue(opz[2]);
        }
        
        ObservableList<Integer> anni = FXCollections.observableArrayList();
        for (int i = 2024; i >= 1000; i--) {
            anni.add(i);
        }
        sceltaAnno.setItems(anni);
        sceltaAnno.getSelectionModel().selectFirst();

        filtri.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if ("Cerca per Titolo".equals(newValue) | "Cerca per Autore".equals(newValue)) {
                try {
                    FXMLLoader loader = new FXMLLoader(linkHome);
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
        Integer anno = sceltaAnno.getValue();
        List<String> lista = new ArrayList<>();
        Ricerca r = new Ricerca();

        lista = r.cercaAutoreAnno(ricerca, anno.toString());

        FXMLLoader loader = new FXMLLoader(linkTrov);
        Parent root = loader.load();
        Stage stage = (Stage) barraRicerca.getScene().getWindow();
        
        TrovatoController trovatoController = loader.getController();
        trovatoController.mostraRisultati(lista);
        trovatoController.setRicerca(ricerca + ", " + anno.toString());

        Scene scene = new Scene(root);
        stage.setScene(scene);
    }
}
