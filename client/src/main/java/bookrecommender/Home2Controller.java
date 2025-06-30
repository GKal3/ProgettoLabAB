/**
 * Laboratory Project B: "BookRecommender", Academic Year 2025-2026.
 * @author Giulia Kalemi, 756143, Como.
 * @author Chiara Leone, 759095, Como.
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
 * Controller class for the FXML file associated with the "HomePage2" screen.
 * This version supports searching books by author and year.
 */
public class Home2Controller extends HomeController {

    @FXML
    private TextField searchBar;

    @FXML
    private ChoiceBox<String> filter;

    @FXML
    private ComboBox<Integer> year;
    
    /**
     * Search filter options shown in the ChoiceBox.
     */
    private String[] opt = {"Search by Title", "Search by Author", "Search by Author and Year"};
    
    /**
     * Path to the FXML file for the "HomePage" screen.
     */
    private final URL linkHome = getClass().getResource("/fxml/HomePage.fxml");
    
    /**
     * Path to the FXML file for the "ListaTrovato" screen, used to display search results.
     */
    private final URL linkTrov = getClass().getResource("/fxml/ListaTrovato.fxml");
    
    /**
     * Initializes the screen by setting up filter options and year selection,
     * and adding listeners for filter changes.
     */
    @FXML
    public void initialize () {
        filter.getItems().addAll(opt);

        if (filter.getValue() == null) {
            filter.setValue(opt[2]);
        }
        
        ObservableList<Integer> years = FXCollections.observableArrayList();
        for (int i = 2024; i >= 1000; i--) {
            years.add(i);
        }
        year.setItems(years);
        year.getSelectionModel().selectFirst();

        filter.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if ("Search by Title".equals(newValue) | "Search by Author".equals(newValue)) {
                try {
                    FXMLLoader loader = new FXMLLoader(linkHome);
                    Parent root = loader.load();
                    Stage stage = (Stage) filter.getScene().getWindow();
                    Scene scene = new Scene(root);
                    stage.setScene(scene);
                    HomeController homeController = loader.getController();
                    homeController.setClientConnection(conn);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    
    /**
     * Handles the search action based on user input.
     * Sends a request to the server and displays the results in a new scene.
     * @param event the event triggered by the user clicking the search button.
     * @throws IOException if an error occurs while loading the result screen.
     */
    @FXML
    void cercaLibro (ActionEvent event) throws IOException {
        String search = searchBar.getText().trim();
        Integer y = year.getValue();
        List<String> list = new ArrayList<>();
        
        String command = "SEARCH_AUT_Y;" + y + ";" + search;

        conn.sendMessage(command);
        list = conn.receiveList();

        FXMLLoader loader = new FXMLLoader(linkTrov);
        Parent root = loader.load();
        Stage stage = (Stage) searchBar.getScene().getWindow();

        Scene scene = new Scene(root);
        stage.setScene(scene);

        TrovatoController trovatoController = loader.getController();
        trovatoController.setClientConnection(conn);
        trovatoController.showRes(list);
        trovatoController.setSearch(search + ", " + y.toString());
    }
}
