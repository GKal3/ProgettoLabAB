/**
 * Laboratory Project B: "BookRecommender", Academic Year 2025-2026.
 * @author Giulia Kalemi, 756143, Como.
 * @author Chiara Leone, 759095, Como.
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
 * Controller class for the FXML file associated with the "Trovato" screen.
 * Displays the results of a book search and allows the user to select a book to view its details.
 */
public class TrovatoController extends MainController {
    
    @FXML
    private ListView<String> bookList;

    @FXML
    private Label search;
    
    private Scene current;
    
    /**
     * Path to the FXML file for the "Libro" screen.
     */
    private final URL linkLib = getClass().getResource("/fxml/Libro.fxml");

    /**
     * Displays the list of search results inside the ListView.
     * @param found a list of book titles matching the search criteria
     */
    public void showRes (List<String> found) {
        bookList.getItems().clear();
        bookList.getItems().addAll(found);
    }

    /**
     * Sets the search term label displayed at the top of the screen.
     * @param tit the search term to be displayed
     */
    @FXML
    void setSearch (String tit) {
        search.setText('"' + tit + '"');
    }

    /**
     * Initializes the controller after the FXML has been loaded.
     * Sets a mouse click listener for selecting items in the list.
     */
    @FXML
    public void initialize() {
        bookList.setOnMouseClicked(this::openBook);
    }

    /**
     * Handles the action triggered by a mouse click on a book in the list.
     * If a double-click is detected on a valid item, it loads the detailed book view.
     * @param event the mouse event associated with the click
     */
    private void openBook (MouseEvent event) {
        String book = bookList.getSelectionModel().getSelectedItem();

        if(event.getClickCount() == 2){
            if(book != null){
                try {
                    Stage stage = (Stage) bookList.getScene().getWindow();
                    current = stage.getScene();
                    FXMLLoader loader = new FXMLLoader(linkLib);
                    Parent root = loader.load();
                    Scene scene = new Scene(root);
                    
                    LibroController libroController = loader.getController();
                    libroController.setClientConnection(conn);
                    libroController.visualizzaLibro(book.replace("\"", ""));
                    libroController.setPrevScene(current);
                    libroController.setPrevCont(this); 

                    stage.setScene(scene);
                    stage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}