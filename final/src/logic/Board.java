package logic;

import java.io.IOException;
import java.math.BigInteger;

/**
 * Spielfeld mit seiner Spielsteinbelegung
 *
 * @author Dimitri Solodownik, inf102736
 */
public class Board {

    private Marble[][] field;
    private final int LINESIZE_DEFAULT = 6;
    private int linesize = LINESIZE_DEFAULT;
    private int size = LINESIZE_DEFAULT * LINESIZE_DEFAULT;
    private int emptyPositionsNum = size;
    private final int pointsTarget = 50;

    /**
     * Erstellt ein leeres Spielfeld. Fuer jede Zelle wird Marble-Empty gesetzt
     */
    public Board() {
        this.field = new Marble[LINESIZE_DEFAULT][LINESIZE_DEFAULT];
        for (int i = 0; i < LINESIZE_DEFAULT; i++) {
            for (int j = 0; j < LINESIZE_DEFAULT; j++) {
                this.field[i][j] = new Marble("-");
            }
        }
        this.linesize = this.LINESIZE_DEFAULT;
    }

    /**
     * Erstellt ein Spielfeld und setzt den Spielstand aus dem uebergebenen
     * String
     *
     * @param allocation Spielfeldbelegung als String
     * @throws java.io.IOException ungueltiges Format
     */
    public Board(String allocation) throws IOException {
        this.linesize = allocation.indexOf("\n");
        // bei den Tests wird das Feld aus String ohne Zeilenumbruch erstellt
        if (this.linesize == -1) {
            this.linesize = this.LINESIZE_DEFAULT;
        }
        this.field = new Marble[linesize][linesize];

        allocation = allocation.replaceAll("\n", "");
        int row = 0;
        for (int i = 0; i < allocation.length(); row++) {
            for (int col = 0; col < linesize; col++, i++) {
                insertMarble(allocation.charAt(i), row, col);
            }
            //Überprüfen ob der Sektor quadratisch ist
            if (row >= linesize) {
                throw new IOException("nicht quadratisch");
            }
        }
        if (row != linesize) {
            throw new IOException("nicht quadratisch");
        }
    }

    /**
     * Erstelt ein Spielfeld aus einem BigInteger-Wert
     *
     * @param value BigInteger-Repraesentation eines Feldes
     */
    public Board(BigInteger value) {
        String alloc = value.toString(2);
        if (alloc.length() < 72) {
            int diff = 72 - alloc.length();
            String newString = new String(new char[diff]).replace("\0", "0");
            alloc = newString.concat(alloc);
        }
        //assert (alloc.length() == 72);
        this.field = new Marble[linesize][linesize];
        for (int bitPos = 0, row = 0; bitPos < alloc.length(); row++) {
            for (int col = 0; col < linesize; col++, bitPos = bitPos + 2) {
                String twoDigits = alloc.substring(bitPos, bitPos + 2);
                switch (twoDigits) {
                    case "00":
                        setCell(new Marble("-"), row, col);
                        break;
                    case "01":
                        setCell(new Marble("C"), row, col);
                        break;
                    case "10":
                        setCell(new Marble("P"), row, col);
                        break;
                    default:
                        //assert (false) : "falscher Buchstabe an position " + bitPos + " - " + twoDigits + " " + value.toString(2);
                        break;
                }
            }
        }
    }

    /**
     * Fuegt den uebergebenen Spielstein an die gewuenschte Position ein
     *
     * @param mbl Marble
     * @param posX x-Koordinate
     * @param posY y-Koordinate
     */
    private void setCell(Marble mbl, int posX, int posY) {
        this.field[posX][posY] = mbl;
    }

    /**
     * Liefert das Marble, welches an dieser Position gesetzt ist.
     *
     * @param posX - x Koordinate
     * @param posY - y koordinate
     * @return Marble an dieser Position
     */
    public Marble getMarble(int posX, int posY) {
        //assert (posX >= 0 && posY >= 0);
        return this.field[posX][posY];
    }

