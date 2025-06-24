/**
 * Project lab B: "BookRecommender", year 2025-2026
 * @author Giulia Kalemi, 756143, Como.
 * @author Chiara Leone, 759095, Como.
 */
package bookrecommender.dao;

import java.util.*;
import java.io.*;
import java.net.URL;
import java.nio.file.*;
import java.sql.*;
/**
 * Classe per la gestione delle librerie personali degli utenti.
 * Consente di registrare nuove librerie e di visualizzare i libri contenuti in una libreria specifica.
 */
public class Librerie {
    private Connection conn;

    public Librerie (Connection conn) {
        this.conn = conn;
    }
    
    /**
     * Registra una nuova libreria o aggiorna un libro all'interno di una libreria esistente.
     */
    public boolean registraLibreria (String userId, String libName, String title) {
        boolean newLib = false;
        try {
            // Ricava l'id del libro dal titolo
            String getBookIdSql = "SELECT id FROM Libri WHERE Title = ?";
            String bookId = null;
            try (PreparedStatement getBookStmt = conn.prepareStatement(getBookIdSql)) {
                getBookStmt.setString(1, title);
                ResultSet rs = getBookStmt.executeQuery();
                if (rs.next()) {
                    bookId = rs.getString("id");
                }
            }
            // Verifica se la libreria esiste già per quell'utente e nome
            String checkSql = "SELECT id FROM Librerie WHERE UserID = ? AND Lib_Name = ?";
            int libId = -1;
            try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
                checkStmt.setString(1, userId);
                checkStmt.setString(2, libName);
                ResultSet rs = checkStmt.executeQuery();
                if (rs.next()) {
                    libId = rs.getInt("id");
                }
            }
            // Se la libreria non esiste, la creo
            if (libId == -1) {
                String insertSql = "INSERT INTO Librerie (Lib_Name, UserID) VALUES (?, ?)";
                try (PreparedStatement insertStmt = conn.prepareStatement(insertSql, PreparedStatement.RETURN_GENERATED_KEYS)) {    //restituisce l'id generato
                    insertStmt.setString(1, libName);
                    insertStmt.setString(2, userId);
                    insertStmt.executeUpdate();
                    ResultSet generatedKeys = insertStmt.getGeneratedKeys();
                    if (generatedKeys.next()) {
                        libId = generatedKeys.getInt(1);
                        newLib = true;
                    }
                }
            }
            //inserisco il libro nella libreria
            // Controllo se il libro è già presente nella libreria
            String checkBookSql = "SELECT COUNT(*) FROM Libri.Librerie WHERE LibID = ? AND BookID = ?";
        
            boolean libExist = false;
            try (PreparedStatement checkBookStmt = conn.prepareStatement(checkBookSql)) {
                checkBookStmt.setInt(1, libId);
                checkBookStmt.setString(2, bookId);
                ResultSet rs = checkBookStmt.executeQuery();
                if (rs.next() && rs.getInt(1) > 0) {
                    // se la query restituisce un conteggio maggiore di 0, il libro esiste già nella libreria
                    libExist = true;
                }
            }
            if (!libExist) {
                String insertBookSql = "INSERT INTO Libri.Librerie (LibID, BookID) VALUES (?, ?)";
                try (PreparedStatement insertBookStmt = conn.prepareStatement(insertBookSql)) {
                    insertBookStmt.setInt(1, libId);
                    insertBookStmt.setString(2, bookId);
                    insertBookStmt.executeUpdate();
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return newLib;
    } 

    /**
     * Visualizza i libri contenuti in una libreria specifica dell'utente.
     */
    public List<String> visLib (String userid, String libName) {
        List<String> libList = new ArrayList<>();
        // fa il join tra Librerie, Libri.Librerie e Libri per ottenere i titoli dei libri
        String sql = """
            SELECT b.Title
            FROM Librerie l
            JOIN Libri.Librerie ll ON l.id = ll.LibID
            JOIN Libri b ON ll.BookID = b.id
            WHERE l.UserID = ? AND l.Lib_Name = ?
        """;
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, userid);
            stmt.setString(2, libName);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                libList.add(rs.getString("Title"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return libList;
    }
}