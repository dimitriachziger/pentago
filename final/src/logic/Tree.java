package logic;

import java.math.BigInteger;

/**
 * Erstellt einen Baum und berechnet den besten Computerzug.
 *
 * @author Dimitri Solodownik, inf102736
 */
public class Tree {

    /* Anzahl Rotationnen */
    private static final int NUMOFROTATIONS = Quadrant.values().length * 2;
    /* Rotation der ersten Ebene */
    private Rotation rotation;
    /* Baumtiefe */
    private int depth;
    /* beste Einfuegeposition */
    private int insertBitPos;
    /* Spielfeldgroesse */
    private static final int FIELDSIZE = 36;
    /* Strategie des Computers */
    private boolean defensive;
    /* Rotator */
    private Rotator rotator = new Rotator();

    /**
     * Berecht den besten Computerzug mit dem Alpha-Beta-Algorihtmus. Nach der
     * Durchfuehrung kann der Zug mit den Methoden getRotation() und
     * getInsertPos() entnommen werden.
     *
     * @param bigInt Spielfeld
     * @param depth Baumtiefe
     * @param defensive Strategie des Computers
     */
    public void startAI(BigInteger bigInt, int depth, boolean defensive) {
        this.depth = depth;
        this.defensive = defensive;
        int bestMoveValuation = alphabeta(bigInt, Integer.MIN_VALUE, Integer.MAX_VALUE, true, this.depth);
        // wenn der Sieg des Spieler nicht mehr verhindert werden kann, soll
        // weiterhin trotzdem ein guter Zug durchgefuehrt werden
        if (bestMoveValuation == Integer.MIN_VALUE) {
            startAI(bigInt, depth - 1, defensive);
        }
    }

    /**
     * Alpha-Beta-Algorithmus fuer die Berechnung des besten Computerzuges.
     *
     * @param bigInt Spielfeld
     * @param alpha beste Bewertung fuer den Max-Spieler
     * @param beta beste Bewertung fuer den Min-Spieler
     * @param maximize Angabe ob maximiert werden soll
     * @param depth Baumtiefe
     * @return Bewertung des Spielfeldes nach dem besten Computerzug
     */
    private int alphabeta(BigInteger bigInt, int alpha, int beta, boolean maximize, int depth) {
        // alle Felder besetzt -> Untentschieden
        if (bigInt.bitCount() == FIELDSIZE) {
            return 0;
        } // einer der Spieler hat gewonnen
        else if (bigInt.bitCount() >= 10
                && WinValidator.getSetOfWinBits(bigInt, Player.COMPUTER) != null) {
            return Integer.MAX_VALUE;
        } else if (bigInt.bitCount() >= 10
                && WinValidator.getSetOfWinBits(bigInt, Player.HUMANPLAYER) != null) {
            return Integer.MIN_VALUE;
        } else if (depth == 0) {
            return new Board(bigInt).evaluate(defensive);
        }
        // Alghorithmusschritt durchfuehren
        Rotation rtn = new Rotation();

        if (maximize) {
            // bei 71.Bit anfangen und dann immer 2 weniger
            for (int i = FIELDSIZE * 2 - 1; i >= 0; i = i - 2) {

                // beide Bits nicht gesetzt -> leeres Feld
                if (!bigInt.testBit(i) && !bigInt.testBit(i - 1)) {
                    BigInteger child = bigInt.setBit(i - 1);

                    // 8 Rotationen
                    for (int k = 0; k < NUMOFROTATIONS; k++) {
                        BigInteger rotated = rotator.performRotation(child, rtn);
                        // rekurisve Auswertung
                        int valuationForChild = alphabeta(rotated, alpha, beta, false, depth - 1);

                        //Bewertung ist bisher beste Bewertung
                        if (valuationForChild > alpha) {
                            alpha = valuationForChild;

                            if (depth == this.depth) {
                                this.rotation = rtn.copy();
                                this.insertBitPos = i;
                            }
                        }

                        // maximale Bewertung fuer Max-Spieler
                        if (alpha >= beta) {
                            if (depth == this.depth) {
                                this.rotation = rtn.copy();
                                this.insertBitPos = i;
                            }
                            return alpha;
                        }
                        rtn.next();
                    }
                }
            }
            return alpha;
        } else {
            // bei 71 anfangen und dann immer 2 weniger
            for (int i = FIELDSIZE * 2 - 1; i >= 0; i = i - 2) {
                if (!bigInt.testBit(i) && !bigInt.testBit(i - 1)) {
                    
                    BigInteger child = bigInt.setBit(i);
                    // 8 Rotationen
                    for (int k = 0; k < NUMOFROTATIONS; k++) {

                        BigInteger rotated = rotator.performRotation(child, rtn);
                        int valuationForChild = alphabeta(rotated, alpha, beta, true, depth - 1);

                        // Bewertung ist bisher die Beste für den Min-Player
                        if (valuationForChild < beta) {
                            beta = valuationForChild;
                        }

                        // maximale(negative) Bewertung fuer Min-Spieler
                        if (beta <= alpha) {
                            return beta;
                        }
                        rtn.next();
                    }
                }
            }
            return beta;
        }
    }

    /**
     * Liefert die Rotatian fuer den besten Computerzug. Zuvor Auswertung des
     * Spielbaums durch startAI().
     *
     * @return Rotation
     */
    public Rotation getRotation() {
        return this.rotation;
    }

    /**
     * Liefert die Bit-Position fuer den besten Computerzug. Zuvor Auswertung
     * des Spielbaums durch startAI().
     *
     * @return
     */
    public int getInsertPos() {
        return this.insertBitPos;
    }

}
