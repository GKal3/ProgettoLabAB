/**
 * Laboratory Project B: "BookRecommender", Academic Year 2025-2026.
 * @author Giulia Kalemi, 756143, Como.
 * @author Chiara Leone, 759095, Como.
 */
package bookrecommender;

import java.io.*;
import java.net.Socket;
import java.util.*;

/**
 * Handles the connection between the client and the server.
 * <p>
 * Manages object streams and provides methods to send and receive data over a socket.
 * </p>
 */
public class ClientConnection {
    /**
     * Socket used to establish the connection with the server.
     */
    private Socket socket;

    /**
     * Output stream used to send objects to the server.
     */
    private ObjectOutputStream out;

    /**
     * Input stream used to receive objects from the server.
     */
    private ObjectInputStream in;

    /**
     * Establishes a connection with the server using the specified host and port.
     * @param host the server host address
     * @param port the server port
     * @throws IOException if the connection fails
     */
    public ClientConnection (String host, int port) throws IOException {
        socket = new Socket(host, port);
        out = new ObjectOutputStream(socket.getOutputStream());
        out.flush();
        in = new ObjectInputStream(socket.getInputStream());
    }
    
    /**
     * Sends a message to the server.
     * @param message the message to send
     * @throws IOException if an I/O error occurs
     */
    public void sendMessage (String message) throws IOException {
        out.writeObject(message);
        out.flush();
    }

    /**
     * Sends an object to the server.
     * @param obj the object to send
     * @throws IOException if an I/O error occurs
     */
    public void sendObject (Object obj) throws IOException {
        out.writeObject(obj);
        out.flush();
    }
    
    /**
     * Receives a list of strings from the server.
     * @return a list of strings, or an empty list if the response is invalid
     * @throws IOException if an I/O error occurs
     */
    public List<String> receiveList() throws IOException {
        try {
            Object obj = in.readObject();
            if (obj instanceof List<?> list) {
                @SuppressWarnings("unchecked")
                List<String> results = (List<String>) list;
                in.readObject();    
                return results;
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }
    
    /**
     * Receives an array of integer ratings from the server.
     * @return an array of ratings, or an empty array if none are received or an error occurs
     * @throws IOException if an I/O error occurs
     */
    public int [] receiveRatings() throws IOException {
        try {
            int[] ratings = (int[]) in.readObject();
            in.readObject();
            if (ratings == null || ratings.length == 0) {
                return new int[0];
            }
            return ratings;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return new int[0];
        }
    }

    /**
     * Receives an array of strings (e.g., user or book information) from the server.
     * @return a string array, or an empty array if none are received or an error occurs
     * @throws IOException if an I/O error occurs
     */
    public String [] receiveInfo() throws IOException {
        try {
            String[] info = (String[]) in.readObject();
            in.readObject();
            if (info == null || info.length == 0) {
                return new String[0];
            }
            return info;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return new String[0];
        }
    }

    /**
     * Receives a single message from the server.
     * @return the received message, or {@code null} if an error occurs
     * @throws IOException if an I/O error occurs
     */
    public String receiveMessage() throws IOException {
        try {
            Object obj = in.readObject();
            in.readObject();
            if (obj instanceof String message) {
                return message;
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Closes the connection with the server and releases resources.
     * @throws IOException if an I/O error occurs during closing
     */
    public void close() throws IOException {
        sendMessage("QUIT");
        socket.close();
    }
}