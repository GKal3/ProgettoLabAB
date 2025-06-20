package bookrecommender;

import java.sql.*;
import java.util.*;

public class DBManager {

  private static final String DB_URL = "jdbc:postgresql://localhost:5432/dbBR";
  private static final String USER = "postgres";
  private static final String PASSWORD = "131103Giuk";
  private Connection conn;
  
  public static Connection connect() throws SQLException{
    return DriverManager.getConnection(DB_URL, USER, PASSWORD);
  }

  public boolean loginUtente(String username, String passwordInput) throws SQLException {
    String sql = "SELECT * FROM UtentiRegistrati WHERE username = ? AND password = ?";
    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setString(1, username);
      stmt.setString(2, passwordInput);
      ResultSet rs = stmt.executeQuery();
      return rs.next();
    }
  }

  public List<String> getLibri() throws SQLException {
    List<String> lista = new ArrayList<>();
    String sql = "SELECT titolo FROM Libri";
    try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
      while (rs.next()) {
        lista.add(rs.getString("titolo"));
      }
    }
    return lista;
  }
}