import logic.GUIConnector;
import logic.GameLogic;
import logic.Player;
import logic.Quadrant;

/**
 * Imitiert eine Gui. Aufrufende Methoden machen nichts.
 * 
 * @author Dimitri Solodownik, inf102736
 */
public class FakeGUI implements GUIConnector {

    @Override
    public void setImage(Player pl, int posX, int posY) {

    }

    @Override
    public void rotateQuadrant(Quadrant qdr, boolean leftTwist, long duration, GameLogic logic) {
    }

    @Override
    public void writeLog(String str) {
    }
    

    @Override
    public void alertWin(Player p, GameLogic logic) {
    }

    @Override
    public void alertDraw(Player p0, Player p1, GameLogic logic) {
    }


    @Override
    public void clearLog(boolean allEntrys) {

    }

    @Override
    public void alertError(String err) {

    }

    @Override
    public void markWin(int x, int y) {

    }

    @Override
    public void resetImages() {

    }

    @Override
    public void disableItmSaveAndHistoryButtons(boolean disable) {
    }

    @Override
    public void disableRotButtons(boolean disable) {
    }
    
}
