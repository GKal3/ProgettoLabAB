/**
 * Progetto laboratorio A: "BookRecommender", anno 2024-2025
 * @author Giulia Kalemi, Matricola 756143, sede di Como.
 * @author Chiara Leone, Matricola 759095, sede di Como.
 */
package bookrecommender;

import java.io.*;
import java.net.URL;
import java.util.*;
import javafx.fxml.*;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
/**
 * Classe Controller del file FXML associato alla schermata "ListaTrovato".
 * Visualizza l'elenco dei risultati di ricerca (relativi alla schermata precedente).
 */
public class TrovatoController extends MainController {
    
    @FXML
    private ListView<String> listaLibri;
    @FXML
    private Label ricerca;
    /**
     * Campo in cui viene salvata la scena corrente, con l'intento di
     * passarla alla scena successiva in caso si voglia tornare indietro.
     */
    private Scene attuale;
    /** 
     * Percorso del file FXML che definisce la schermata "Libro" dell'applicazione.
     */
    private final URL linkLib = getClass().getResource("/fxml/Libro.fxml");
    /**
     * Visualizza i risultati della ricerca nella ListView.
     * @param titoliTrovati lista di titoli di libri trovati durante la ricerca (presa da HomeController o Home2Controller).
     */
    public void mostraRisultati (List<String> titoliTrovati) {
        listaLibri.getItems().clear();
        listaLibri.getItems().addAll(titoliTrovati);
    }
    /**
     * Imposta il testo della Label "ricerca" mostrato nell'interfaccia.
     * @param tit il termine di ricerca da mostrare (preso da HomeController o Home2Controller).
     */
    @FXML
    void setRicerca (String tit) {
        ricerca.setText('"' + tit + '"');
    }
    /**
     * Metodo di inizializzazione della schermata.
     * Aggiunge un listener per il click su un elemento della lista di libri.
     */
    @FXML
    public void initialize() {
        listaLibri.setOnMouseClicked(this::apriLibro);
    }
    /**
     * Gestisce l'azione di click del mouse su un elemento della lista.
     * Se l'utente effettua un doppio clic su un libro, viene caricata la schermata del libro selezionato.
     * @param event l'evento di click del mouse.
     */
    private void apriLibro (MouseEvent event) {
        String libro = listaLibri.getSelectionModel().getSelectedItem();

        if(event.getClickCount() == 2){
            if(libro != null){
                try {
                    Stage stage = (Stage) listaLibri.getScene().getWindow();
                    attuale = stage.getScene();
                    FXMLLoader loader = new FXMLLoader(linkLib);
                    Parent root = loader.load();
                    Scene scene = new Scene(root);
                    
                    LibroController libroController = loader.getController();
                    libroController.setClientConnection(conn);
                    libroController.visualizzaLibro(libro.replace("\"", ""));
                    libroController.setScenaPrec(attuale);
                    libroController.setPrecController(this); // Imposta il controller precedente

                    stage.setScene(scene);
                    stage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}