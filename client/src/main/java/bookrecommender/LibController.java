/**
 * Progetto laboratorio A: "BookRecommender", anno 2024-2025
 * @author Giulia Kalemi, Matricola 756143, sede di Como.
 * @author Chiara Leone, Matricola 759095, sede di Como.
 */
package bookrecommender;

import java.io.*;
import java.net.URL;
import java.util.List;
import javafx.collections.*;
import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
/**
 * Classe Controller del file FXML associato alla schermata "Libreria".
 * Gestisce la visualizzazione dei libri presenti in una libreria specifica 
 * e consente di aggiungere valutazioni o suggerimenti per i libri.
 */
public class LibController extends MainController{
    
    @FXML
    private Button enter;
    @FXML
    private ListView<String> listaLib;
    @FXML
    private Label titLib;
    /**
     * Identificativo dell'utente autenticato.
     */
    private String user;
    /**
     * Campo in cui viene salvata la scena precedente.
     */
    private Scene prec;
    /** 
     * Percorso del file FXML che definisce la schermata "Valuta" dell'applicazione.
     */
    private final URL linkVal = getClass().getResource("/fxml/Valuta.fxml");
    /** 
     * Percorso del file FXML che definisce la schermata "Sugg" dell'applicazione.
     */
    private final URL linkSugg = getClass().getResource("/fxml/Sugg.fxml");

    private ARController precController;
    /**
     * Imposta la scena precedente da utilizzare per tornare indietro.
     * @param scene la scena precedente.
     */
    public void setScenaPrec (Scene scene) {
        this.prec = scene;
    }

    public void setPrecController(ARController controller) {
        this.precController = controller;
    }
    /**
     * Metodo per tornare alla scena precedente.
     * @param event l'evento generato dall'utente con il click sul Button "enter".
     */
    @FXML
    void apriAreaRiservata (ActionEvent event) {
        if (prec != null) {
            if (precController != null) {
                precController.setClientConnection(conn);
            }
            Stage stage = (Stage) enter.getScene().getWindow();
            stage.setScene(prec);
        }
    }
    /**
     * Imposta l'ID dell'utente autenticato.
     * @param id l'ID dell'utente.
     */
    public void setID (String id) {
        user = id;
    }
    /**
     * Imposta il testo della Label "titLib" mostrato nell'interfaccia.
     * @param tit il nome della libreria preso dalla schermata precedente.
     */
    @FXML
    void setTitLib (String tit) {
        titLib.setText(tit);
    }
    /**
     * Visualizza i libri nella libreria e configura i pulsanti "Aggiungi Valutazione" e "Aggiungi Suggerimento".
     * @param titoli lista dei titoli dei libri da mostrare.
     */
    public void mostraLibri (List<String> titoli) {
        listaLib.getItems().clear();
        listaLib.getItems().addAll(titoli);

        ObservableList<String> libriList = FXCollections.observableArrayList(titoli);

        listaLib.setItems(libriList);

        listaLib.setCellFactory(lv -> new ListCell<>() {
            private final Button butVal = new Button("Aggiungi Valutazione");
            private final Button butSugg = new Button("Aggiungi Suggerimento");
            private final HBox hbox = new HBox();
            private final Label label = new Label();
            private final Region region = new Region();

            {
                hbox.setSpacing(40);
                hbox.setAlignment(Pos.CENTER_LEFT);
                HBox.setHgrow(region, Priority.ALWAYS);
                hbox.getChildren().addAll(label, region, butVal, butSugg);
                
                butVal.setOnAction(event -> {
                    String titoloSelect = getItem();
                    if (titoloSelect != null) {
                        apriValuta(titoloSelect);
                    }
                });

                butSugg.setOnAction(event -> {
                    String titoloSelect = getItem();
                    if (titoloSelect != null) {
                        apriSugg(titoloSelect);
                    }
                });
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    label.setText(item);
                    setGraphic(hbox);
                }
            }
        });
    }
    /**
     * Apre la schermata "Valuta" per aggiungere una valutazione a un libro selezionato.
     * @param titolo il titolo del libro selezionato.
     */
    private void apriValuta (String titolo) {
        try {
            FXMLLoader loader = new FXMLLoader(linkVal);
            Parent root = loader.load();

            ValutaController valutaController = loader.getController();
            valutaController.setClientConnection(conn);
            valutaController.setTitolo(titolo);
            valutaController.setID(user);
            valutaController.setLibScene(listaLib.getScene());
            valutaController.setARScene(prec);
            valutaController.setPrecController(this); // Imposta il controller precedente
            valutaController.setARController(precController); // Imposta il controller dell'area ris

            Stage stage = (Stage) listaLib.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * Apre la schermata "Sugg" per aggiungere un suggerimento a un libro selezionato.
     * @param titolo il titolo del libro selezionato.
     */
    private void apriSugg (String titolo) {
        try {
            FXMLLoader loader = new FXMLLoader(linkSugg);
            Parent root = loader.load();

            SuggController suggController = loader.getController();
            suggController.setClientConnection(conn);
            suggController.setTitolo(titolo);
            suggController.setID(user);
            suggController.setLibScene(listaLib.getScene());
            suggController.setARScene(prec);
            suggController.setPrecController(this); // Imposta il controller precedente
            suggController.setARController(precController); // Imposta il controller dell'area riservata
            suggController.setLib(titLib.getText()); // Imposta il nome della libreria

            Stage stage = (Stage) listaLib.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}