package bookrecommender;

import java.io.*;
import java.net.Socket;
import java.sql.Connection;
import java.util.*;
import bookrecommender.dao.*;

public class ClientHandler implements Runnable {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private DBManager db;
    private ObjectOutputStream objectOut;

    // DAO
    private Ricerca ricerca;
    private Valutazione valutazione;
    private Visualizza visualizza;
    private Librerie librerie;
    private Registra registra;

    public ClientHandler (Socket socket, Connection dbConnection) {
        this.socket = socket;
        try {
            db = new DBManager(dbConnection);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
            objectOut = new ObjectOutputStream(socket.getOutputStream());

            // Inizializza i DAO
            ricerca = new Ricerca(dbConnection);
            valutazione = new Valutazione(dbConnection);
            visualizza = new Visualizza(dbConnection);
            librerie = new Librerie(dbConnection);
            registra = new Registra(dbConnection);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void run() {
        try {
            String request;
            while ((request = in.readLine()) != null) {
                System.out.println("Ricevuto: " + request);

                String[] parts = request.split(";", 2);
                String command = parts[0];

                switch (command) {
                    case "CERCA_TITOLO":
                        // CERCA_TITOLO;parola
                        if (parts.length < 2) {
                            out.println("ERRORE_PARAMETRI");
                            out.println("FINE");
                            break;
                        }
                        List<String> titoli = ricerca.cercaTitolo(parts[1]);
                        for (String titolo : titoli) {
                            out.println(titolo);
                        }
                        out.println("FINE");
                        break;

                    case "CERCA_AUTORE":
                        // CERCA_AUTORE;autore
                        if (parts.length < 2) {
                            out.println("ERRORE_PARAMETRI");
                            out.println("FINE");
                            break;
                        }
                        List<String> titoliAutore = ricerca.cercaAutore(parts[1]);
                        for (String titolo : titoliAutore) {
                            out.println(titolo);
                        }
                        out.println("FINE");
                        break;

                    case "CERCA_AUTORE_ANNO":
                        // CERCA_AUTORE_ANNO;anno;autore
                        if (parts.length < 2) {
                            out.println("ERRORE_PARAMETRI1");
                            out.println("FINE");
                            break;
                        }
                        String[] paramAA = parts[1].split(";", 2);
                        if (paramAA.length < 2) {
                            out.println("ERRORE_PARAMETRI2");
                            out.println("FINE");
                            break;
                        }
                        List<String> titoliAutoreAnno = ricerca.cercaAutoreAnno(paramAA[1], Integer.parseInt(paramAA[0]));
                        for (String titolo : titoliAutoreAnno) {
                            out.println(titolo);
                        }
                        out.println("FINE");
                        break;

                    case "VISUALIZZA_INFO":
                        // VISUALIZZA_INFO;titolo
                        if (parts.length < 2) {
                            objectOut.writeObject("ERRORE_PARAMETRI");
                            objectOut.writeObject("FINE");
                            break;
                        }
                        String[] info = visualizza.infoLibro(parts[1]);
                        objectOut.writeObject(info);
                        objectOut.writeObject("FINE");
                        break;

                    case "VISUALIZZA_NOTE":
                        // VISUALIZZA_NOTE;titolo
                        if (parts.length < 2) {
                            out.println("ERRORE_PARAMETRI");
                            out.println("FINE");
                            break;
                        }
                        String boh = "";
                        List<String> note = visualizza.note(parts[1], boh);
                        out.println(String.join(";", note));
                        out.println("FINE");
                        break;

                    case "VISUALIZZA_VALUTAZIONI":
                        // VISUALIZZA_VALUTAZIONI;titolo
                        if (parts.length < 2) {
                            objectOut.writeObject("ERRORE_PARAMETRI");
                            objectOut.writeObject("FINE");
                            break;
                        }
                        int[] val = visualizza.recapVal(parts[1]);
                        //out.println(Arrays.toString(val));
                        objectOut.writeObject(val);
                        objectOut.writeObject("FINE");
                        break;

                    case "VISUALIZZA_SUGGERIMENTI":
                        // VISUALIZZA_SUGGERIMENTI;titolo
                        if (parts.length < 2) {
                            out.println("ERRORE_PARAMETRI");
                            out.println("FINE");
                            break;
                        }
                        List<String> suggerimenti = visualizza.recapSugg(parts[1]);
                        out.println(String.join(";", suggerimenti));
                        out.println("FINE");
                        break;

                    case "REGISTRA_LIBRERIA":
                        // REGISTRA_LIBRERIA;id,nomeLibreria,titolo
                        if (parts.length < 2) {
                            out.println("ERRORE_PARAMETRI");
                            out.println("FINE");
                            break;
                        }
                        String[] paramLib = parts[1].split(",");
                        if (paramLib.length < 3) {
                            out.println("ERRORE_PARAMETRI");
                            out.println("FINE");
                            break;
                        }
                        boolean esitoLib = librerie.registraLibreria(paramLib[0], paramLib[1], paramLib[2]);
                        out.println(esitoLib ? "LIBRERIA_REGISTRATA" : "LIBRERIA_AGGIORNATA");
                        out.println("FINE");
                        break;

                    case "VISUALIZZA_LIBRERIA":
                        // VISUALIZZA_LIBRERIA;id,nomeLibreria
                        if (parts.length < 2) {
                            out.println("ERRORE_PARAMETRI");
                            out.println("FINE");
                            break;
                        }
                        String[] paramVisLib = parts[1].split(",");
                        if (paramVisLib.length < 2) {
                            out.println("ERRORE_PARAMETRI");
                            out.println("FINE");
                            break;
                        }
                        List<String> libriLibreria = librerie.visLib(paramVisLib[0], paramVisLib[1]);
                        out.println(String.join(",", libriLibreria));
                        out.println("FINE");
                        break;

                    // Aggiungi qui altri casi per Valutazione, ad esempio:
                    // case "INSERISCI_VALUTAZIONE":
                    // case "INSERISCI_SUGGERIMENTO":

                    default:
                        out.println("COMANDO_NON_RICONOSCIUTO");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Client disconnesso");
    }
}