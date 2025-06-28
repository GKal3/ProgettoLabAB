package bookrecommender;

import java.io.*;
import java.net.Socket;
import java.sql.Connection;
import java.util.*;
import bookrecommender.dao.*;

public class ClientHandler implements Runnable {
    private Socket socket;
    //private BufferedReader in;
    //private PrintWriter out;
    private DBManager db;
    private ObjectOutputStream out;
    private ObjectInputStream in;

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
            //in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            //out = new PrintWriter(socket.getOutputStream(), true);
            out = new ObjectOutputStream(socket.getOutputStream());
            out.flush(); // importantissimo
            in = new ObjectInputStream(socket.getInputStream());

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
            while ((request = (String) in.readObject()) != null) {
                System.out.println("Ricevuto: " + request);

                String[] parts = request.split(";", 2);
                String command = parts[0];

                switch (command) {
                    case "CERCA_TITOLO":
                        // CERCA_TITOLO;parola
                        if (parts.length < 2) {
                            out.writeObject("ERRORE_PARAMETRI");
                            out.writeObject("FINE");
                            break;
                        }
                        List<String> titoli = ricerca.cercaTitolo(parts[1]);
                        out.writeObject(titoli);
                        out.writeObject("FINE");
                        break;

                    case "CERCA_AUTORE":
                        // CERCA_AUTORE;autore
                        if (parts.length < 2) {
                            out.writeObject("ERRORE_PARAMETRI");
                            out.writeObject("FINE");
                            break;
                        }
                        List<String> titoliAutore = ricerca.cercaAutore(parts[1]);
                        out.writeObject(titoliAutore);
                        out.writeObject("FINE");
                        break;

                    case "CERCA_AUTORE_ANNO":
                        // CERCA_AUTORE_ANNO;anno;autore
                        if (parts.length < 2) {
                            out.writeObject("ERRORE_PARAMETRI1");
                            out.writeObject("FINE");
                            break;
                        }
                        String[] paramAA = parts[1].split(";", 2);
                        if (paramAA.length < 2) {
                            out.writeObject("ERRORE_PARAMETRI1");
                            out.writeObject("FINE");
                            break;
                        }
                        List<String> titoliAutoreAnno = ricerca.cercaAutoreAnno(paramAA[1], Integer.parseInt(paramAA[0]));
                        out.writeObject(titoliAutoreAnno);
                        out.writeObject("FINE");
                        break;

                    case "CERCA_LIB":
                        // CERCA_LIB;parola,id,nomeLibreria
                        if (parts.length < 2) {
                            out.writeObject("ERRORE_PARAMETRI");
                            out.writeObject("FINE");
                            break;
                        }
                        String[] paramLibS = parts[1].split(",");
                        if (paramLibS.length < 3) {
                            out.writeObject("ERRORE_PARAMETRI");
                            out.writeObject("FINE");
                            break;
                        }
                        List<String> libri = ricerca.searchLib(paramLibS[0], paramLibS[1], paramLibS[2]);
                        out.writeObject(libri);
                        out.writeObject("FINE");
                        break;

                    case "VISUALIZZA_INFO":
                        // VISUALIZZA_INFO;titolo
                        if (parts.length < 2) {
                            out.writeObject("ERRORE_PARAMETRI");
                            out.writeObject("FINE");
                            break;
                        }
                        String[] info = visualizza.infoLibro(parts[1]);
                        out.writeObject(info);
                        out.writeObject("FINE");
                        break;

                    case "VISUALIZZA_NOTE":
                        // VISUALIZZA_NOTE;titolo,categoria
                        if (parts.length < 2) {
                            out.writeObject("ERRORE_PARAMETRI");
                            out.writeObject("FINE");
                            break;
                        }
                        String[] noteParams = parts[1].split(",");
                        if (noteParams.length < 2) {
                            out.writeObject("ERRORE_PARAMETRI");
                            out.writeObject("FINE");
                            break;
                        }
                        List<String> note = visualizza.note(noteParams[0], noteParams[1]);
                        out.writeObject(note);
                        out.writeObject("FINE");
                        break;

                    case "VISUALIZZA_VALUTAZIONI":
                        // VISUALIZZA_VALUTAZIONI;titolo
                        if (parts.length < 2) {
                            out.writeObject("ERRORE_PARAMETRI");
                            out.writeObject("FINE");
                            break;
                        }
                        int[] val = visualizza.recapVal(parts[1]);
                        //out.println(Arrays.toString(val));
                        out.writeObject(val);
                        out.writeObject("FINE");
                        break;

                    case "VISUALIZZA_SUGGERIMENTI":
                        // VISUALIZZA_SUGGERIMENTI;titolo
                        if (parts.length < 2) {
                            out.writeObject("ERRORE_PARAMETRI");
                            out.writeObject("FINE");
                            break;
                        }
                        List<String> suggerimenti = visualizza.recapSugg(parts[1]);
                        out.writeObject(suggerimenti);
                        out.writeObject("FINE");
                        break;

                    case "REGISTRA_LIBRERIA":
                        // REGISTRA_LIBRERIA;id,nomeLibreria,titolo
                        if (parts.length < 2) {
                            out.writeObject("ERRORE_PARAMETRI");
                            out.writeObject("FINE");
                            break;
                        }
                        String[] paramLib = parts[1].split(",");
                        if (paramLib.length < 3) {
                            out.writeObject("ERRORE_PARAMETRI");
                            out.writeObject("FINE");
                            break;
                        }
                        boolean esitoLib = librerie.registraLibreria(paramLib[0], paramLib[1], paramLib[2]);
                        out.writeObject(esitoLib ? "LIBRERIA_REGISTRATA" : "LIBRERIA_AGGIORNATA");
                        out.writeObject("FINE");
                        break;

                    case "VISUALIZZA_LIBRERIA":
                        // VISUALIZZA_LIBRERIA;id,nomeLibreria
                        if (parts.length < 2) {
                            out.writeObject("ERRORE_PARAMETRI");
                            out.writeObject("FINE");
                            break;
                        }
                        String[] paramVisLib = parts[1].split(",");
                        if (paramVisLib.length < 2) {
                            out.writeObject("ERRORE_PARAMETRI");
                            out.writeObject("FINE");
                            break;
                        }
                        List<String> libriLibreria = librerie.visLib(paramVisLib[0], paramVisLib[1]);
                        out.writeObject(libriLibreria);
                        out.writeObject("FINE");
                        break;

                    case "LOGIN":
                        // LOGIN;username,password
                        if (parts.length < 2) {
                            out.writeObject("ERRORE_PARAMETRI");
                            out.writeObject("FINE");
                            break;
                        }
                        String[] paramLogin = parts[1].split(",");
                        if (paramLogin.length < 2) {
                            out.writeObject("ERRORE_PARAMETRI");
                            out.writeObject("FINE");
                            break;
                        }
                        String[] data = registra.login(paramLogin[0], paramLogin[1]);
                        out.writeObject(data);
                        out.writeObject("FINE");
                        break;

                    case "REG":
                        // REG;nome_cognome,cf,email,id,password
                        if (parts.length < 2) {
                            out.writeObject("ERRORE_PARAMETRI");
                            out.writeObject("FINE");
                            break;
                        }
                        String[] paramReg = parts[1].split(",");
                        if (paramReg.length < 5) {
                            out.writeObject("ERRORE_PARAMETRI");
                            out.writeObject("FINE");
                            break;
                        }
                        registra.registrazione(paramReg);
                        out.writeObject("FINE");
                        break;

                    case "VIS_LIB_LIST":
                        // VIS_LIB_LIST;id
                        if (parts.length < 2) {
                            out.writeObject("ERRORE_PARAMETRI");
                            out.writeObject("FINE");
                            break;
                        }
                        String id = parts[1];
                        List<String> libList = visualizza.libList(id);
                        out.writeObject(libList);
                        out.writeObject("FINE");
                        break;
                        
                    case "INS_SUGG":
                        // INS_SUGG;id,titolo,suggerimento
                        if (parts.length < 2) {
                            out.writeObject("ERRORE_PARAMETRI");
                            out.writeObject("FINE");
                            break;
                        }
                        String[] paramSugg = parts[1].split(",");
                        if (paramSugg.length < 3) {
                            out.writeObject("ERRORE_PARAMETRI");
                            out.writeObject("FINE");
                            break;
                        }
                        boolean esitoSugg = valutazione.inserisciSuggerimentoLibri(paramSugg[0], paramSugg[1], paramSugg[2]);
                        out.writeObject(esitoSugg ? "SUGG_INS" : "ERROR_INS");
                        out.writeObject("FINE");
                        break;

                    case "INS_VAL":
                        // INS_VAL;userId,titolo,valutazioni,note
                        try {
                        String user = (String) in.readObject();
                        String tit = (String) in.readObject();
                        int[] val1 = (int[]) in.readObject();
                        @SuppressWarnings("unchecked")
                        List<String> note1 = (List<String>) in.readObject();

                        boolean esito = valutazione.inserisciValutazioneLibro(user, tit, val1, note1);
                        out.writeObject(esito ? "VAL_INS" : "ERROR_INS");
                        out.writeObject("FINE");
                    } catch (Exception ex) {
                        out.writeObject("ERRORE_PARAMETRI");
                        out.writeObject("FINE");
                    }
                    break;
                    default:
                        out.writeObject("COMANDO_NON_RICONOSCIUTO");
                }
            }
        } catch (EOFException e) {
            System.out.println("Client disconnesso normalmente.");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}