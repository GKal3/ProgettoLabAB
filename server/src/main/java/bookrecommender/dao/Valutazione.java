/**
 * Project lab B: "BookRecommender", year 2025-2026
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
 * Classe che gestisce le operazioni relative alle valutazioni e ai suggerimenti dei libri.
 */
public class Valutazione {
    private Connection conn;

    public Valutazione (Connection conn) {
        this.conn = conn;
    }
    /**
     * Inserisce una valutazione per un libro specifico.
     * @param userId l'ID dell'utente che effettua la valutazione.
     * @param title titolo del libro valutato.
     * @param val un array di 6 interi rappresentanti le valutazioni assegnate.
     * @param note eventuali note aggiuntive sulla valutazione (opzionale).
     */
    public boolean inserisciValutazioneLibro(String userId, String title, int[] val, List<String> noteList) {  // ValutazioniLibri e Libri
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
     * Inserisce un suggerimento per un libro specifico.
     * @param userId l'ID dell'utente che fornisce il suggerimento.
     * @param title il titolo del libro per cui si sta fornendo il suggerimento.
     * @param sugg il suggerimento da aggiungere.
     * @return <code>true</code> se il suggerimento è stato aggiunto correttamente,
     *         <code>false</code> in caso di errore o se non è stato possibile aggiungerlo.
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