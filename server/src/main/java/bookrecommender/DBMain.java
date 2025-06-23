package bookrecommender;

import java.sql.Connection;
import java.sql.SQLException;

public class DBMain {
    public static void main(String[] args) {
        try (Connection conn = DBManager.connect()) {
            System.out.println("Connessione riuscita!");
        } catch (SQLException e) {
            System.err.println("Errore nella connessione: " + e.getMessage());
        }
    }
}
