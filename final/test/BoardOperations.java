import java.io.IOException;
import logic.Board;
import logic.GameLogic;
import logic.Player;
import logic.Quadrant;
import logic.Rotation;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 * Testet diverse Methoden der Klasse Board.
 * 
 * @author Dimitri Solodownik, inf102736
 */
public class BoardOperations {

    @Test
    public void createEmptyField() throws IOException {
        Board field = new Board("------"
                + "------"
                + "------"
                + "------"
                + "------"
                + "------");

        assertEquals(36, field.getNumberOfEmptyPlaces());
        assertEquals(36, field.size());
        assertEquals(6, field.lineSize());
        assertEquals(Player.EMPTY, field.getMarble(0, 0).getPlayer());
    }

    @Test
    public void createFieldFromString() throws IOException {
        Board field = new Board("CP---P"
                + "C-PPC-"
                + "---C--"
                + "P-C--P"
                + "C---P-"
                + "--C--P");

        assertEquals(36, field.size());
        assertEquals(21, field.getNumberOfEmptyPlaces());
        assertEquals(6, field.lineSize());
        assertEquals(Player.COMPUTER, field.getMarble(0, 0).getPlayer());
        assertEquals(Player.HUMANPLAYER, field.getMarble(0, 1).getPlayer());
    }

    @Test
    public void insertMarble() throws IOException {
        Board field = new Board("------"
                + "------"
                + "------"
                + "------"
                + "------"
                + "------");

        assertEquals(36, field.getNumberOfEmptyPlaces());
        assertEquals(36, field.size());
        assertEquals(6, field.lineSize());
        field.insert(Player.COMPUTER, 1, 1);
        assertEquals(Player.EMPTY, field.getMarble(0, 0).getPlayer());
        assertEquals(Player.COMPUTER, field.getMarble(1, 1).getPlayer());
    }

    @Test
    public void fieldToString() throws IOException {
        Board f0 = new Board("CP---P"
                + "C-PPC-"
                + "---C--"
                + "P-C--P"
                + "C---P-"
                + "--C--P");

        String expected = "CP---P\nC-PPC-\n---C--\nP-C--P\nC---P-\n--C--P";
        assertEquals(expected, f0.toString());
    }

    @Test
    public void fieldToString2() throws IOException {
        Board field = new Board("------"
                + "------"
                + "------"
                + "------"
                + "------"
                + "------");

        String expected = "------\n------\n------\n------\n------\n------";
        assertEquals(expected, field.toString());
    }

    @Test
    public void rotateFourthQuadrantLogic() throws IOException {
        Board field = new Board("----C-"
                + "C---PC"
                + "CP----"
                + "---PCC"
                + "----PP"
                + "--CP-C");

        GameLogic logic = new GameLogic(new FakeGUI(), field);
        logic.getField().performRotation(new Rotation(Quadrant.BOTTOM_RIGHT, true));

        Board expectedField = new Board("----C-"
                + "C---PC"
                + "CP----"
                + "---CPC"
                + "---CP-"
                + "--CP-P");

        assertEquals(expectedField.toBigInt(), logic.getField().toBigInt());

    }

    @Test
    public void rightRotationFirstQuadrant() throws IOException {
        Board initialField = new Board("CP---P"
                + "C-PPC-"
                + "---C--"
                + "P-C--P"
                + "C---P-"
                + "--C--P");

        GameLogic logic = new GameLogic(new FakeGUI(), initialField);
        logic.handlePlayerMove(0, 0, 0, 2);
        logic.getField().performRotation(new Rotation(Quadrant.TOP_LEFT, false));

        Board expected = new Board("-CC--P"
                + "--PPC-"
                + "-PPC--"
                + "P-C--P"
                + "C---P-"
                + "--C--P");

        assertEquals(expected.toString(), logic.getField().toString());
    }

    @Test
    public void rotateFourthQuadrantLeft2() throws IOException {
        Board initialField = new Board("------"
                + "------"
                + "------"
                + "---C-P"
                + "-----C"
                + "-----P");

        GameLogic logic = new GameLogic(new FakeGUI(), initialField);
        logic.getField().performRotation(new Rotation(Quadrant.BOTTOM_RIGHT, true));

        Board expected = new Board("------"
                + "------"
                + "------"
                + "---PCP"
                + "------"
                + "---C--");

        assertEquals(expected.toString(), logic.getField().toString());

    }
}
