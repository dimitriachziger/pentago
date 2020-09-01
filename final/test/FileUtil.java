
import java.io.File;
import java.io.IOException;
import logic.Board;
import logic.GameLogic;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * Testet das Laden und Speichern von Spielstaenden.
 * @author Dimitri Solodownik, inf102736
 */
public class FileUtil {

    @Test
    public void loadGame() throws IOException {
        GameLogic gl = new GameLogic(new FakeGUI(), null);
        File f = new File("test/testdata/" + "fileUtil_loadGameTest.pen");
        gl.loadFile(f);

        Board expected = new Board("-PPPC-"
                + "CCCPPC"
                + "-P--CP"
                + "C-PPCP"
                + "PCPPCC"
                + "-PCCC-");

        assertEquals(expected.toString(), gl.getField().toString());
    }

    @Test
    public void saveGame() throws IOException {
        Board field = new Board("CP---P"
                + "C-PPC-"
                + "---C--"
                + "P-C--P"
                + "C---P-"
                + "--C--P");

        File f = new File("test/testdata/" + "fileUtil_saveGameTest.pen");
        GameLogic gl = new GameLogic(new FakeGUI(), field);
        gl.saveGame(f);

        GameLogic gl_1 = new GameLogic(new FakeGUI(), null);
        gl_1.loadFile(f);

        assertEquals(field.toString(), gl_1.getField().toString());
    }

}