    /**
     * Fuegt Spielstein anhand eines char-Wertes ins Spielfeld ein.
     *
     * @param name char-Wert der Steins
     * @param posX - x Koordinate
     * @param posY - y koordinate
     */
    private void insertMarble(char pl, int posX, int posY) throws IOException {
        switch (pl) {
            case 'C':
                insert(Player.COMPUTER, posX, posY);
                break;
            case 'P':
                insert(Player.HUMANPLAYER, posX, posY);
                break;
            case '-':
                insert(Player.EMPTY, posX, posY);
                break;
            default:
                throw new IOException();
        }
    }

    /**
     * Fuegt Spielstein anhand des Spielertyps in das Spielfeld ein.
     *
     * @param pl Spieler
     * @param posX - x Koordinate
     * @param posY - y koordinate
     * @return Spielerstein konnte platziert werden.
     */
    public boolean insert(Player pl, int posX, int posY) {
        if (this.field[posX][posY] != null
                && this.field[posX][posY].getPlayer() != Player.EMPTY) {
            return false;
        }
        switch (pl) {
            case COMPUTER:
                setCell(new Marble("C"), posX, posY);
                this.emptyPositionsNum--;
                break;
            case HUMANPLAYER:
                setCell(new Marble("P"), posX, posY);
                this.emptyPositionsNum--;
                break;
            case EMPTY:
                setCell(new Marble("-"), posX, posY);
                break;
            default:
                return false;
        }
        return true;
    }

    /**
     * Erstellt aus der aktuellen Spielfeldbelegung einen BigInteger-Wert.
     * Wandelt das Spielfeld zuvor in einen Binaerstring um.
     *
     * @return BigInteger-Wert
     */
    public BigInteger toBigInt() {
        String str = "";
        for (Marble[] field : this.field) {
            for (int j = 0; j < field.length; j++) {
                if (field[j].getPlayer() == Player.EMPTY) {
                    str = str.concat("00");
                } else {
                    str = str.concat(field[j].toBinary());
                }
            }
        }
        return new BigInteger(str, 2);
    }

    /**
     * Erstellt einen String mit Zeilenumbruechen von der aktuellen
     * Spielbelegung
     *
     * @return Stringwert der Spielfeldbelegung
     */
    @Override
    public String toString() {
        String str = "";
        for (int row = 0; row < linesize; row++) {
            for (int col = 0; col < linesize; col++) {
                str = str.concat(this.field[row][col].toString());
                // bis auf die letzte Zeile, Zeilenumbruch nach der letzten Spalte
                if (col == linesize - 1 && row != linesize - 1) {
                    str = str.concat("\n");
                }
            }
        }
        return str;
    }

    /**
     * Gibt die Anzahl der leeren Felder des Spielfeldes zurück
     *
     * @return Anzahl der leeren Felder
     */
    public int getNumberOfEmptyPlaces() {
        return this.emptyPositionsNum;
    }

