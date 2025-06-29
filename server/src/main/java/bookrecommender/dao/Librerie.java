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
            
            String getBookIdSql = "SELECT \"id\" FROM \"Libri\" WHERE \"Title\" = ?";
            System.out.println("Esecuzione query 1 riuscita");
            int bookId = -1;
            try (PreparedStatement getBookStmt = conn.prepareStatement(getBookIdSql)) {
                getBookStmt.setString(1, title);
                ResultSet rs = getBookStmt.executeQuery();
                if (rs.next()) {
                    bookId = rs.getInt("id");
                }
            }

            String checkSql = "SELECT \"id\" FROM \"Librerie\" WHERE \"UserID\" = ? AND \"Lib_Name\" = ?";
            System.out.println("Esecuzione query 2 riuscita");

            int libId = -1;
            try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
                checkStmt.setString(1, userId);
                checkStmt.setString(2, libName);
                ResultSet rs = checkStmt.executeQuery();
                if (rs.next()) {
                    libId = rs.getInt("id");
                }
            }
            
            if (libId == -1) {
                String insertSql = "INSERT INTO \"Librerie\" (\"Lib_Name\", \"UserID\") VALUES (?, ?)";
                System.out.println("Esecuzione query 3 riuscita");
                try (PreparedStatement insertStmt = conn.prepareStatement(insertSql, PreparedStatement.RETURN_GENERATED_KEYS)) {    
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
            
           
            String checkBookSql = "SELECT COUNT(*) FROM \"Libri.Librerie\" WHERE \"LibID\" = ? AND \"BookID\" = ?";
            System.out.println("Esecuzione query 4 riuscita");

            boolean libExist = false;
            try (PreparedStatement checkBookStmt = conn.prepareStatement(checkBookSql)) {
                checkBookStmt.setInt(1, libId);
                checkBookStmt.setInt(2, bookId);
                ResultSet rs = checkBookStmt.executeQuery();
                if (rs.next() && rs.getInt(1) > 0) {
                    
                    libExist = true;
                }
            }
            if (!libExist) {
                String insertBookSql = "INSERT INTO \"Libri.Librerie\" (\"LibID\", \"BookID\") VALUES (?, ?)";
                System.out.println("Esecuzione query 5 riuscita");
                try (PreparedStatement insertBookStmt = conn.prepareStatement(insertBookSql)) {
                    insertBookStmt.setInt(1, libId);
                    insertBookStmt.setInt(2, bookId);
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
        String sql = """
            SELECT b.\"Title\"
            FROM \"Librerie\" l
            JOIN \"Libri.Librerie\" ll ON l.\"id\" = ll.\"LibID\"
            JOIN \"Libri\" b ON ll.\"BookID\" = b.\"id\"
            WHERE l.\"UserID\" = ? AND l.\"Lib_Name\" = ?
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

    public boolean deleteLib(String userId, String libName) {
        boolean deleted = false;
        String getLibIdSql = "SELECT \"id\" FROM \"Librerie\" WHERE \"UserID\" = ? AND \"Lib_Name\" = ?";
        String deleteBooksSql = "DELETE FROM \"Libri.Librerie\" WHERE \"LibID\" = ?";
        String deleteLibSql = "DELETE FROM \"Librerie\" WHERE \"id\" = ?";
        try {
            conn.setAutoCommit(false);
            int libId = -1;
            try (PreparedStatement getLibIdStmt = conn.prepareStatement(getLibIdSql)) {
                getLibIdStmt.setString(1, userId);
                getLibIdStmt.setString(2, libName);
                ResultSet rs = getLibIdStmt.executeQuery();
                if (rs.next()) {
                    libId = rs.getInt("id");
                }
            }
            if (libId != -1) {
                try (PreparedStatement deleteBooksStmt = conn.prepareStatement(deleteBooksSql)) {
                    deleteBooksStmt.setInt(1, libId);
                    deleteBooksStmt.executeUpdate();
                }
                try (PreparedStatement deleteLibStmt = conn.prepareStatement(deleteLibSql)) {
                    deleteLibStmt.setInt(1, libId);
                    int rowsAffected = deleteLibStmt.executeUpdate();
                    if (rowsAffected > 0) {
                        deleted = true;
                    }
                }
            }
            conn.commit();
        } catch (SQLException e) {
            try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            e.printStackTrace();
        } finally {
            try { conn.setAutoCommit(true); } catch (SQLException e) { e.printStackTrace(); }
        }
        return deleted;
    }
}