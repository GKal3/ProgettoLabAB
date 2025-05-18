/**
 * Progetto laboratorio A: "BookRecommender", anno 2024-2025
 * @author Giulia Kalemi, Matricola 756143, sede di Como.
 * @author Chiara Leone, Matricola 759095, sede di Como.
 */
package bookrecommender;

import java.io.*;
import java.net.URL;
import java.nio.file.*;
import java.util.*;
/**
 * Classe che gestisce le operazioni relative alle valutazioni e ai suggerimenti dei libri.
 */
public class Valutazione {
    /**
     * Percorso al file CSV contenente i dati relativi alle valutazioni dei libri.
     */
    private final Path file = Paths.get("src/main/resources/csv/ValutazioniLibri.dati.csv");
    /**
     * Percorso al file CSV contenente i dati relativi ai suggerimenti per i libri.
     */
    private final Path file1 = Paths.get("src/main/resources/csv/ConsigliLibri.dati.csv");
    /**
     * URL del file CSV contenente i dati relativi ai suggerimenti per i libri, recuperato tramite il class loader.
     */
    private final URL link = getClass().getResource("/csv/ConsigliLibri.dati.csv");
    /**
     * Inserisce una valutazione per un libro specifico.
     * @param id l'ID dell'utente che effettua la valutazione.
     * @param titolo il titolo del libro valutato.
     * @param val un array di 6 interi rappresentanti le valutazioni assegnate.
     * @param note eventuali note aggiuntive sulla valutazione (opzionale).
     */
    public void inserisciValutazioneLibro (String id, String titolo, int [] val, String note) {

        try (BufferedWriter wr = new BufferedWriter (new FileWriter(file.toFile(), true))) {
            wr.write('"' + id + '"' + "," + titolo.trim());

        } catch (IOException e) {
            e.printStackTrace();
        }
        
        for (int i = 0; i < 6; i++) {
            try (BufferedWriter wr = new BufferedWriter (new FileWriter(file.toFile(), true))) {
                wr.write("," + '"' + String.valueOf(val[i]) + '"');

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (note != null && !note.isEmpty()) {
            try (BufferedWriter wr = new BufferedWriter (new FileWriter(file.toFile(), true))) {
                wr.write("," + '"' + note.replace(",", ";") + '"');
                wr.newLine();
                
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    /**
     * Inserisce un suggerimento per un libro specifico.
     * @param id l'ID dell'utente che fornisce il suggerimento.
     * @param titolo il titolo del libro per cui si sta fornendo il suggerimento.
     * @param sugg il suggerimento da aggiungere.
     * @return <code>true</code> se il suggerimento è stato aggiunto correttamente,
     *         <code>false</code> in caso di errore o se non è stato possibile aggiungerlo.
     */
    public boolean inserisciSuggerimentoLibri (String id, String titolo, String sugg) {
        int iRiga = 0, iRigaF = 0;
        boolean esito = true;
        boolean trovato = false;
        String nuovaL = '"' + id + '"' + "," + titolo.trim() + ",";
        
        try {
            BufferedReader read = new BufferedReader(new InputStreamReader(link.openStream()));
            String riga;
            List<String> lines = new ArrayList<>();

            while ((riga = read.readLine()) != null) {
                String[] tipo = riga.split(",");
                lines.add(riga);
                if (tipo.length > 1
                        && id.trim().replace("\"", "").equals(tipo[0].trim().replace("\"", ""))
                        && titolo.trim().replace("\"", "").equalsIgnoreCase(tipo[1].trim().replace("\"", ""))) {
                    trovato = true;
                    nuovaL = riga;
                    iRigaF = iRiga;

                    if (tipo.length < 5) {
                        nuovaL = nuovaL + "," + sugg.trim();
                    } else {
                        esito = false;
                    }
                }
                
                iRiga++;
            }
            read.close();

            BufferedWriter wr = new BufferedWriter(new FileWriter(file1.toFile()));
            if (trovato && iRigaF < lines.size()) {
                lines.set(iRigaF, nuovaL);
                
            } else {
                nuovaL = nuovaL + sugg.trim();
                lines.add(nuovaL);
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
}