    /**
     * Rotiert die Elemente eines Quadranten anhand der uebergebenen Rotation.
     *
     * @param rot Rotation die ausgefuehrt werden soll
     */
    public void performRotation(Rotation rot) {
        Quadrant qdr = rot.getQuadrant();
        boolean leftRotation = rot.getToLeft();

        int firstCol = (qdr == Quadrant.TOP_RIGHT || qdr == Quadrant.BOTTOM_RIGHT) ? linesize / 2 : 0;
        int firstRow = (qdr == Quadrant.BOTTOM_LEFT || qdr == Quadrant.BOTTOM_RIGHT) ? linesize / 2 : 0;
        int y = 2;
        int lastCol = y + firstCol;
        int lastRow = y + firstRow;

        // Linksrotation
        if (leftRotation) {
            for (int j = 0; j < y; j++) {

                Marble mb = getMarble(firstRow, firstCol + j);
                // 0 2 -> 0 0, 1 0 -> 0 1 0,1 2,2 0 0,1
                setCell(getMarble(firstRow + j, lastCol), firstRow, firstCol + j);
                // 2 2 -> 0 2 2 1 -> 1 0 2 2,1 0,1 2
                setCell(getMarble(lastRow, lastCol - j), firstRow + j, lastCol);
                // 2 0 -> 2 2 1 2 -> 2 1 2,1 0 2, 2,1
                setCell(getMarble(lastRow - j, firstCol), lastRow, lastCol - j);
                // 0 0 -> 2 0 0 1 -> 2,1 0
                setCell(mb, lastRow - j, firstCol);
            }
            // Rechtsrotation
        } else {
            for (int j = 0; j < y; j++) {
                Marble mb = getMarble(firstRow, firstCol + j);
                // 2 0 -> 0 0, 1 0 -> 0 1
                setCell(getMarble(lastRow - j, firstCol), firstRow, firstCol + j);
                // 2 2 -> 2 0, 2 1 -> 1 0
                setCell(getMarble(lastRow, lastCol - j), lastRow - j, firstCol);
                // 0 2 -> 2 2, 1 2 -> 2 1
                setCell(getMarble(firstRow + j, lastCol), lastRow, lastCol - j);
                // 0 0 -> 0 2, 0 1 -> 1 2
                setCell(mb, firstRow + j, lastCol);
            }
        }

    }

    /**
     * Prueft ob 2 Spielermarblen durch eine Computer-Marble blockiert werden.
     *
     * @param fst erste Marble
     * @param snd zweite Marble
     * @param trd dritte Marble
     * @return eine Computer-Spielstein blockiert zwei Spieler-Spielsteine
     */
    private boolean blocked(Marble fst, Marble snd, Marble trd) {
        if (fst.getPlayer() == Player.HUMANPLAYER && (snd.getPlayer() == Player.HUMANPLAYER || trd.getPlayer() == Player.HUMANPLAYER)) {
            if (snd.getPlayer() != Player.COMPUTER && trd.getPlayer() != Player.COMPUTER) {
                return false;
            }
        }
        if (snd.getPlayer() == Player.HUMANPLAYER && trd.getPlayer() == Player.HUMANPLAYER) {
            if (fst.getPlayer() != Player.COMPUTER) {
                return false;
            }
        }

        return true;
    }

    /**
     * Bewertet 5 Spielsteine. Die Spielsteine muessen nebeneinander liegen um
     * eine potentielle Gewinnreige darzustellen.
     *
     * @param fst erste Marble
     * @param snd zweite Marble
     * @param thd dritte Marble
     * @param fth vierte Marble
     * @param fith fuenste Marble
     * @return Bewertung der 5 Steine
     */
    private int evalFive(Marble fst, Marble snd, Marble thd, Marble fth, Marble fith) {
        int player = 0;
        int computer = 0;
        int additional = 0;
        Marble[] arr = new Marble[]{fst, snd, thd, fth, fith};
        for (int i = 0; i < 5; i++) {
            switch (arr[i].getPlayer()) {
                case HUMANPLAYER:
                    player++;
                    break;
                case COMPUTER:
                    computer++;
                    break;
                case EMPTY:
                    additional++;
                    break;
                default:
                    break;
            }
        }

        // keine Spielerstein
        if (computer > 0 && player == 0) {
            return computer * 10 + additional;
        }
        // kein Computerstein
        if (player > 0 && computer == 0) {
            return player * -10 - additional;
        }

        return 0;
    }

