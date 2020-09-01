import java.io.IOException;
import static org.junit.Assert.*;

import java.math.BigInteger;

import org.junit.Test;

import logic.Board;
import logic.Quadrant;
import logic.Rotation;
import logic.Rotator;

/**
 * Testet die Rotation eines Quadranten durch den Rotator.
 * @author Dimitri Solodownik, inf102736
 */
public class RotatorTest {


    @Test
    public void setNextRotation() {
        Rotation rot = new Rotation(Quadrant.TOP_LEFT, true);
        rot.next();
        assertEquals(Quadrant.TOP_LEFT, rot.getQuadrant());
        assertFalse(rot.getToLeft());
        rot.next();
        assertEquals(Quadrant.TOP_RIGHT, rot.getQuadrant());
        assertTrue(rot.getToLeft());
        rot.next();
        assertEquals(Quadrant.TOP_RIGHT, rot.getQuadrant());
        assertFalse(rot.getToLeft());


        Rotation last = new Rotation(Quadrant.BOTTOM_RIGHT, false);
        last.next();
        assertEquals(Quadrant.TOP_LEFT, last.getQuadrant());
        assertTrue(last.getToLeft());
    }

    @Test
    public void rotateFirstQuadrantRight() throws IOException {
        Board field = new Board("----C-"
                + "C---PC"
                + "CP----"
                + "----CC"
                + "------"
                + "--C--C");

        BigInteger bigInt = field.toBigInt();

        Board expectedField = new Board("CC--C-"
                + "P---PC"
                + "------"
                + "----CC"
                + "------"
                + "--C--C");
        
        BigInteger expectedValue = expectedField.toBigInt();

        Rotator rot = new Rotator();
        assertEquals(expectedValue, rot.performRotation(bigInt,  new Rotation(Quadrant.TOP_LEFT, false)));

    }

    @Test
    public void rotateFourthQuadrantLeft() throws IOException {
        Board field = new Board("----C-"
                + "C---PC"
                + "CP----"
                + "---PCC"
                + "----PP"
                + "--CP-C");

        BigInteger bigInt = field.toBigInt();

        Board expectedField = new Board("----C-"
                + "C---PC"
                + "CP----"
                + "---CPC"
                + "---CP-"
                + "--CP-P");

        Rotator rotator = new Rotator();
        assertEquals(expectedField.toBigInt(), rotator.performRotation(bigInt, new Rotation(Quadrant.BOTTOM_RIGHT, true)));

    }

    @Test
    public void rotateFourthQuadrantRight() throws IOException {
        Board field = new Board("----C-"
                + "C---PC"
                + "CP----"
                + "---PCC"
                + "----PP"
                + "--CP-C");

        BigInteger bigInt = field.toBigInt();

        Board expectedField = new Board("----C-"
                + "C---PC"
                + "CP----"
                + "---P-P"
                + "----PC"
                + "--CCPC");
        BigInteger expected = expectedField.toBigInt();

        Rotator rotator = new Rotator();
        assertEquals(expected, rotator.performRotation(bigInt, new Rotation(Quadrant.BOTTOM_RIGHT, false)));

    }
}
