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
import javafx.scene.input.*;
import javafx.stage.Stage;
import javafx.scene.layout.*;
import javafx.geometry.Pos;

/**
 * Controller class for the FXML file associated with the "Reserved Area" screen.
 * Allows the user to view their libraries and access the library creation screen.
 */
public class ARController extends MainController {

    @FXML
    private Label name;

    @FXML
    private ListView<String> listLib;

    @FXML
    private Button newLib;

    private Scene current;

    private String user, nameS;

    /** 
     * URL to the FXML file for the "Library" screen.
     */
    private final URL linkLib = getClass().getResource("/fxml/Libreria.fxml");
    
    /** 
     * URL to the FXML file for the "New Library" screen.
     */
    private final URL linkNew = getClass().getResource("/fxml/NuovaLib.fxml");
    
    /**
     * URL to the FXML file for the "Reserved Area" screen.
     */
    private final URL linkAR = getClass().getResource("/fxml/AreaRiservata.fxml");
    
    /**
     * Sets the user's full name to be displayed.
     * @param ns Full name of the user.
     */
    @FXML
    void setNome (String ns) {
        name.setText(ns.replace("\"", ""));
        nameS = ns.replace("\"", "");
    }

    /**
     * Sets the ID of the authenticated user.
     * @param id The user's ID.
     */
    @FXML
    void setID (String id) {
        user = id;
    }
    
    /**
     * Displays the list of libraries associated with the user.
     * @param titoliLib List of library titles.
     */
    public void mostraLib (List<String> titoliLib) {
        listLib.getItems().clear();
        listLib.getItems().addAll(titoliLib);
    }
    
    /**
     * Handles mouse click events on the library list.
     * On double-click, opens the selected library's detail screen.
     * @param event The mouse click event.
     */
    @FXML
    void apriLibreria(MouseEvent event) {
        if (event.getClickCount() == 2) {
            String lib = listLib.getSelectionModel().getSelectedItem();
            if (lib != null) {
                List<String> elem = new ArrayList<>();
                try {
                    conn.sendMessage("VISUALIZZA_LIBRERIA;" + user + "," + lib);
                    elem = conn.receiveList();
                    Stage stage = (Stage) listLib.getScene().getWindow();
                    current = stage.getScene();
                    FXMLLoader loader = new FXMLLoader(linkLib);
                    Parent root = loader.load();
                    Scene scene = new Scene(root);
                    stage.setScene(scene);

                    LibController libController = loader.getController();
                    libController.setClientConnection(conn);
                    libController.setTitLib(lib);
                    libController.setScenaPrec(current);
                    libController.setPrecController(this);
                    libController.mostraLibri(elem);
                    libController.setID(user);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Initializes the controller after the FXML file has been loaded.
     * Sets a custom cell factory for the list and configures the delete button behavior.
     */
    @FXML
    public void initialize() {
        listLib.setOnMouseClicked(this::apriLibreria);
        listLib.setCellFactory(lv -> new ListCell<>() {
            private final Button deleteButton = new Button();
            private final HBox hbox = new HBox();
            private final Label label = new Label();
            private final Region region = new Region();

            {
                hbox.setAlignment(Pos.CENTER_LEFT);
                deleteButton.getStyleClass().add("deleteButton");
                deleteButton.setOnAction(e -> {
                    String selectedLib = getItem();
                    if (selectedLib != null) {
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Do you really want to delete the library?", ButtonType.YES, ButtonType.NO);
                        alert.setHeaderText(null);
                        Optional<ButtonType> result = alert.showAndWait();
                        if (result.isPresent() && result.get() == ButtonType.YES) {
                            try {
                                conn.sendMessage("DEL_LIB;" + user + "," + selectedLib);
                                String ans = conn.receiveMessage();
                                if (ans.equals("DELETED")) {
                                    
                                    try {
                                        Stage stage = (Stage) listLib.getScene().getWindow();
                                        current = stage.getScene();
                                        FXMLLoader loader = new FXMLLoader(linkAR);
                                        Parent root = loader.load();
                                        Scene scene = new Scene(root);
                                        stage.setScene(scene);

                                        ARController arController = loader.getController();
                                        arController.setClientConnection(conn);
                                        arController.setID(user);
                                        arController.setNome(nameS);

                                        conn.sendMessage("VIS_LIB_LIST;" + user);
                                        List<String> titoliLib = conn.receiveList();
                                        arController.mostraLib(titoliLib);

                                    } catch (IOException ex) {
                                        ex.printStackTrace();
                                    }
                                    Alert successAlert = new Alert(Alert.AlertType.INFORMATION, "Library deleted successfully.");
                                    successAlert.setHeaderText(null);
                                    successAlert.showAndWait();
                                } else {
                                    Alert errorAlert = new Alert(Alert.AlertType.ERROR, "An error occurred while deleting the library.");
                                    errorAlert.setHeaderText(null);
                                    errorAlert.showAndWait();
                                }
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }
                        }
                    }
                    e.consume();
                });

                hbox.setSpacing(10);
                hbox.getChildren().addAll(label, region, deleteButton);
                HBox.setHgrow(region, Priority.ALWAYS);
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
     * Opens the screen to create a new library.
     * @param event The event triggered by clicking the "newLib" button.
     * @throws IOException If the FXML file cannot be loaded.
     */
    @FXML
    void apriNuovaLib (ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(linkNew);
        Parent root = loader.load();
        Stage stage = (Stage) newLib.getScene().getWindow();
        Scene scene = new Scene(root);
        
        current = stage.getScene();
        NuovaLibController nl = loader.getController();
        nl.setClientConnection(conn);
        nl.setScenaPrec(current);
        nl.setARController(this);
        nl.setID(user);
        nl.setName(nameS);

        stage.setScene(scene);
    }
}