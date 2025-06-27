package bookrecommender;

import java.io.*;
import java.net.Socket;
import java.util.*;

public class ClientConnection {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private ObjectInputStream objectIn;

    public ClientConnection (String host, int port) throws IOException {
        System.out.println("Provo a connettermi al server...");
        socket = new Socket(host, port);
        out = new PrintWriter(socket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        objectIn = new ObjectInputStream(socket.getInputStream());
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
    // Riceve un array di valutazioni dal server
    public int [] receiveRatings() throws IOException {
        try {
            int[] ratings = (int[]) objectIn.readObject();
            if (ratings == null || ratings.length == 0) {
                return new int[0]; // Nessuna valutazione ricevuta
            }
            return ratings;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return new int[0]; // In caso di errore, restituisce un array vuoto
        }
    }

    public String [] receiveInfo() throws IOException {
        try {
            String[] info = (String[]) objectIn.readObject();
            if (info == null || info.length == 0) {
                return new String[0]; // Nessuna informazione ricevuta
            }
            return info;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return new String[0]; // In caso di errore, restituisce un array vuoto
        }
    }

    public void close() throws IOException {
        System.out.println("Chiudo la connessione col server");
        socket.close();
    }
}