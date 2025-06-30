/**
 * Laboratory Project B: "BookRecommender", Academic Year 2025-2026.
 * @author Giulia Kalemi, 756143, Como.
 * @author Chiara Leone, 759095, Como.
 */
package bookrecommender.dao;

import java.io.*;
import java.net.URL;
import java.nio.file.*;
import java.sql.*;
import java.util.*;

/**
 * Class responsible for managing book evaluations and suggestions.
 * It allows users to rate books and suggest books to others.
 * This class interacts with the "ValutazioniLibri" and "ConsigliLibri" tables in the database.
 */
public class Valutazione {
    
    /**
     * Database connection used to interact with book evaluation data.
     */
    private Connection conn;

    /**
     * Constructor that initializes the Valutazione object with a database connection.
     * @param conn the database connection to be used for operations
     */
    public Valutazione (Connection conn) {
        this.conn = conn;
    }
    
    /**
     * Inserts a book evaluation for a specific user.
     * @param userId the ID of the user who is evaluating the book.
     * @param title the title of the book being evaluated.
     * @param val an array of integers representing the evaluation scores for different aspects of the book.
     * @param noteList a list of notes corresponding to each aspect of the evaluation.
     * @return <code>true</code> if the evaluation was successfully added, <code>false</code> otherwise.
     */
    public boolean inserisciValutazioneLibro (String userId, String title, int[] val, List<String> noteList) {
        boolean feedback = false;
        try {
        
            String getBookIdSql = "SELECT \"id\" FROM \"Libri\" WHERE \"Title\" = ?";
            int bookId = -1;
            try (PreparedStatement getBookStmt = conn.prepareStatement(getBookIdSql)) {
                getBookStmt.setString(1, title);
                ResultSet rs = getBookStmt.executeQuery();
                if (rs.next()) {
                    bookId = rs.getInt("id");
                }
            }

            if (bookId == -1) {
                return false;
            }

            String checkSql = "SELECT COUNT(*) FROM \"ValutazioniLibri\" WHERE \"UserID\" = ? AND \"BookID\" = ?";
            try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
                checkStmt.setString(1, userId);
                checkStmt.setInt(2, bookId);
                ResultSet rs = checkStmt.executeQuery();
                if (rs.next() && rs.getInt(1) > 0) {
                    return false;
                }
            }

            int sum = 0;
            for (int v : val) sum += v;
            int finalVote = Math.round((float) sum / val.length);
            
            String insertSql = """
                INSERT INTO \"ValutazioniLibri\" (\"UserID\", \"BookID\", \"Style\", \"Content\", \"Pleasantness\", \"Originality\", \"Edition\", \"FinalVote\", \"Note_Style\", \"Note_Content\", \"Note_Pleasantness\", \"Note_Originality\", \"Note_Edition\")
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            """;
            try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
                insertStmt.setString(1, userId);
                insertStmt.setInt(2, bookId);
                insertStmt.setInt(3, val[0]); 
                insertStmt.setInt(4, val[1]); 
                insertStmt.setInt(5, val[2]); 
                insertStmt.setInt(6, val[3]); 
                insertStmt.setInt(7, val[4]); 
                insertStmt.setInt(8, finalVote); 
                insertStmt.setString(9,  noteList.size() > 0 ? noteList.get(0) : null); 
                insertStmt.setString(10, noteList.size() > 1 ? noteList.get(1) : null); 
                insertStmt.setString(11, noteList.size() > 2 ? noteList.get(2) : null); 
                insertStmt.setString(12, noteList.size() > 3 ? noteList.get(3) : null); 
                insertStmt.setString(13, noteList.size() > 4 ? noteList.get(4) : null); 
                insertStmt.executeUpdate();
                feedback = true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return feedback;
    }
    
    /**
     * Inserts a book suggestion for a specific user.
     * @param userId the ID of the user who is suggesting the book.
     * @param title the title of the book being suggested.
     * @param sugg the title of the suggested book.
     * @return <code>true</code> if the suggestion was successfully added, <code>false</code> otherwise.
     */
    public boolean inserisciSuggerimentoLibri (String userId, String title, String sugg) {  
        boolean feedback = false;
        try {
            
            String getBookIdSql = "SELECT \"id\" FROM \"Libri\" WHERE \"Title\" = ?";
            int bookId = -1;
            try (PreparedStatement getBookStmt = conn.prepareStatement(getBookIdSql)) {
                getBookStmt.setString(1, title);
                ResultSet rs = getBookStmt.executeQuery();
                if (rs.next()) {
                    bookId = rs.getInt("id");
                }
            }

            String getSuggIdSql = "SELECT \"id\" FROM \"Libri\" WHERE \"Title\" = ?";
            int suggId = -1;
            try (PreparedStatement getSuggStmt = conn.prepareStatement(getSuggIdSql)) {
                getSuggStmt.setString(1, sugg);
                ResultSet rs = getSuggStmt.executeQuery();
                if (rs.next()) {
                    suggId = rs.getInt("id");
                }
            }

            String checkSql = "SELECT COUNT(*) FROM \"ConsigliLibri\" WHERE \"UserID\" = ? AND \"BookID\" = ?";
            try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
                checkStmt.setString(1, userId);
                checkStmt.setInt(2, bookId);
                ResultSet rs = checkStmt.executeQuery();
                if (rs.next() && rs.getInt(1) >= 3) {
                    
                    return feedback;
                }
            }
            
            String checkDuplicateSql = "SELECT COUNT(*) FROM \"ConsigliLibri\" WHERE \"UserID\" = ? AND \"BookID\" = ? AND \"SuggID\" = ?";
            try (PreparedStatement checkDupStmt = conn.prepareStatement(checkDuplicateSql)) {
                checkDupStmt.setString(1, userId);
                checkDupStmt.setInt(2, bookId);
                checkDupStmt.setInt(3, suggId);
                ResultSet rs = checkDupStmt.executeQuery();
                if (rs.next() && rs.getInt(1) > 0) {

                    return feedback;
                }
            }
            
            String insertSql = "INSERT INTO \"ConsigliLibri\" (\"UserID\", \"BookID\", \"SuggID\") VALUES (?, ?, ?)";
            try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
                insertStmt.setString(1, userId);
                insertStmt.setInt(2, bookId);
                insertStmt.setInt(3, suggId);
                insertStmt.executeUpdate();
                feedback = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return feedback;
    }
}