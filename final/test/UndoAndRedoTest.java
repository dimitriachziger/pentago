import logic.GameLogic;
import logic.Board;
import logic.Player;
import logic.Quadrant;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Testet das Zuruecknehmen und Wiederholen von Spielzuegen.
 * 
 * @author Dimitri Solodownik, inf102736
 */
public class UndoAndRedoTest {

    @Test
    public void undo() throws InterruptedException {
        Board field = new Board();
        GameLogic logic = new GameLogic(new FakeGUI(), field);

        logic.handlePlayerMove(0, 0, 0, 0);
        logic.handleRotation(Quadrant.TOP_LEFT, true);
        assertEquals(35, logic.getField().getNumberOfEmptyPlaces());
        // Computerzug durchfuehren
        logic.proceedAfterGuiRotation();
        assertEquals(34, logic.getField().getNumberOfEmptyPlaces());

        // bothPlayerMoved setzen
        logic.proceedAfterGuiRotation();
        logic.undoMove();

        assertEquals(36, logic.getField().getNumberOfEmptyPlaces());
    }
    
    @Test
    public void undo_notPossible() throws InterruptedException {
        Board field = new Board();
        GameLogic logic = new GameLogic(new FakeGUI(), field);

        logic.handlePlayerMove(0, 0, 0, 0);
        logic.handleRotation(Quadrant.TOP_LEFT, true);
        assertEquals(35, logic.getField().getNumberOfEmptyPlaces());
        // keinen Computerzug durchfuehren
        logic.undoMove();
        assertEquals(35, logic.getField().getNumberOfEmptyPlaces());
    }

    @Test
    public void redo() throws InterruptedException {
        Board field = new Board();
        GameLogic logic = new GameLogic(new FakeGUI(), field);

        logic.handlePlayerMove(0, 0, 0, 0);
        logic.handleRotation(Quadrant.TOP_LEFT, true);
        assertEquals(35, logic.getField().getNumberOfEmptyPlaces());
        // Computerzug durchfuehren
        logic.proceedAfterGuiRotation();
        assertEquals(34, logic.getField().getNumberOfEmptyPlaces());

        // bothPlayerMoved setzen
        logic.proceedAfterGuiRotation();
        logic.undoMove();
        assertEquals(36, logic.getField().getNumberOfEmptyPlaces());

        // Spielzug Wiederholen
        logic.redoMove();
        assertEquals(34, logic.getField().getNumberOfEmptyPlaces());
        assertEquals(Player.HUMANPLAYER, logic.getField().getMarble(0, 0).getPlayer());
    }
    
        @Test
    public void redo_notPossible() throws InterruptedException {
        Board field = new Board();
        GameLogic logic = new GameLogic(new FakeGUI(), field);

        logic.handlePlayerMove(0, 0, 0, 0);
        logic.handleRotation(Quadrant.TOP_LEFT, true);
        assertEquals(35, logic.getField().getNumberOfEmptyPlaces());
        // Computerzug durchfuehren
        logic.proceedAfterGuiRotation();
        assertEquals(34, logic.getField().getNumberOfEmptyPlaces());

        // bothPlayerMoved setzen
        logic.proceedAfterGuiRotation();
        logic.undoMove();
        assertEquals(36, logic.getField().getNumberOfEmptyPlaces());

        // Steinplatzierung vor Wiederholung
        logic.handlePlayerMove(0, 0, 1, 1);
        logic.handleRotation(Quadrant.TOP_LEFT, true);
        // Spielzug Wiederholen
        logic.redoMove();
        assertEquals(35, logic.getField().getNumberOfEmptyPlaces());
    }
}
