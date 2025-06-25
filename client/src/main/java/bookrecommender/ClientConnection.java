package bookrecommender;

import java.io.*;
import java.net.Socket;
import java.util.*;

public class ClientConnection {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    public ClientConnection (String host, int port) throws IOException {
        System.out.println("Provo a connettermi al server...");
        socket = new Socket(host, port);
        out = new PrintWriter(socket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        System.out.println("Connessione stabilita!");
    }
    // Invia un messaggio al server
    public void sendMessage(String message) {
        out.println(message);
    }
    // Serve per ricevere dal server una lista di risultati
    public List<String> receiveList() throws IOException {
        List<String> results = new ArrayList<>();
        String line;
        while ((line = in.readLine()) != null && !line.equals("FINE")) {
            results.add(line);
        }
        return results;
    }

    public void close() throws IOException {
        socket.close();
    }
} 


