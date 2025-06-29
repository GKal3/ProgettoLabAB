package bookrecommender;

import java.sql.*;

public class DBManager {

  private static final String DB_URL = "jdbc:postgresql://localhost:5432/dbBR";
  
  private Connection connection;

  
  public DBManager(Connection connection) {
      this.connection = connection;
  }

  
  public Connection getConnection() {
      return connection;
  }

  public static Connection connect(String user, String password) throws SQLException{
    try {
      Class.forName("org.postgresql.Driver");
    } catch (ClassNotFoundException e) {
      throw new SQLException("PostgreSQL JDBC Driver non trovato!", e);
    }
    return DriverManager.getConnection(DB_URL, user, password);
  }
}