import java.io.IOException;
import logic.GameLogic;
import logic.Board;
import logic.Player;
import logic.Quadrant;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Testet die Korrektheit des Spielablauf.
 * @author Dimitri Solodownik, inf102736
 */
public class GameplayTest {

    @Test
    public void insertCorrect() throws IOException {
        Board field = new Board("CP---P"
                + "C-PPC-"
                + "---C--"
                + "P-C--P"
                + "C---P-"
                + "--C--P");
        
        int empty = field.getNumberOfEmptyPlaces();
        assertEquals(Player.EMPTY, field.getMarble(0, 3).getPlayer());
        assertTrue(field.insert(Player.HUMANPLAYER, 0, 3));
        assertEquals(Player.HUMANPLAYER, field.getMarble(0, 3).getPlayer());
        assertEquals(empty-1,field.getNumberOfEmptyPlaces());
    }

    @Test
    public void insertIncorrect() throws IOException {
        Board field = new Board("CP---P"
                + "C-PPC-"
                + "---C--"
                + "P-C--P"
                + "C---P-"
                + "--C--P");

        int empty = field.getNumberOfEmptyPlaces();
        assertEquals(Player.COMPUTER, field.getMarble(0, 0).getPlayer());
        // Zelle belegt
        assertFalse(field.insert(Player.HUMANPLAYER, 0, 0));
        // Zelle unveraendert
        assertEquals(Player.COMPUTER, field.getMarble(0, 0).getPlayer());
        assertEquals(empty,field.getNumberOfEmptyPlaces());

    }

    @Test
    public void rotationNotPossible() throws IOException {
        Board field = new Board("CP---P"
                + "C-PPC-"
                + "---C--"
                + "P-C--P"
                + "C---P-"
                + "--C--P");
        
        String expected = field.toString();
        
        GameLogic logic = new GameLogic(new FakeGUI(), field);
        // Spiel wartet auf das Plazieren eines Spielsteins des Spielers
        // es wird keine Rotation ausgefuehrt
        logic.handleRotation(Quadrant.TOP_LEFT, true);
        // Spielfeld unveraendert
        assertEquals(expected, logic.getField().toString());

    }

    @Test
    public void validRotation() throws IOException {
        /*  C P -
            C - P
            - - - */
        
        Board initialField = new Board("CP---P"
                + "C-PPC-"
                + "---C--"
                + "P-C--P"
                + "C---P-"
                + "--C--P");
        
        GameLogic logic = new GameLogic(new FakeGUI(), initialField);

        logic.handlePlayerMove(0, 0, 0, 2);
        
        logic.handleRotation(Quadrant.TOP_LEFT, true);
        
        Board expected = new Board("PP---P"
                + "P--PC-"
                + "CC-C--"
                + "P-C--P"
                + "C---P-"
                + "--C--P");
        
       // nach einer Linksdrehung 
       assertEquals(expected.toString(), logic.getField().toString());
    }
    
    
    
}
