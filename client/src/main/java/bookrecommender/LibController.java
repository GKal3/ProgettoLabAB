/**
 * Laboratory Project B: "BookRecommender", Academic Year 2025-2026.
 * @author Giulia Kalemi, 756143, Como.
 * @author Chiara Leone, 759095, Como.
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
 * Controller class for the FXML file associated with the "Library" screen.
 * Manages the display of books within a specific library and allows users to add
 * ratings or suggestions for the books.
 */
public class LibController extends MainController{
    
    @FXML
    private Button enter;

    @FXML
    private ListView<String> listLib;

    @FXML
    private Label titLib;

    private String user;

    private Scene prev;

    private ARController prevController;

    /**
     * Path to the FXML file defining the "Rate" screen of the application.
     */
    private final URL linkVal = getClass().getResource("/fxml/Valuta.fxml");

    /**
     * Path to the FXML file defining the "Suggest" screen of the application.
     */
    private final URL linkSugg = getClass().getResource("/fxml/Sugg.fxml");
    
    /**
     * Sets the previous scene, to allow returning to it later.
     * @param scene the previous scene.
     */
    public void setPrevScene (Scene scene) {
        this.prev = scene;
    }

    /**
     * Sets the controller of the previous screen.
     * @param controller the previous screen's controller.
     */
    public void setPrevCont (ARController controller) {
        this.prevController = controller;
    }

    /**
     * Handles the action of clicking the "Enter" button to return to the reserved area.
     * @param event the event triggered by clicking the "Enter" button.
     */
    @FXML
    void openAR (ActionEvent event) {
        if (prev != null) {
            if (prevController != null) {
                prevController.setClientConnection(conn);
            }
            Stage stage = (Stage) enter.getScene().getWindow();
            stage.setScene(prev);
        }
    }

    /**
     * Sets the ID of the authenticated user.
     * @param id the user ID.
     */
    public void setID (String id) {
        user = id;
    }

    /**
     * Sets the text of the "titLib" label shown in the interface.
     * @param tit the name of the library (provided from the previous screen).
     */
    @FXML
    void setTitLib (String tit) {
        titLib.setText(tit);
    }

    /**
     * Displays the list of books in the library and sets up the buttons
     * to add ratings or suggestions for each book.
     * @param tit the list of book titles to be shown.
     */
    public void showBooks (List<String> tit) {
        listLib.getItems().clear();
        listLib.getItems().addAll(tit);

        ObservableList<String> bookList = FXCollections.observableArrayList(tit);

        listLib.setItems(bookList);

        listLib.setCellFactory(lv -> new ListCell<>() {
            private final Button butRat = new Button("Add Rating");
            private final Button butSugg = new Button("Add Suggestions");
            private final HBox hbox = new HBox();
            private final Label label = new Label();
            private final Region region = new Region();

            {
                hbox.setSpacing(40);
                hbox.setAlignment(Pos.CENTER_LEFT);
                HBox.setHgrow(region, Priority.ALWAYS);
                hbox.getChildren().addAll(label, region, butRat, butSugg);
                
                butRat.setOnAction(event -> {
                    String titSelect = getItem();
                    if (titSelect != null) {
                        apriValuta(titSelect);
                    }
                });

                butSugg.setOnAction(event -> {
                    String titSelect = getItem();
                    if (titSelect != null) {
                        apriSugg(titSelect);
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
     * Opens the "Rate" screen to allow the user to add a rating to the selected book.
     * @param tit the title of the selected book.
     */
    private void apriValuta (String tit) {
        try {
            FXMLLoader loader = new FXMLLoader(linkVal);
            Parent root = loader.load();

            ValutaController valutaController = loader.getController();
            valutaController.setClientConnection(conn);
            valutaController.setTitle(tit);
            valutaController.setID(user);
            valutaController.setLibScene(listLib.getScene());
            valutaController.setARScene(prev);
            valutaController.setPrevCont(this); 
            valutaController.setARController(prevController); 

            Stage stage = (Stage) listLib.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Opens the "Suggest" screen to allow the user to add a suggestion for the selected book.
     * @param tit the title of the selected book.
     */
    private void apriSugg (String tit) {
        try {
            FXMLLoader loader = new FXMLLoader(linkSugg);
            Parent root = loader.load();

            SuggController suggController = loader.getController();
            suggController.setClientConnection(conn);
            suggController.setTitle(tit);
            suggController.setID(user);
            suggController.setLibScene(listLib.getScene());
            suggController.setARScene(prev);
            suggController.setPrevCont(this);
            suggController.setARController(prevController);
            suggController.setLib(titLib.getText());

            Stage stage = (Stage) listLib.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}