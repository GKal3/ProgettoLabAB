package bookrecommender;

import java.io.*;
import java.net.Socket;
import java.util.*;

public class ClientConnection {
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    public ClientConnection (String host, int port) throws IOException {
        System.out.println("Provo a connettermi al server...");
        socket = new Socket(host, port);
        out = new ObjectOutputStream(socket.getOutputStream());
        out.flush(); // importantissimo
        in = new ObjectInputStream(socket.getInputStream());
        System.out.println("Connessione stabilita!");
    }
    
    // Invia un messaggio al server
    public void sendMessage(String message) throws IOException {
        out.writeObject(message);
        out.flush();
    }
    
    // Serve per ricevere dal server una lista di risultati
    public List<String> receiveList() throws IOException {
        try {
        Object obj = in.readObject();
        if (obj instanceof List<?> list) {
            // Supponiamo che la lista sia di String
            @SuppressWarnings("unchecked")
            List<String> results = (List<String>) list;
            // Leggi anche il "FINE"
            in.readObject();    
            return results;
        }
    } catch (ClassNotFoundException e) {
        e.printStackTrace();
    }
    return new ArrayList<>();
    }
    
    // Riceve un array di valutazioni dal server
    public int [] receiveRatings() throws IOException {
        try {
            int[] ratings = (int[]) in.readObject();
            in.readObject(); // Leggi e scarta "FINE"
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
            String[] info = (String[]) in.readObject();
            in.readObject(); // Leggi e scarta "FINE"
            if (info == null || info.length == 0) {
                return new String[0]; // Nessuna informazione ricevuta
            }
            return info;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return new String[0]; // In caso di errore, restituisce un array vuoto
        }
    }

    // Riceve un messaggio di tipo String dal server
    // usata per il login e la registrazione
    public String receiveMessage() throws IOException {
        try {
            Object obj = in.readObject();
            in.readObject(); // Leggi e scarta "FINE"
            if (obj instanceof String message) {
                return message;
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null; // In caso di errore o tipo non riconosciuto
    }

    public void close() throws IOException {
        System.out.println("Chiudo la connessione col server");
        socket.close();
    }
}