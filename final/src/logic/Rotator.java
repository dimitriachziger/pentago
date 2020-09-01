package logic;

import java.math.BigInteger;

/**
 * Rotiert einen BigInteger-Wert.
 * 
 * @author Dimitri Solodownik, inf102736
 */
public class Rotator {

    /**
     * Schreibt die einzelnen Bits des BigInteger-Wertes in ein char-Array.
     *
     * @param value BigInteger-Wert
     * @return Array mit den Bits des Wertes
     */
    private char[] toCharArray(BigInteger value) {
        String str = value.toString(2);

        int strLength = str.length();
        if (strLength < 36 * 2) {
            int diff = 36 * 2 - strLength;
            String missing0 = String.format("%0" + diff + "d", 0);
            str = missing0 + str;
        }

        return str.toCharArray();
    }

    /**
     * Preuft ob eine Rotation ggf keine Auswirkung hat und somit uebersprungen
     * werden kann. Das betrifft die BigInteger-Werte die an der Stelle des
     * Quadranten keine Bits gesetzt haben, bzw. nur die beiden Bits fuer das
     * mittlere Feld.
     *
     * @param bigInt Wert der rotiert wird.
     * @param quadrant Quadrant der rotiert wird.
     * @return Rotation kann uebersrungen werden
     */
    private boolean canSkip(BigInteger bigInt, Quadrant quadrant) {
        // Quadranten wo keine Bits gesetzt sind, bzw nur die Bits für das mittlere Feld
        BigInteger emptyMaskTopLeft = new BigInteger("111111000000110011000000111111000000000000000000000000000000000000000000", 2);
        BigInteger emptyMaskTopRight = new BigInteger("000000111111000000110011000000111111000000000000000000000000000000000000", 2);
        BigInteger emptyMaskBottomLeft = new BigInteger("000000000000000000000000000000000000111111000000110011000000111111000000", 2);
        BigInteger emptyMaskBottomRight = new BigInteger("000000000000000000000000000000000000000000111111000000110011000000111111", 2);
        // bei leerem Quadranten muss nicht gedreht werden
        switch (quadrant) {
            case TOP_LEFT:
                if (bigInt.and(emptyMaskTopLeft).bitCount() == 0) {
                    return true;
                }
                break;
            case TOP_RIGHT:
                if (bigInt.and(emptyMaskTopRight).bitCount() == 0) {
                    return true;
                }
                break;
            case BOTTOM_LEFT:
                if (bigInt.and(emptyMaskBottomLeft).bitCount() == 0) {
                    return true;
                }
                break;
            case BOTTOM_RIGHT:
                if (bigInt.and(emptyMaskBottomRight).bitCount() == 0) {
                    return true;
                }
                break;
        }
        return false;
    }

    /**
     * Fuehrt eine Rotation der Bits des uebergebenen Wertes durch. Falls die
     * signifikanten Bits nicht gesetzt sind, bleibt der Wert unveraendert.
     *
     * @param bigInt Wert der rotiert wird.
     * @param rtn Rotation die durchgefuehrt wird.
     * @return Wert nach der Rotation
     */
    public BigInteger performRotation(BigInteger bigInt, Rotation rtn) {
        Quadrant qdr = rtn.getQuadrant();
        boolean toLeft = rtn.getToLeft();
        
        if (canSkip(bigInt, qdr)) {
            return bigInt;
        }

        int start = 0;
        switch (qdr.ordinal()) {
            case 0:
                start = 0;
                break;
            case 1:
                start = 6;
                break;
            case 2:
                start = 36;
                break;
            case 3:
                start = 42;
                break;
        }


        char[] charArr = toCharArray(bigInt);

        if (toLeft) {
            for (int i = 0; i < 2; i++) {
                // 00 speichern
                // 01 speichern
                char tempBit0 = charArr[start + (i * 2)];
                char tempBit1 = charArr[start + 1 + (i * 2)];

                // 00 setzen mit 02
                // 01 setzen mit 12
                charArr[start + (i * 2)] = charArr[start + 4 + (i * 12)];
                charArr[start + 1 + (i * 2)] = charArr[start + 5 + (i * 12)];

                // 02 setzen mit 22
                // 12 setzen mit 21
                charArr[start + 4 + (i * 12)] = charArr[start + 28 - (i * 2)];
                charArr[start + 5 + (i * 12)] = charArr[start + 29 - (i * 2)];

                // 22 setzen mit 20
                // 21 setzen mit 10
                charArr[start + 28 - (i * 2)] = charArr[start + 24 - (i * 12)];
                charArr[start + 29 - (i * 2)] = charArr[start + 25 - (i * 12)];

                // 20 setzen mit 00
                // 10 setzen mit 01
                charArr[start + 24 - (i * 12)] = tempBit0;
                charArr[start + 25 - (i * 12)] = tempBit1;
            }

        } else {
            // Rechtsrotation
            for (int i = 0; i < 2; i++) {

                // 00 speichern
                // 01 speichern
                char tempBit0 = charArr[start + (i * 2)];
                char tempBit1 = charArr[start + 1 + (i * 2)];

                // 00 setzen mit 20
                // 01 setzen mit 10
                charArr[start + (i * 2)] = charArr[start + 24 - (i * 12)];
                charArr[start + 1 + (i * 2)] = charArr[start + 25 - (i * 12)];

                // 20 setzen mit 22
                // 10 setzen mit 21
                charArr[start + 24 - (i * 12)] = charArr[start + 28 - (i * 2)];
                charArr[start + 25 - (i * 12)] = charArr[start + 29 - (i * 2)];

                // 22 setzen mit 02
                // 21 setzen mit 12
                charArr[start + 28 - (i * 2)] = charArr[start + 4 + (i * 12)];
                charArr[start + 29 - (i * 2)] = charArr[start + 5 + (i * 12)];

                // 02 setzen mit 00
                // 12 setzen mit 01
                charArr[start + 4 + (i * 12)] = tempBit0;
                charArr[start + 5 + (i * 12)] = tempBit1;
            }
        }

        return new BigInteger(new String(charArr), 2);
    }
}
