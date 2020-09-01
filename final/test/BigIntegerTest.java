
import java.io.IOException;
import java.math.BigInteger;
import logic.Board;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Testet die Umwandlung eines Board in ein BigInteger, sowie die Erstellung
 * eines Board aus einem BigInteger
 *
 * @author Dimitri Solodownik, inf102736
 */
public class BigIntegerTest {

    @Test
    public void fieldToBigInteger() throws IOException {
        // Computer-Bits 01
        // Player-Bits 10
        Board field = new Board("P---C-"
                + "C---PC"
                + "CP----"
                + "----CC"
                + "------"
                + "--C--C");

        BigInteger value = field.toBigInt();

        assertEquals(new BigInteger("100000000100010000001001011000000000000000000101000000000000000001000001", 2), value);
        assertTrue(value.testBit(71));
        assertFalse(value.testBit(70));
        assertTrue(value.testBit(0));
        assertFalse(value.testBit(1));
    }

    @Test
    public void fieldToBigInteger2() throws IOException {
        Board field = new Board("-PPPC-"
                + "CCCPPC"
                + "CPCPCP"
                + "PPCPCC"
                + "CCPCPC"
                + "CPCPCP");

        assertEquals(new BigInteger("001010100100010101101001011001100110101001100101010110011001011001100110", 2), field.toBigInt());
    }

    @Test
    public void fieldFromBigInteger() throws IOException {
        BigInteger value = new BigInteger("001010100100010101101001011001100110101001100101010110011001011001100110", 2);

        Board expected = new Board("-PPPC-"
                + "CCCPPC"
                + "CPCPCP"
                + "PPCPCC"
                + "CCPCPC"
                + "CPCPCP");

        Board field = new Board(value);
        assertEquals(expected.toString(), field.toString());
    }

}
