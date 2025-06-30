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
 * Class responsible for searching books in the database.
 * It provides methods to search by title, author, author and year, and within a specific library.
 * It interacts with the "Libri" and "Librerie" tables in the database.
 */
public class Ricerca {

    /**
     * Database connection used to interact with book data.
     */
    private Connection conn;
    
    /**
     * Constructor that initializes the Ricerca object with a database connection.
     * @param conn the database connection to be used for operations
     */
    public Ricerca (Connection conn) {
        this.conn = conn;
    }

    /**
     * Searches for books by title in the "Libri" table.
     * @param search the title or part of the title to search for
     * @return a list of book titles that match the search criteria
     */
    public List<String> cercaTitolo (String search) { 
        List<String> result = new ArrayList<>();
        String query = "SELECT \"Title\" FROM \"Libri\" WHERE LOWER(\"Title\") LIKE ?";

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, "%" + search.toLowerCase() + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                result.add(rs.getString("Title"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Searches for books by author in the "Libri" table.
     * @param search the author's name or part of it to search for
     * @return a list of book titles written by the specified author
     */
    public List<String> cercaAutore (String search) {
        List<String> result = new ArrayList<>(); 
        String query = "SELECT \"Title\" FROM \"Libri\" WHERE LOWER(\"Authors\") LIKE ?";
        
        try(PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, "%" + search.toLowerCase() + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                result.add(rs.getString("Title"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Searches for books by author and publication year in the "Libri" table.
     * @param search the author's name or part of it to search for
     * @param year the publication year to filter the results
     * @return a list of book titles written by the specified author and published in the given year
     */
    public List<String> cercaAutoreAnno (String search, int year) {
        List<String> result = new ArrayList<>();
        String query = "SELECT \"Title\" FROM \"Libri\" WHERE LOWER(\"Authors\") LIKE ? AND \"Pub_Year\" = ?";
    
        try(PreparedStatement  stmt = conn.prepareStatement(query)) {
                stmt.setString(1, "%" + search.toLowerCase() + "%");
                stmt.setInt(2, year);
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    result.add(rs.getString("Title")); 
                    }
            } catch (SQLException e) {
                e.printStackTrace();
        }  
        return result;
    }

    /**
     * Searches for books in a specific library by title.
     * @param search the title or part of the title to search for
     * @param user the ID of the user who owns the library
     * @param libName the name of the library to search within
     * @return a list of book titles that match the search criteria within the specified library
     */
    public List<String> searchLib (String search, String user, String libName) {
        List<String> result = new ArrayList<>();
        String query = """
            SELECT l.\"Title\"
            FROM \"Libri\" l
            JOIN \"Libri.Librerie\" ll ON l.\"id\" = ll.\"BookID\"
            JOIN \"Librerie\" lib ON ll.\"LibID\" = lib.\"id\"
            WHERE LOWER(l.\"Title\") LIKE ?
                AND lib.\"UserID\" = ?
                AND lib.\"Lib_Name\" = ?
        """;

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, "%" + search.toLowerCase() + "%");
            stmt.setString(2, user);
            stmt.setString(3, libName);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                result.add(rs.getString("Title"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
}