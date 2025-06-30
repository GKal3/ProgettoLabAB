/**
 * Laboratory Project B: "BookRecommender", Academic Year 2025-2026.
 * @author Giulia Kalemi, 756143, Como.
 * @author Chiara Leone, 759095, Como.
 */
package bookrecommender.dao;

import java.util.*;
import java.io.*;
import java.net.URL;
import java.nio.file.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Class responsible for displaying book information, including evaluations, suggestions, and notes.
 * It interacts with the "ValutazioniLibri", "ConsigliLibri", and "Libri" tables in the database.
 */
public class Visualizza {

    /**
     * Database connection used to interact with book data.
     */
	private Connection conn;
    
    /**
     * Constructor that initializes the Visualizza object with a database connection.
     * @param conn the database connection to be used for operations
     */
    public Visualizza(Connection conn) {
        this.conn = conn;
    }

    /**
     * Retrieves the average evaluation values for a book based on its title.
     * @param title the title of the book for which to retrieve evaluation values
     * @return an array of integers containing the average values for style, content, pleasantness,
     *         originality, edition, final vote, and the number of evaluations
     */
    public int [] recapVal (String title) { 
        int[] val = new int[7]; 
        int j = 0;
        
        String query ="""
            SELECT VL.\"Style\", VL.\"Content\", VL.\"Pleasantness\", VL.\"Originality\", VL.\"Edition\", VL.\"FinalVote\"
            FROM \"ValutazioniLibri\" VL
            JOIN \"Libri\" L ON VL.\"BookID\" = L.\"id\"
            WHERE LOWER(L.\"Title\") = LOWER(?)
        """;
        
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, title.replace("\"", ""));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    for (int i = 0; i < 6; i++) {
                        val[i] += rs.getInt(i + 1);
                    }
                    j++;
                }
            }
            if (j > 0) {
                for (int i = 0; i < 6; i++) {
                    val[i] = val[i] / j;
                }
                val[6] = j;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return val;
    }

    /**
     * Retrieves a list of book suggestions based on the title of a book.
     * @param title the title of the book for which to retrieve suggestions
     * @return a list of strings containing the titles of suggested books and the number of users who suggested them
     */
    public List<String> recapSugg (String title) {  
      List<String> sugg = new ArrayList<>();
        String query = """
            SELECT L2.\"Title\", COUNT(DISTINCT CL.\"UserID\") as nSugg
            FROM \"ConsigliLibri\" CL
            JOIN \"Libri\" L1 ON CL.\"BookID\" = L1.\"id\"
            JOIN \"Libri\" L2 ON CL.\"SuggID\" = L2.\"id\"
            WHERE LOWER(L1.\"Title\") = LOWER(?)
            GROUP BY L2.\"Title\"
        """;       

        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, title.replace("\"", ""));
            try (ResultSet rs = ps.executeQuery()) {
                boolean found = false;
                while (rs.next()) {
                    String tit = rs.getString("Title");
                    int nSugg = rs.getInt("nSugg");
                    sugg.add(tit + " (da " + nSugg + " utenti)");
                    found = true;
                }
                if (!found) {
                    sugg.add("Ancora nessun suggerimento");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sugg;
    }
       
    /**
     * Retrieves the basic information of a book (authors, year, etc)
     * @param title the title of the book for which to retrieve the information
     * @return an array of strings containing the information
     */
    public String[] infoLibro(String title) {
        String[] info = new String[4];
        String query = """
            SELECT \"Authors\", \"Category\", \"Publisher\", \"Pub_Year\"
            FROM \"Libri\"
            WHERE LOWER(\"Title\") = LOWER(?)
        """;
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, title.replace("\"", ""));
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    info[0] = rs.getString("Authors");
                    info[1] = rs.getString("Category");
                    info[2] = rs.getString("Publisher");
                    info[3] = rs.getString("Pub_Year");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return info;
    }

    /**
     * Retrieves notes for a book based on its title and category.
     * @param title the title of the book for which to retrieve notes
     * @param cat the category of notes to retrieve (style, content, pleasantness, originality, edition)
     * @return a list of strings containing the notes and the users who wrote them
     */
    public List<String> note (String title, String cat) {
        List<String> notes = new ArrayList<>();
        String column = null;

        switch (cat.toLowerCase()) {
            case "style":
                column = "Note_Style";
                break;
            case "content":
                column = "Note_Content";
                break;
            case "pleasantness":
                column = "Note_Pleasantness";
                break;
            case "originality":
                column = "Note_Originality";
                break;
            case "edition":
                column = "Note_Edition";
                break;
            default:
                notes.add("Categoria non valida");
                return notes;
        }

        String query =
            "SELECT VL.\"UserID\", VL.\"" + column + "\"" +
            " FROM \"ValutazioniLibri\" VL" +
            " JOIN \"Libri\" L ON VL.\"BookID\" = L.\"id\"" +
            " WHERE LOWER(L.\"Title\") = LOWER(?)";

        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, title.replace("\"", ""));
            try (ResultSet rs = ps.executeQuery()) {
                boolean found = false;
                while (rs.next()) {
                    String user = rs.getString("UserID");
                    String note = rs.getString(column);
                    if (note != null && !note.trim().isEmpty()) {
                        notes.add(note.trim() + " da @" + user);
                        found = true;
                    }
                }
                if (!found) {
                    notes.add("Ancora nessuna nota");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return notes;
    }

    /**
     * Retrieves a list of libraries associated with a user.
     * @param userID the ID of the user for whom to retrieve the library list
     * @return a list of library names, or a message indicating no libraries found
     */
    public List<String> libList (String userID) {
        String query = """
            SELECT \"Lib_Name\"
            FROM \"Librerie\"
            WHERE \"UserID\" = ?
        """;
        List<String> libList = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, userID);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String libName = rs.getString("Lib_Name");
                    if (libName != null && !libName.trim().isEmpty()) {
                        libList.add(libName.trim());
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (libList.isEmpty()) {
            libList.add("Nessuna libreria trovata");
        }
        return libList;
    }
}