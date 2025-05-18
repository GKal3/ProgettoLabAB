package bookrecommender;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class HomeController2 extends HomeController {

    @FXML
    private TextField barra_di_ricerca;

    @FXML
    private ChoiceBox<String> filtri;

    private String[] opz = {"Cerca per Titolo", "Cerca per Autore", "Cerca per Autore e Anno"};

    @FXML
    private ComboBox<Integer> sceltaAnno;

    @FXML
    public void initialize () {
        filtri.getItems().addAll(opz);
        // Imposta un valore di default nella ChoiceBox se non c'è una selezione
        if (filtri.getValue() == null) {
            filtri.setValue(opz[2]); // Imposta "Cerca per Autore e Anno" come predefinito
        }
        // Aggiunge gli anni al ComboBox
        ObservableList<Integer> anni = FXCollections.observableArrayList();
        for (int i = 2024; i >= 1000; i--) {
            anni.add(i);
        }
        sceltaAnno.setItems(anni);
        
        // Impostiamo un valore predefinito
        sceltaAnno.getSelectionModel().selectFirst();  // Seleziona il primo anno (2024) per default

        // Aggiunta di un listener per rilevare la selezione del filtro
        filtri.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            // Controlla se l'utente ha selezionato "Cerca per Autore e Anno"
            if ("Cerca per Titolo".equals(newValue) | "Cerca per Autore".equals(newValue)) {
                // Cambio scena senza cliccare un altro pulsante
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/HomePage.fxml"));
                    Parent root = loader.load();

                    // Ottieni lo Stage corrente dalla barra di ricerca o dalla scena attuale
                    Stage stage = (Stage) filtri.getScene().getWindow();
                    
                    // Crea una nuova scena con il file caricato
                    Scene scene = new Scene(root);

                    // Imposta la nuova scena sullo stage
                    stage.setScene(scene);
                    stage.show(); // Mostra la nuova scena
                } catch (IOException e) {
                    e.printStackTrace();
                    // Gestisci l'eccezione, ad esempio mostrando un messaggio di errore
                }
            }
        });
    }

    // Metodo di ricerca che verrà invocato quando l'utente preme invio sulla barra di ricerca
    @FXML
    void cerca (ActionEvent event) {
        String ricerca = barra_di_ricerca.getText().trim().toLowerCase();
        Integer anno = sceltaAnno.getValue();
        List<String> lista = new ArrayList<>();
        Ricerca r = new Ricerca();

        lista = r.cercaAutoreAnno(ricerca, anno.toString());

        // Passa i risultati alla nuova scena
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ListaTrovato.fxml"));
            Parent root = loader.load();
            
            // Ottieni il controller della nuova finestra
            TrovatoController trovatoController = loader.getController();
            trovatoController.mostraRisultati(lista);
            trovatoController.setRicerca(ricerca);

            Stage stage = (Stage) barra_di_ricerca.getScene().getWindow();

            // Crea una nuova scena con il file caricato
            Scene scene = new Scene(root);

            // Imposta la nuova scena sullo stage
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
