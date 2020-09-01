
import java.io.IOException;
import logic.Board;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Testet die Bewertung eines Spielfeldes durch die evaluation-Methode der Board
 * Klasse durch verschiedene Spielsituationen.
 *
 * @author Dimitri Solodownik, inf102736
 */
public class EvaluationTest {

    final static int POINTSTARGET = 40;

    @Test
    public void equally() throws IOException {
        // alle Spielfeldbelegungen sind ausgeglichen 
        Board field = new Board("P----C"
                + "------"
                + "------"
                + "------"
                + "------"
                + "P----C");

        assertEquals(0, field.evaluate(true));
        assertEquals(0, field.evaluate(false));

        Board field2 = new Board("------"
                + "-P--C-"
                + "------"
                + "------"
                + "-P--C-"
                + "------");

        assertEquals(0, field2.evaluate(true));
        assertEquals(0, field2.evaluate(false));

        Board field3 = new Board("C----P"
                + "-P--C-"
                + "------"
                + "------"
                + "-P--C-"
                + "P----C");

        assertEquals(0, field3.evaluate(true));
        assertEquals(0, field3.evaluate(false));

        Board field4 = new Board("CC--PP"
                + "------"
                + "------"
                + "------"
                + "------"
                + "------");
        // nicht blockiert, daher selbe Bewertung fuer beide Strategien
        assertEquals(0, field4.evaluate(false));
        assertEquals(0, field4.evaluate(true));
    }

    @Test
    public void computerOffensive() throws IOException {
        // Vorteil fuer Spieler in Zeile 0 und 2
        Board field = new Board("CP----"
                + "-P--C-"
                + "-P----"
                + "-C----"
                + "-P--C-"
                + "------");

        assertTrue(field.evaluate(false) < 0);

        // Vorteil fuer Spieler in Spalte 1
        Board field2 = new Board("CP----"
                + "-PC-C-"
                + "-P--P-"
                + "------"
                + "-PC-C-"
                + "------");

        assertTrue(field2.evaluate(false) < 0);

        // Vorteil Computer in Zeile 0, 2
        // Vorteil Spieler in Zeile 2
        Board field3 = new Board("PC----"
                + "-PC-C-"
                + "-P--P-"
                + "-C----"
                + "-P--C-"
                + "------");

        assertTrue(field3.evaluate(false) > 0);
    }

    @Test
    public void playerWinDiagonal() throws IOException {
        Board field0 = new Board("C--P-C"
                + "CPPCP-"
                + "-C-P-C"
                + "-PP---"
                + "-P--C-"
                + "PC----");

        assertEquals(Integer.MIN_VALUE, field0.evaluate(false));
        assertEquals(Integer.MIN_VALUE, field0.evaluate(true));

    }

    @Test
    public void playerAdvantage() throws IOException {
        Board field0 = new Board("C---P-"
                + "-C--P-"
                + "--C-P-"
                + "------"
                + "----P-"
                + "------");

        assertTrue(field0.evaluate(false) < 0);
        assertTrue(field0.evaluate(true) < 0);

        Board field1 = new Board("C---P-"
                + "-C--P-"
                + "C-C-P-"
                + "------"
                + "----P-"
                + "------");

        assertEquals(-56, field1.evaluate(false));
        assertEquals(-56, field1.evaluate(true));

        Board field2 = new Board("C---P-"
                + "-C--P-"
                + "--C-P-"
                + "----C-"
                + "----P-"
                + "------");

        assertEquals(0, field2.evaluate(false));
        assertEquals(0, field2.evaluate(true));

    }

    @Test
    public void verticalAdvantage() throws IOException {
        // Spieler kann 5 vertikale erreichen, sehr hohe Bewertung
        Board field0 = new Board("------"
                + "-C--P-"
                + "----P-"
                + "----P-"
                + "-C--P-"
                + "----C-");

        assertTrue(field0.evaluate(false) < -50);
        assertTrue(field0.evaluate(true) < -50);

        // Gewinnreihe fuer Spieler wird blockiert
        // hohe Bewertung fuer Blockierung bei defensiver Strategie
        Board field2 = new Board("----C-"
                + "-C--P-"
                + "----P-"
                + "----P-"
                + "-C--P-"
                + "----C-");

        assertTrue(field2.evaluate(false) > -10);
        assertTrue(field2.evaluate(false) < 10);
        assertTrue(field2.evaluate(true) > 20);
    }

    @Test
    public void computerWinsDiagonal() throws IOException {
        Board field = new Board("CPP--P"
                + "C-PPC-"
                + "-C-C--"
                + "P-C--P"
                + "CP-CP-"
                + "P-C-CP");

        assertEquals(Integer.MAX_VALUE, field.evaluate(true));
        assertEquals(Integer.MAX_VALUE, field.evaluate(false));
    }

    @Test
    public void defensiveStrategy_notBlocked() throws IOException {
        // Felder enthalten 2 Spielsteine die nicht blockiert werden
        Board f0 = new Board("------"
                + "-C----"
                + "------"
                + "------"
                + "P-P---"
                + "------");
        // nicht blockiert, daher selbe Bewertung fuer beide Strategien
        assertEquals(f0.evaluate(true), f0.evaluate(false));
        assertTrue(f0.evaluate(false) < 0);

        // ausgeglichene Vorteile beider Spieler
        Board f1 = new Board("C--P-P"
                + "-C----"
                + "------"
                + "------"
                + "------"
                + "------");

        assertEquals(f1.evaluate(false), f1.evaluate(true));
        assertEquals(0, f1.evaluate(false));
    }

    @Test
    public void defensiveStrategy_blocked() throws IOException {
        // Computerstein blockiert 2 Spielersteine. Defensive Strategie 
        // erhaelt zusaetzliche Punkte 

        // blockiert horizontal mittig
        Board f0 = new Board("------"
                + "-C----"
                + "------"
                + "------"
                + "PCP---"
                + "------");

        assertTrue(f0.evaluate(true) > 10);

        // blockiert links
        Board f1 = new Board("C--CPP"
                + "------"
                + "------"
                + "------"
                + "------"
                + "------");

        assertTrue(f1.evaluate(true) > 10);

        // blockiert vertikal
        Board f2 = new Board("C----P"
                + "-----C"
                + "-----P"
                + "------"
                + "------"
                + "------");

        assertTrue(f2.evaluate(true) > 10);
    }

    @Test
    public void computerAdvantange() throws IOException {
        Board field = new Board("CP---P"
                + "C-PPC-"
                + "---C--"
                + "P-C--P"
                + "C---P-"
                + "--C--P");

        assertTrue(field.evaluate(true) > 10);
        assertTrue(field.evaluate(false) > 10);
    }
}
