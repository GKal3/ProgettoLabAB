/**
 * Progetto laboratorio A: "BookRecommender", anno 2024-2025
 * @author Giulia Kalemi, Matricola 756143, sede di Como.
 * @author Chiara Leone, Matricola 759095, sede di Como.
 */
package bookrecommender;

import java.io.*;
import java.net.URL;
import java.util.*;
/**
 * Classe che gestisce la visualizzazione delle informazioni relative ai libri, incluse le informazioni
 * sul libro, le valutazioni degli utenti, i suggerimenti per altri libri e le note aggiuntive.
 */
public class Visualizza {
	/**
     * URL del file CSV contenente i dati dei libri, recuperato tramite il class loader.
     */
	private final URL linkLib = getClass().getResource("/csv/Libri.dati.csv");
    /**
     * URL del file CSV contenente i dati relativi alle valutazioni dei libri, recuperato tramite il class loader.
     */
    private final URL linkVal = getClass().getResource("/csv/ValutazioniLibri.dati.csv");
    /**
     * URL del file CSV contenente i dati relativi ai suggerimenti per i libri, recuperato tramite il class loader.
     */
    private final URL linkSugg = getClass().getResource("/csv/ConsigliLibri.dati.csv");
    /**
     * Restituisce un array di interi contenente le medie delle valutazioni per ogni criterio,
     * insieme al numero totale di recensioni per il libro specificato.
     * @param titolo il titolo del libro per il quale ottenere le valutazioni.
     * @return un array di interi, dove i primi 6 valori rappresentano le medie dei punteggi e
     *         l'ultimo valore rappresenta il numero di recensioni ricevute.
     */
    public int [] recapVal (String titolo) {
        int j = 0;
        int [] val = new int[7];
        
        try (BufferedReader read = new BufferedReader(new InputStreamReader(linkVal.openStream()))) {
            String riga = read.readLine();
            
            while ((riga = read.readLine()) != null) {
                
                String [] tipo = riga.split(",");
                if (tipo.length > 0
                        && tipo[1].replace("\"", "").equalsIgnoreCase(titolo.replace("\"", ""))) {
                    
                    for (int i = 2; i <= 7; i++) {
                        val[i-2] += Integer.parseInt(tipo[i].trim().replace("\"", ""));
                    }
                    j++;
                }
            }
            
            if (j>0) {
                for(int i = 0; i < 6; i++){
                    val[i] = val[i]/j;
                }
                val[6] = j; 
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return val;
    }
    /**
     * Restituisce una lista di suggerimenti di libri basati sul titolo del libro specificato,
     * insieme al numero di utenti che hanno suggerito ciascun libro.
     * @param titolo il titolo del libro per il quale ottenere i suggerimenti.
     * @return una lista di stringhe, ognuna rappresentante un libro suggerito e il numero di utenti
     *         che lo hanno suggerito.
     */
    public List<String> recapSugg (String titolo) {
        List<String> sugg = new ArrayList<>();
        int j = 0;
        Map<String, Integer> count = new HashMap<>();   // Mappa per tenere traccia dei suggerimenti e del loro contatore
        try (BufferedReader read = new BufferedReader(new InputStreamReader(linkSugg.openStream()))) {
            String riga = read.readLine();
            
            while ((riga = read.readLine()) != null) {
                
                String [] tipo = riga.split(",");
                
                if (tipo.length > 0
                        && tipo[1].trim().replace("\"", "").equalsIgnoreCase(titolo.trim().replace("\"", ""))) {
                    String [] sLib = Arrays.copyOfRange(tipo, 2, tipo.length); 

                    for (String libro : sLib) {
                        count.put(libro, count.getOrDefault(libro, 0) + 1);
                    }
                    j++;
                }
            }
            
            if( j > 0 ){
                for (Map.Entry<String, Integer> entry : count.entrySet()) {
                    String tit = entry.getKey().trim();
                    int nSugg = entry.getValue();
                    sugg.add(tit + " (da " + nSugg + " utenti)");
                }
            } else {
                sugg.add("Ancora nessun suggerimento");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sugg;
    }
    /**
     * Restituisce le informazioni di base di un libro (autori, categoria, editore e anno di pubblicazione).
     * @param titolo il titolo del libro per il quale ottenere le informazioni.
     * @return un array di stringhe contenente le informazioni sul libro: "Autori", "Categoria", 
     *         "Editore" e "Anno di pubblicazione".
     */
    public String [] infoLibro (String titolo) {
        String [] info = new String[4];

        try (BufferedReader read = new BufferedReader(new InputStreamReader(linkLib.openStream()))) {
            String riga = read.readLine();
            
            while ((riga = read.readLine()) != null) {
                
                String [] tipo = riga.split(",");
                if (tipo.length > 0
                        && tipo[0].replace("\"", "").equalsIgnoreCase(titolo.replace("\"", ""))) {
                    
                    for (int i = 0; i < 4; i++) {
                        info[i] = tipo[i+1].trim().replace("\"", "");
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return info;
    }
    /**
     * Restituisce una lista di note fornite dagli utenti per un libro specificato.
     * @param titolo il titolo del libro per il quale ottenere le note.
     * @return una lista di stringhe contenente le note degli utenti, o un messaggio
     *         indicante che non ci sono note se non ne è presente nessuna.
     */
    public List<String> note (String titolo) {
        List<String> notes = new ArrayList<>();
        int j = 0;

        try (BufferedReader read = new BufferedReader(new InputStreamReader(linkVal.openStream()))) {
            String riga = read.readLine();
            
            while ((riga = read.readLine()) != null) {
                
                String [] tipo = riga.split(",");
                
                if (tipo.length == 9
                        && tipo[1].trim().replace("\"", "").equalsIgnoreCase(titolo.trim().replace("\"", ""))) {
                    notes.add((tipo[8].trim()) + " da @" + tipo[0].replace("\"", ""));
                    j++;
                }
            }
            if( j == 0 ){
                notes.add("Ancora nessuna nota");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return notes;
    }
}