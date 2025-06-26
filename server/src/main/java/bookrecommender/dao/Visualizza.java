/**
 * Class that manages the display of information related to books, including
 * book details, user ratings, suggestions for other books, and additional notes.
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

public class Visualizza {
	
 private Connection conn;
    
  public Visualizza(Connection conn) {
        this.conn = conn;
    }


    public int [] recapVal (String title) { //Utilizza tabella ValutazioniLibri con JOIN Libri
    int[] val = new int[7]; // Style, Content, Pleasantness, Originality, Edition, FinalVote, Count
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


    


     
    public List<String> recapSugg (String title) {  //usa tab consiglilibri/libri 
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
     * Restituisce le informazioni di base di un libro (autori, categoria, editore e anno di pubblicazione).
     * @param titolo il titolo del libro per il quale ottenere le informazioni.
     * @return un array di stringhe contenente le informazioni sul libro: "Autori", "Categoria", 
     *         "Editore" e "Anno di pubblicazione".
     */
// Restituisce le info base di un libro
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


    //Restituisce una lista di note fornite dagli utenti per un libro specificato.
     
public List<String> note(String title, String cat) {
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

    String query = String.format("""
        SELECT VL.\"UserID\", VL.%s
        FROM \"ValutazioniLibri\" VL
        JOIN \"Libri\" L ON VL.\"BookID\" = L.\"id\"
        WHERE LOWER(L.\"Title\") = LOWER(?)
    """, column);

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

}