    /**
     * Bewertet 6 Spielsteine. Gibt einen positiven Wert zurück wenn fuer den
     * Computer eine Moeglichkeit besteht eine Reihe von mindestens 5 Steinen
     * nebeneinandern zu bilden. Wenn der Spieler diese Bedingung erfuellt,
     * liefert die Methode einen negativen Wert zurueck. Liefert 45 falls ein
     * Computerstein 2 Spielersteine blockiert. Gibt den Wert 0 zurueck falls
     * alle Steine EMPTY sind oder fuer beide Spieler keine Reihe von 5 Steinen
     * mehr entstehen kann.
     *
     * @param fst erste Marble
     * @param snd zweite Marble
     * @param thd dritte Marble
     * @param fth vierte Marble
     * @param fith fuenfte Marble
     * @param sixt sechste Marble
     * @param defensive Defensive Strategie des Computers
     * @return Bewertung der 6 Spielsteine
     */
    private int evaluateSix(Marble fst, Marble snd, Marble thd, Marble fth, Marble fith, Marble sixt, boolean defensive) {
        int player = 0;
        boolean playerBlocks = false;
        int computer = 0;
        boolean computerBlocks = false;

        int freePositions = 0;
        Marble[] arr = new Marble[]{fst, snd, thd, fth, fith, sixt};
        for (int i = 0; i < 6; i++) {
            switch (arr[i].getPlayer()) {
                case HUMANPLAYER:
                    if (i != 0 && i != 5) {
                        playerBlocks = true;
                    }
                    player++;
                    break;
                case COMPUTER:
                    if (i != 0 && i != 5) {
                        computerBlocks = true;
                    }
                    computer++;
                    break;
                case EMPTY:
                    freePositions++;
                    break;
                default:
                    break;
            }
        }
        // Mindestens zwei Computersteine, kein Spielerstein auf inneren Feldern
        if (computer > 2 && computer + freePositions >= 5 && !playerBlocks) {
            return computer * 10 + freePositions;
        }
        // Mindestens drei Spielersteine, kein Spielerstein auf inneren Feldern
        if (player > 2 && player + freePositions >= 5 && !computerBlocks) {
            return player * -10 - freePositions;
        }
        if (defensive && twoOfThreeArePlayer(fst, snd, thd)) {
            if (blocked(fst, snd, thd)) {
                return pointsTarget - 5;
            }

        }
        if (defensive && twoOfThreeArePlayer(fth, fith, sixt)) {
            if (blocked(fth, fith, sixt)) {
                return pointsTarget - 5;
            }

        }

        return 0;
    }

    /**
     * Prueft ob mindestens 2 von 3 Spielsteinen, Spielsteine des Spielers sind.
     *
     * @param fst erste Marble
     * @param snd zweite Marble
     * @param thd dritte Marble
     * @return mindestens 2 Spielsteine sind Spielerspielsteine
     */
    private boolean twoOfThreeArePlayer(Marble fst, Marble snd, Marble thd) {
        if (fst.getPlayer() == Player.HUMANPLAYER) {
            return snd.getPlayer() == Player.HUMANPLAYER || thd.getPlayer() == Player.HUMANPLAYER;
        } else {
            return snd.getPlayer() == Player.HUMANPLAYER && thd.getPlayer() == Player.HUMANPLAYER;
        }
    }

