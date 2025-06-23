/**
 * Progetto laboratorio A: "BookRecommender", anno 2024-2025
 * @author Giulia Kalemi, Matricola 756143, sede di Como.
 * @author Chiara Leone, Matricola 759095, sede di Como.
 */
package bookrecommender.dao;

import java.util.*;
import java.io.*;
import java.net.URL;
import java.nio.file.*;
/**
 * Classe per la gestione delle librerie personali degli utenti.
 * Consente di registrare nuove librerie e di visualizzare i libri contenuti in una libreria specifica.
 */
public class Librerie {
    /**
     * Percorso al file CSV contenente i dati delle librerie.
     */
    private final Path file = Paths.get("src/main/resources/csv/Librerie.dati.csv");
    /**
     * URL del file CSV contenente i dati delle librerie, recuperato tramite il class loader.
     */
    private final URL link = getClass().getResource("/csv/Librerie.dati.csv");
    
    /**
     * Registra una nuova libreria o aggiorna un libro all'interno di una libreria esistente.
     * Se la libreria con lo stesso ID utente e nome esiste già, aggiunge il titolo del libro a quella libreria.
     * @param id      l'ID dell'utente.
     * @param nomeLib il nome della libreria.
     * @param tit     il titolo del libro da aggiungere.
     * @return <code>true</code> se la libreria non esisteva ed è stata registrata,
     *         <code>false</code> se la libreria esisteva già ed è stata aggiornata.
     */
    public boolean registraLibreria (String id, String nomeLib, String tit) {
        boolean esito = true;
        int iRiga = 0, iRigaF = 0;
        String line = '"' + id + '"' + "," + '"' + nomeLib + '"' + ",";

        try {
            BufferedReader read = new BufferedReader(new InputStreamReader(link.openStream()));
            String riga;
            List<String> lines = new ArrayList<>();

            while ((riga = read.readLine()) != null) {
                String[] tipo = riga.split(",");
                lines.add(riga);
                
                if (tipo.length > 1
                        && id.replace("\"", "").equals(tipo[0].replace("\"", ""))
                        && nomeLib.trim().replace("\"", "").equalsIgnoreCase(tipo[1].trim().replace("\"", ""))) {
                    esito = false;
                    line = riga + "," + tit.trim();
                    iRigaF = iRiga;
                }
                iRiga++;
            }
            read.close();

            BufferedWriter wr = new BufferedWriter(new FileWriter(file.toFile()));

            if (!esito && iRigaF < lines.size()) {
                lines.set(iRigaF, line);
                
            } else {
                line = line + tit.trim();
                lines.add(line);
            }
            
            for (String righe : lines) {
                wr.write(righe);
                wr.newLine();
            }            
            wr.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return esito;
    }
    /**
     * Visualizza i libri contenuti in una libreria specifica dell'utente.
     * @param userid  l'ID dell'utente.
     * @param nomeLib il nome della libreria.
     * @return una lista di titoli di libri contenuti nella libreria specificata.
     */
    public List<String> visLib (String userid, String nomeLib) {
        List<String> listaLib = new ArrayList<>();
        
            try (BufferedReader read = new BufferedReader(new InputStreamReader(link.openStream()))) {
                String riga;
                while ((riga = read.readLine()) != null) {
                    
                    String [] tipo = riga.split(",");
                    
                    if (tipo.length > 0
                            && userid.replace("\"", "").equals(tipo[0].replace("\"", ""))
                            && nomeLib.replace("\"", "").equalsIgnoreCase(tipo[1].trim().replace("\"", ""))) {
                        for (int i = 2; i < tipo.length; i++) {
                                listaLib.add(tipo[i]);
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        return listaLib;
    }   
}