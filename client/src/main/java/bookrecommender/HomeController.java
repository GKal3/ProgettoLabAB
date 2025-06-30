/**
 * Laboratory Project B: "BookRecommender", Academic Year 2025-2026.
 * @author Giulia Kalemi, 756143, Como.
 * @author Chiara Leone, 759095, Como.
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
 * Controller class for the FXML file associated with the "HomePage" screen.
 * Allows the user to search for books using various filters.
 */
public class HomeController extends MainController {

    @FXML
    private TextField searchBar;

    @FXML
    private ChoiceBox<String> filter;

    /**
     * Search filter options shown in the ChoiceBox.
     */
    private String[] opt = {"Search by Title", "Search by Author", "Search by Author and Year"};

    /**
     * Path to the FXML file for the "HomePage2" screen.
     */
    private final URL linkHome2 = getClass().getResource("/fxml/HomePage2.fxml");

    /**
     * Path to the FXML file for the "ListaTrovato" screen, used to display search results.
     */
    private final URL linkTrov = getClass().getResource("/fxml/ListaTrovato.fxml");

    /**
     * Initialization method for the screen.
     * Sets up the search filter options in the ChoiceBox and adds a listener to
     * switch the scene when the "Search by Author and Year" filter is selected.
     */
    @FXML
    public void initialize () {
        filter.getItems().addAll(opt);

        if (filter.getValue() == null) {
            filter.setValue(opt[0]);
        }

        filter.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if ("Search by Author and Year".equals(newValue)) {
                try {
                    FXMLLoader loader = new FXMLLoader(linkHome2);
                    Parent root = loader.load();
                    Stage stage = (Stage) filter.getScene().getWindow();
                    Scene scene = new Scene(root);
                    stage.setScene(scene);
                    
                    Home2Controller home2Controller = loader.getController();
                    home2Controller.setClientConnection(conn);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Handles the search action based on the input text and the selected filter.
     * Loads a new scene to display the search results.
     * @param event the user-generated event (e.g., button click).
     * @throws IOException if an error occurs while loading the FXML file.
     */
    @FXML
    void cercaLibro (ActionEvent event) throws IOException {
        String search = searchBar.getText().trim();
        List<String> list = new ArrayList<>();
        String select = filter.getValue();

        String command = "";
        switch (select) {
            case "Search by Title":
                command = "SEARCH_TITLE;" + search;
                break;
            case "Search by Author":
                command = "SEARCH_AUTHOR;" + search;
                break;
            default:
                break;
        }

        if (conn == null) {
            return;
        }
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
        trovatoController.setSearch(search);
    }
}