    /**
     * Wertet das Spielfeld horizontal, vertikal und diagonal aus. Liefert einen
     * positiven Wert bei einem Vorteil fuer den Computer. Liefert einen
     * negativen Wert bei einem Vorteil fuer den Spieler.
     *
     * @param defensive Defensive Strategie des Computers
     * @return Bewertung des Spielfeldes
     */
    public int evaluate(boolean defensive) {
        int points = 0;
        int linepoints = 0;

        // Feld horizontal auswerten
        for (int row = 0; row < linesize; row++) {
            linepoints = evaluateSix(this.field[row][0], this.field[row][1], this.field[row][2], this.field[row][3], this.field[row][4], this.field[row][5], defensive);
            if (Math.abs(linepoints) >= pointsTarget) {
                return (linepoints >= 0) ? Integer.MAX_VALUE : Integer.MIN_VALUE;
            } else {
                points += linepoints;
            }

        }
        // Feld vertikal auswerten
        for (int col = 0; col < linesize; col++) {

            linepoints = evaluateSix(this.field[0][col], this.field[1][col], this.field[2][col], this.field[3][col], this.field[4][col], this.field[5][col], defensive);
            if (Math.abs(linepoints) >= pointsTarget) {
                return (linepoints >= 0) ? Integer.MAX_VALUE : Integer.MIN_VALUE;
            } else {
                points += linepoints;
            }

        }

        // Hauptdiagonalen Quadrant TOP_LEFT
        if (!(this.field[1][1].getPlayer() == Player.COMPUTER && this.field[4][4].getPlayer() == Player.HUMANPLAYER)
                && !(this.field[1][1].getPlayer() == Player.HUMANPLAYER && this.field[4][4].getPlayer() == Player.COMPUTER)) {

            linepoints = evaluateSix(this.field[0][0], this.field[1][1], this.field[2][2], this.field[3][3], this.field[4][4], this.field[5][5], defensive);
            if (Math.abs(linepoints) >= pointsTarget) {
                return (linepoints >= 0) ? Integer.MAX_VALUE : Integer.MIN_VALUE;
            } else {
                points += linepoints;
            }

        }
        // kurze Diagonale
        linepoints = evalFive(this.field[0][1], this.field[1][2], this.field[2][3], this.field[3][4], this.field[4][5]);
        if (Math.abs(linepoints) >= pointsTarget) {
            return (linepoints >= 0) ? Integer.MAX_VALUE : Integer.MIN_VALUE;
        } else {
            points += linepoints;
        }

        // kurze Diagonale
        linepoints = evalFive(this.field[1][0], this.field[2][1], this.field[3][2], this.field[4][3], this.field[5][4]);
        if (Math.abs(linepoints) >= pointsTarget) {
            return (linepoints >= 0) ? Integer.MAX_VALUE : Integer.MIN_VALUE;
        } else {
            points += linepoints;
        }

        // Hauptdiagonalen Quadrant TOP_Right
        if (!(this.field[1][4].getPlayer() == Player.COMPUTER && this.field[4][1].getPlayer() == Player.HUMANPLAYER)
                && !(this.field[1][4].getPlayer() == Player.HUMANPLAYER && this.field[4][1].getPlayer() == Player.COMPUTER)) {
            linepoints = evaluateSix(this.field[0][5], this.field[1][4], this.field[2][3], this.field[3][2], this.field[4][1], this.field[5][0], defensive);
            if (Math.abs(linepoints) >= pointsTarget) {
                return (linepoints >= 0) ? Integer.MAX_VALUE : Integer.MIN_VALUE;
            } else {
                points += linepoints;
            }

        }

        // kurze Diagonale
        linepoints = evalFive(this.field[0][4], this.field[1][3], this.field[2][2], this.field[3][1], this.field[4][0]);
        if (Math.abs(linepoints) >= pointsTarget) {
            return (linepoints >= 0) ? Integer.MAX_VALUE : Integer.MIN_VALUE;
        } else {
            points += linepoints;
        }

        // kurze Diagonale
        linepoints = evalFive(this.field[1][5], this.field[2][4], this.field[3][3], this.field[4][2], this.field[5][1]);
        if (Math.abs(linepoints) >= pointsTarget) {
            return (linepoints >= 0) ? Integer.MAX_VALUE : Integer.MIN_VALUE;
        } else {
            points += linepoints;
        }

        return points;
    }

    /**
     * Gibt die Groeße einer Zeile/Spalte an.
     *
     * @return Groeße
     */
    public int lineSize() {
        return this.linesize;
    }

    /**
     * Gibt die Groeße des Spielfeldes an.
     *
     * @return Groeße des Spielfeldes
     */
    public int size() {
        return this.linesize * this.linesize;
    }

}
