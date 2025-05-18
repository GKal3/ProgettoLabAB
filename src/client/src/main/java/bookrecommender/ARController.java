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
import javafx.scene.input.*;
import javafx.stage.Stage;
/**
 * Classe Controller del file FXML associato alla schermata "AreaRiservata".
 * Permette di visualizzare le librerie associate all'utente e di accedere 
 * alla schermata per la creazione di nuove.
 */
public class ARController extends MainController {

    @FXML
    private Label nome;
    @FXML
    private ListView<String> listaLib;
    @FXML
    private Button newLib;
    /**
     * Campo in cui viene salvata la scena corrente.
     * <p>
     * L'intento è di passarla alla scena successiva in caso si voglia tornare indietro.
     * </p>
     */
    private Scene attuale;
    /**
     * Identificativo dell'utente autenticato.
     */
    private String user;
    /** 
     * Percorso del file FXML che definisce la schermata "Libreria" dell'applicazione.
     */
    private final URL linkLib = getClass().getResource("/fxml/Libreria.fxml");
    /** 
     * Percorso del file FXML che definisce la schermata "NuovaLib" dell'applicazione.
     */
    private final URL linkNew = getClass().getResource("/fxml/NuovaLib.fxml");
    /**
     * Imposta il nome (e cognome) dell'utente da visualizzare.
     * @param nomeCognome nome e cognome dell'utente.
     */
    @FXML
    void setNome (String nomeCognome) {
        nome.setText(nomeCognome.replace("\"", ""));
    }
    /**
     * Imposta l'ID dell'utente autenticato.
     * @param id l'ID dell'utente.
     */
    @FXML
    void setID (String id) {
        user = id;
    }
    /**
     * Mostra le librerie associate all'utente all'interno della lista.
     * @param titoliLib lista di titoli delle librerie.
     */
    public void mostraLib (List<String> titoliLib) {
        listaLib.getItems().clear();
        listaLib.getItems().addAll(titoliLib);
    }
    /**
     * Metodo di inizializzazione della schermata.
     * Aggiunge un listener per il click su un elemento della lista di librerie.
     */
    @FXML
    public void initialize() {
        listaLib.setOnMouseClicked(this::apriLibreria);
    }
    /**
     * Gestisce l'azione di click del mouse su un elemento della lista.
     * Se l'utente effettua un doppio clic, viene caricata la schermata della libreria selezionata.
     * @param event l'evento di click del mouse.
     */
    @FXML
    void apriLibreria (MouseEvent event) {
        String libreria = listaLib.getSelectionModel().getSelectedItem();
        Librerie l = new Librerie();
        List<String> lib = l.visLib(user, libreria);

        if(event.getClickCount() == 2){
            if(libreria != null){
                try {
                    Stage stage = (Stage) listaLib.getScene().getWindow();
                    attuale = stage.getScene();
                    FXMLLoader loader = new FXMLLoader(linkLib);
                    Parent root = loader.load();
                    Scene scene = new Scene(root);
                    stage.setScene(scene);
                    stage.show();

                    LibController libController = loader.getController();
                    libController.setTitLib(libreria);
                    libController.setScenaPrec(attuale);
                    libController.mostraLibri(lib);
                    libController.setID(user);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    /**
     * Apre la schermata per la creazione di una nuova libreria.
     * @param event l'evento scatenato dal click sul Button "newLib".
     * @throws IOException se il file FXML non può essere caricato.
     */
    @FXML
    void apriNuovaLib (ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(linkNew);
        Parent root = loader.load();
        Stage stage = (Stage) newLib.getScene().getWindow();
        Scene scene = new Scene(root);
        
        attuale = stage.getScene();
        NuovaLibController nl = loader.getController();
        nl.setScenaPrec(attuale);
        nl.setID(user);

        stage.setScene(scene);
        stage.show();
    }
}