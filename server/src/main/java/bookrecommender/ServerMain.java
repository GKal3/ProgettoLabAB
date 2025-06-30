/**
 * Laboratory Project B: "BookRecommender", Academic Year 2025-2026.
 * @author Giulia Kalemi, 756143, Como.
 * @author Chiara Leone, 759095, Como.
 */
package bookrecommender;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.*;
import java.util.Map;

/**
 * Main class for the server application.
 * It initializes the server, listens for incoming client connections,
 * and handles each client connection in a separate thread.
 */
public class ServerMain extends Thread {
    
    /**
     * The port number on which the server listens for incoming connections.
     */
    public static final int port = 10001;

    /**
     * The main method that starts the server.
     * It prompts the user for database credentials, connects to the database,
     * and listens for client connections on the specified port.
     * Each client connection is handled in a separate thread.
     */
    public static void main(String[] args) {
        
        Map<String, String> credentials = DBLoginWindow.showLoginDialog();
        if (credentials == null) {
            return;
        }

        try (
            Connection dbConnection = DBManager.connect(
                credentials.get("user"),
                credentials.get("password")
            );
            ServerSocket serverSocket = new ServerSocket(port)
        ) {
            System.out.println("Server listening on port " + port);
            while (true) {
                Socket socket = serverSocket.accept();
                ClientHandler handler = new ClientHandler(socket, dbConnection);
                Thread t = new Thread(handler);
                t.start();
            }
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }
}