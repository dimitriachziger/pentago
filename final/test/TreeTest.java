import java.io.IOException;
import java.math.BigInteger;
import logic.Board;
import logic.Quadrant;
import logic.GameLogic;
import logic.Tree;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Testet die Berechnung des besten Computerzuges durch die Klasse Tree.
 * 
 * @author Dimitri Solodownik, inf102736
 */
public class TreeTest {

    private static final int MAXDEPTH = 2;

    @Test
    public void computerCanWinHorizontal() throws IOException {

        // C an 0 5 und TOP_RIGHT linksdreh
        Board field = new Board("CCC---"
                + "P----C"
                + "P-----"
                + "---P--"
                + "----P-"
                + "-P----");

        BigInteger bigInt = field.toBigInt();
        Tree tree = new Tree();
        tree.startAI(bigInt, MAXDEPTH, true);
        assertEquals(0, GameLogic.getXCoordFromBitPos(tree.getInsertPos()));
        assertEquals(5, GameLogic.getYCoordFromBitPos(tree.getInsertPos()));
        assertEquals(Quadrant.TOP_RIGHT, tree.getRotation().getQuadrant());
        assertEquals(true, tree.getRotation().getToLeft());
    }

    @Test
    public void playerCanWinWithTheNextMove() throws IOException {

        // Spieler gewinnt durch Platzierung 5 3
        Board field = new Board("------"
                + "-C--C-"
                + "--C---"
                + "-C--P-"
                + "-P-C-C"
                + "PPP-P-");

        BigInteger bigInt = field.toBigInt();
        Tree tree = new Tree();
        tree.startAI(bigInt, MAXDEPTH, true);
        assertEquals(5, GameLogic.getXCoordFromBitPos(tree.getInsertPos()));
        assertEquals(3, GameLogic.getYCoordFromBitPos(tree.getInsertPos()));
    }

    @Test
    public void computerCanWinVertical() throws IOException {
        // Computer gewinnt durch Platzierung 0 0 und Rotation TOP_LEFT rechts
        Board field = new Board("-CC---"
                + "P----C"
                + "PP----"
                + "--CP--"
                + "--CPP-"
                + "-P----");

        BigInteger bigInt = field.toBigInt();
        Tree tree = new Tree();
        tree.startAI(bigInt, MAXDEPTH, true);
        assertEquals(0, GameLogic.getXCoordFromBitPos(tree.getInsertPos()));
        assertEquals(0, GameLogic.getYCoordFromBitPos(tree.getInsertPos()));
        assertEquals(Quadrant.TOP_LEFT, tree.getRotation().getQuadrant());
        assertEquals(false, tree.getRotation().getToLeft());

    }

    @Test
    public void computerCanWinDiagonal() throws IOException {
        // Computer gewinnt diagonal nach beliebieger Platzierung und 
        // Rotation TOP_RIGHT nach links
        Board field = new Board("-C-C--"
                + "P-C--C"
                + "P-CP-P"
                + "---PCC"
                + "--P--C"
                + "-P----");

        BigInteger bigInt = field.toBigInt();
        Tree tree = new Tree();
        tree.startAI(bigInt, MAXDEPTH, true);
        assertEquals(Quadrant.TOP_RIGHT, tree.getRotation().getQuadrant());
        assertEquals(true, tree.getRotation().getToLeft());

    }


    @Test
    public void defensiveStrategy() throws IOException {
        // aufgrund defensiver Strategie muessen die Spielersteine durch 
        // Platzierung auf 4 1 blockiert werden
        Board field = new Board("------"
                + "-C----"
                + "------"
                + "------"
                + "P-P---"
                + "------");

        BigInteger bigInt = field.toBigInt();
        Tree tree = new Tree();
        tree.startAI(bigInt, MAXDEPTH, true);
        assertEquals(4, GameLogic.getXCoordFromBitPos(tree.getInsertPos()));
        assertEquals(1, GameLogic.getYCoordFromBitPos(tree.getInsertPos()));

    }
}
