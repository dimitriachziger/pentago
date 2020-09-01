import java.io.IOException;
import logic.Board;
import logic.Player;
import logic.WinValidator;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Testet das erkennen eines Gewinns durch die Klasse WinValidator.
 * @author Dimitri Solodownik, inf102736
 */
public class WinAndLoseTest {

    @Test
    public void ComputerWinHorizontal() throws IOException {
        Board field = new Board("CCCCC-"
                + "-PP---"
                + "----P-"
                + "--P---"
                + "----P-"
                + "---P--");

        assertTrue(WinValidator.getSetOfWinBits(field.toBigInt(), Player.COMPUTER) != null);
        assertNull(WinValidator.getSetOfWinBits(field.toBigInt(), Player.HUMANPLAYER));

    }

    @Test
    public void computerWinVertical() throws IOException {
        Board field = new Board("C-----"
                + "CPP---"
                + "C---P-"
                + "C-P---"
                + "C---P-"
                + "---P--");

        assertTrue(WinValidator.getSetOfWinBits(field.toBigInt(), Player.COMPUTER) != null);
        assertNull(WinValidator.getSetOfWinBits(field.toBigInt(), Player.HUMANPLAYER));
    }

    @Test
    public void computerWinDiagonal() throws IOException {
        Board field = new Board("------"
                + "PCP---"
                + "--C-P-"
                + "--PC--"
                + "C--PC-"
                + "---P-C");

        assertTrue(WinValidator.getSetOfWinBits(field.toBigInt(), Player.COMPUTER) != null);
        assertNull(WinValidator.getSetOfWinBits(field.toBigInt(), Player.HUMANPLAYER));
    }

    @Test
    public void playerWinHorizontal() throws IOException {
        Board field = new Board("PPPPP-"
                + "-C--C-"
                + "--C---"
                + "-C----"
                + "------"
                + "---C--");

        assertNull(WinValidator.getSetOfWinBits(field.toBigInt(), Player.COMPUTER));
        assertTrue(WinValidator.getSetOfWinBits(field.toBigInt(), Player.HUMANPLAYER) != null);
    }

    @Test
    public void playerWinVertical() throws IOException {
        Board field = new Board("P-----"
                + "PC--C-"
                + "P-C---"
                + "PC----"
                + "P-----"
                + "---C--");

        assertNull(WinValidator.getSetOfWinBits(field.toBigInt(), Player.COMPUTER));
        assertTrue(WinValidator.getSetOfWinBits(field.toBigInt(), Player.HUMANPLAYER) != null);

    }

    @Test
    public void playerWinDiagonal() throws IOException {
        Board field = new Board("------"
                + "-PC-C-"
                + "--PC--"
                + "PC-P--"
                + "P---P-"
                + "---C-P");

        assertNull(WinValidator.getSetOfWinBits(field.toBigInt(), Player.COMPUTER));
        assertTrue(WinValidator.getSetOfWinBits(field.toBigInt(), Player.HUMANPLAYER) != null);
    }

    @Test
    public void playerWinMultiplesLines() throws IOException {

        Board f0 = new Board("PPPPP-"
                + "-P--P-"
                + "--P-P-"
                + "---PP-"
                + "----P-"
                + "------");

        assertEquals(3, WinValidator.getSetOfWinBits(f0.toBigInt(), Player.HUMANPLAYER).size());
        assertNull(WinValidator.getSetOfWinBits(f0.toBigInt(), Player.COMPUTER));

    }

    @Test
    public void noWin() throws IOException {
        Board f1 = new Board("----PP"
                + "----PP"
                + "--PP--"
                + "PP-PP-"
                + "P----P"
                + "P-----");

        assertNull(WinValidator.getSetOfWinBits(f1.toBigInt(), Player.HUMANPLAYER));
        assertNull(WinValidator.getSetOfWinBits(f1.toBigInt(), Player.COMPUTER));

    }

    @Test
    public void draw_bothPlayerWin() throws IOException {
        Board f1 = new Board("------"
                + "PCCCCC"
                + "P-----"
                + "P-----"
                + "P-----"
                + "P-----");

        assertTrue(WinValidator.getSetOfWinBits(f1.toBigInt(), Player.HUMANPLAYER) != null);
        assertTrue(WinValidator.getSetOfWinBits(f1.toBigInt(), Player.COMPUTER) != null);

    }

}
