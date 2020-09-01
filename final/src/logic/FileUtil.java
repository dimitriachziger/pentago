package logic;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Realisiert das Speichern und Laden einer Spielfeldbelegung
 *
 * @author Dimitri Solodownik, inf102736
 */
public class FileUtil {

    /**
     * Realisiert das Laden einer Spielfeldbelegung aus einer Datei. Erstellt
     * einen String aus dem Inhalt der Datei und fuegt nach jeder Zeile, einen
     * Zeilenumbrueche hinzu
     *
     * @param f Datei aus der gelesen wird
     * @return das erstelle Spielfeld
     * @throws java.io.FileNotFoundException Datei nicht gefunden
     * @throws IOException Syntaxfehler beim Lesen der Datei
     */
    public String readGamefield(File f) throws FileNotFoundException, IOException {
        BufferedReader reader = new BufferedReader(new FileReader(f));
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            stringBuilder.append(line);
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();

    }

    /**
     * Schreibt die aktuelle Spielsituation in eine Datei
     *
     * @param file Datei in die Geschrieben wird
     * @param field Spielfeld mit der aktuellen Spielbelegung
     * @throws IOException Fehler beim Schreiben des Spielstandes
     */
    public void writeGamefield(File file, String field) throws IOException {
        try (
                FileWriter fileWriter = new FileWriter(file)) {
            fileWriter.write(field);
        }
    }

}
