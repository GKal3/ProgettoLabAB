/**
 * Laboratory Project B: "BookRecommender", Academic Year 2025-2026.
 * @author Giulia Kalemi, 756143, Como.
 * @author Chiara Leone, 759095, Como.
 */
package bookrecommender;

import java.sql.*;

/**
 * Class responsible for managing the database connection and providing methods
 * to connect to the PostgreSQL database.
 * It uses JDBC to establish a connection and provides a method to retrieve the connection.
 */
public class DBManager {

  /**
   * The URL of the PostgreSQL database to connect to.
   * It specifies the host, port, and database name.
   */
  private static final String DB_URL = "jdbc:postgresql://localhost:5432/dbBR";
  
  /**
   * The connection object used to interact with the database.
   */
  private Connection connection;
  
  /**
   * Constructor that initializes the DBManager with a database connection.
   * @param connection the database connection to be used for operations
   */
  public DBManager(Connection connection) {
      this.connection = connection;
  }

  /**
   * Returns the database connection used by this DBManager.
   * @return the Connection object representing the database connection
   */
  public Connection getConnection() {
      return connection;
  }

  /**
   * Establishes a connection to the PostgreSQL database using the provided user credentials.
   * It loads the PostgreSQL JDBC driver and returns a Connection object.
   * @param user the username for the database connection
   * @param password the password for the database connection
   * @return a Connection object representing the established connection
   * @throws SQLException if an error occurs while connecting to the database
   */
  public static Connection connect(String user, String password) throws SQLException{
    try {
      Class.forName("org.postgresql.Driver");
    } catch (ClassNotFoundException e) {
      throw new SQLException("PostgreSQL JDBC Driver not found!", e);
    }
    return DriverManager.getConnection(DB_URL, user, password);
  }
}