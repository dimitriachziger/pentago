package logic;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Realisiert die Logik des Spiels und gewährleistet den korrekten Ablauf.
 *
 * @author Dimitri Solodownik, inf102736
 */
public class GameLogic {

    /* Groesse eines Quadranten in einer Richtung */
    private static final int QUADRANTSIZE = 3;
    /* GUI Instanz */
    private final GUIConnector gui;
    /* Spielfeldinstanz */
    private Board gamefield;
    /* Angabe ob Rotation ausstehend */
    private boolean rotationRemain = false;
    /* Wurzelknoten des Berechnungsbaums */
    private Tree tree;
    /* maximale Baumtiefe */
    private int maxTreeDepth = 2;
    /* aktueller Spieler */
    private Player actualPlayer = Player.HUMANPLAYER;
    /* abgebildeter Spielzug */
    private LogEntry logEntry;
    /* Protokollliste der Spielzuege */
    private List<LogEntry> recordList;
    /* Index der Protkollliste */
    private int lastRecordPos = 0;
    /* Nach einem Spielerzug, wurde ein Computerzug durchgefuehrt*/
    private boolean bothPlayerMoved = false;
    /* Dauer fuer die Animation eines Quadranten */
    private final long animationDuration = 2000;
    /* Strategie der AI */
    private boolean defensiveAI = false;
    /* Name der AI */
    private static final String AI_NAME = "Blau";
    /* Name des Spielers */
    private static final String PL_NAME = "Orange";

    /**
     * erzeugt eine Logik und setzt das Spielfeld
     *
     * @param gui zugehoerige GUI
     * @param field Spielfeld
     */
    public GameLogic(GUIConnector gui, Board field) {
        this.gui = gui;
        if (field == null) {
            this.gamefield = new Board();
        } else {
            this.gamefield = field;
        }
        this.recordList = new LinkedList<>();
        this.logEntry = new LogEntry();
        // leeres Feld hinzufuegen
        this.recordList.add(new LogEntry(this.gamefield.toString()));
        this.gui.disableRotButtons(true);
        this.gui.disableItmSaveAndHistoryButtons(true);
    }

    /**
     * Fuehrt einen Spielzug des Spielers durch indem es die Einfuegeposition
     * validiert und dann den Spielerstein auf dem Spielfeld platziert. Wenn
     * Zuege zuruckgenommen worden sind, werden nach diesem Zug die bishierhin
     * zurueckgenommen geloescht
     *
     * @param quadRow Quadrantzeile
     * @param quadCol Quadrantspalte
     * @param row x-Koordinate
     * @param col y-Koordinate
     */
    public void handlePlayerMove(int quadRow, int quadCol, int row, int col) {
        if (!this.rotationRemain) {
            this.rotationRemain = true;
            bothPlayerMoved = false;
            row += quadRow * QUADRANTSIZE;
            col += quadCol * QUADRANTSIZE;
            if (this.gamefield.insert(Player.HUMANPLAYER, row, col)) {
                this.gui.setImage(Player.HUMANPLAYER, row, col);
                this.gui.disableItmSaveAndHistoryButtons(true);
                this.gui.disableRotButtons(false);
                this.gui.writeLog(PL_NAME + " spielt " + row + " " + col);
                if (!checkWin()) {
                    logEntry.setMove(PL_NAME, row, col);
                    // bis hierhin zurueckgenomme Zuege entfernen
                    while (lastRecordPos + 1 < recordList.size()) {
                        recordList.remove(recordList.size() - 1);
                    }
                }
            }
        }
    }

    /**
     * Startet die Auswertung des Berechnungsbaum und fuehrt mit dem Ergebnis
     * einen Spielzug der Ai durch.
     *
     */
    public void handleAiMove() {
        this.tree = new Tree();
        tree.startAI(gamefield.toBigInt(), maxTreeDepth, defensiveAI);
        int insertPos = this.tree.getInsertPos();
        int x = getXCoordFromBitPos(insertPos);
        int y = getYCoordFromBitPos(insertPos);
        // die errechneten Spielzug durchfuehren
        this.gamefield.insert(Player.COMPUTER, x, y);
        this.gui.setImage(Player.COMPUTER, x, y);
        this.gui.writeLog(AI_NAME + " spielt " + x + " " + y);
        this.rotationRemain = true;
        logEntry.setMove(AI_NAME, x, y);

        // pruefen ob Computer vor der Drehung gewonnen hat
        if (!checkWin()) {
            handleRotation(this.tree.getRotation().getQuadrant(), this.tree.getRotation().getToLeft());
        }
        // Speicherplatz freigeben
        this.tree = null;

    }

    /**
     * Liefert anhand der BitPosition die x-Koordinate
     *
     * @param bitPos Bit-Position
     * @return x-Koordinate
     */
    public static int getXCoordFromBitPos(int bitPos) {
        return (71 - bitPos) / 12;
    }

    /**
     * Liefert anhand der BitPosition die y-Koordinate
     *
     * @param bitPos Bit-Position
     * @return y-Koordinate
     */
    public static int getYCoordFromBitPos(int bitPos) {
        return (71 - bitPos) / 2 % 6;
    }

    /**
     * Durchlauft die Bits aller BigInteger-Werte der uebergebenen Menge und ruf
     * bei gesetzten Bits die GUI auf, die entsprechende Zelle zu markieren.
     *
     * @param winSet Set der Gewinn-Positionen
     * @param winner Gewinner
     */
    private void markWinPositions(Set<BigInteger> winSet, Player winner) {
        for (BigInteger bigInteger : winSet) {
            for (int i = this.gamefield.size() * 2 - 1; i >= 0; i = i - 2) {
                if (winner == Player.HUMANPLAYER && bigInteger.testBit(i)) {
                    this.gui.markWin(getXCoordFromBitPos(i), getYCoordFromBitPos(i));
                } else if (bigInteger.testBit(i - 1)) {
                    this.gui.markWin(getXCoordFromBitPos(i), getYCoordFromBitPos(i));
                }
            }
        }
    }

    /**
     * Prueft ob ein Gewinn eines Spielers im aktuellen Spielfeld vorliegt.
     *
     * @return einer der Spieler hat gewonnen.
     */
    private boolean checkWin() {
        if (this.gamefield.getNumberOfEmptyPlaces() > 27) {
            return false;
        }

        BigInteger actualFieldAsBigInt = this.gamefield.toBigInt();
        Set<BigInteger> plWin = WinValidator.getSetOfWinBits(actualFieldAsBigInt, Player.HUMANPLAYER);
        Set<BigInteger> aiWin = WinValidator.getSetOfWinBits(actualFieldAsBigInt, Player.COMPUTER);
        if (plWin != null) {
            if (aiWin != null) {
                markWinPositions(aiWin, Player.COMPUTER);
                markWinPositions(plWin, Player.HUMANPLAYER);
                this.gui.alertDraw(Player.COMPUTER, Player.HUMANPLAYER, this);
                return true;
            } else {
                markWinPositions(plWin, Player.HUMANPLAYER);
                this.gui.alertWin(Player.HUMANPLAYER, this);
                return true;
            }

        } else if (aiWin != null) {
            markWinPositions(aiWin, Player.COMPUTER);
            this.gui.alertWin(Player.COMPUTER, this);
            return true;
        }
        // Unentschieden,da kein freies Feld mehr
        if (this.gamefield.getNumberOfEmptyPlaces() == 0) {
            this.gui.alertDraw(Player.COMPUTER, Player.HUMANPLAYER, this);
            return true;
        }
        return false;
    }

    /**
     * Fuehrt eine Rotation des uebergebenen Quadranten durch.
     * @param qdr Quadrant
     * @param leftRotation Rotation nach links
     */
    public void handleRotation(Quadrant qdr, boolean leftRotation) {
        if (this.rotationRemain) {
            this.gui.disableRotButtons(true);
            gamefield.performRotation(new Rotation(qdr, leftRotation));

            this.gui.rotateQuadrant(qdr, leftRotation, animationDuration, this);
            this.gui.writeLog((actualPlayer == Player.COMPUTER ? AI_NAME : PL_NAME) + " dreht " + qdr.name() + (leftRotation ? " links" : " rechts") + "\n");

            logEntry.setFieldAndRotation(gamefield.toString(), new Rotation(qdr, leftRotation));
            recordList.add(logEntry.copy());
            lastRecordPos++;
        }
    }

    /**
     * Wird von der GUI aufgerufen und setzt den Ablauf fort.
     */
    public void proceedAfterGuiRotation() {
        this.rotationRemain = false;
        if (!checkWin()) {
            if (actualPlayer == Player.HUMANPLAYER) {
                actualPlayer = Player.COMPUTER;
                handleAiMove();
            } else {
                bothPlayerMoved = true;
                //System.gc();
                this.gui.disableItmSaveAndHistoryButtons(false);
                actualPlayer = Player.HUMANPLAYER;
            }
        }
    }

    /**
     * Aktualisiert alle Zellen des Spielfeldes in der GUI.
     */
    private void updateGui() {
        for (int row = 0; row < this.gamefield.lineSize(); row++) {
            for (int col = 0; col < this.gamefield.lineSize(); col++) {
                gui.setImage(this.gamefield.getMarble(row, col).getPlayer(), row, col);
            }
        }
    }

    /**
     * Validiert die Ruecknahme eines Zuges und fuehrt diese ggf Durch.
     */
    public void undoMove() {
        if (bothPlayerMoved && lastRecordPos > 0) {
            lastRecordPos -= 2;
            try {
                this.gamefield = new Board(recordList.get(lastRecordPos).getField());
            } catch (IOException ex) {
                this.gui.alertError("unerwarteter Fehler aufgetreten");
            }
            updateGui();
            this.gui.clearLog(false);
        }
    }

    /**
     * Validiert die Wiederholung eines Zuges und fuehrt diese ggf Durch.
     */
    public void redoMove() {
        if (bothPlayerMoved && lastRecordPos + 1 < recordList.size()) {
            LogEntry foreLast = recordList.get(lastRecordPos + 1);
            LogEntry last = recordList.get(lastRecordPos + 2);

            try {
                this.gamefield = new Board(last.getField());
            } catch (IOException ex) {
                this.gui.alertError("unerwarteter Fehler aufgetreten");
            }
            this.gui.writeLog(foreLast.getPlayer() + " spielt " + foreLast.getX() + " " + foreLast.getY());
            this.gui.writeLog(foreLast.getPlayer() + " dreht " + foreLast.getRotation().getQuadrant() + (foreLast.getRotation().getToLeft() ? " links" : " rechts") + "\n");

            this.gui.writeLog(last.getPlayer() + " spielt " + last.getX() + " " + last.getY());
            this.gui.writeLog(last.getPlayer() + " dreht " + last.getRotation().getQuadrant() + (last.getRotation().getToLeft() ? " links" : " rechts") + "\n");

            updateGui();
            lastRecordPos += 2;
        }
    }

    /**
     * Liefer das Spielfeld. Nur fuer Testzwecke.
     *
     * @return Spielfeld
     */
    public Board getField() {
        return this.gamefield;
    }

    /**
     * Laedt einen Spielstand aus einer Datei und stellt diesen auf dem
     * Spielfeld dar. Benachrichtigt den Benutzer bei falschen Format.
     *
     * @param f Datei aus der der Spielstand gelesen werden soll.
     */
    public void loadFile(File f) {
        FileUtil util = new FileUtil();
        String str = null;
        try {
            str = util.readGamefield(f);
        } catch (IOException ex) {
            this.gui.alertError("Die Datei konnte nicht geladen werden.");
        }
        try {
            this.gamefield = new Board(str);
            updateGui();
            this.gui.clearLog(true);
            this.lastRecordPos = 0;
            this.recordList = new LinkedList<>();
            recordList.add(new LogEntry(this.gamefield.toString()));
            this.rotationRemain = false;
        } catch (ArrayIndexOutOfBoundsException | IOException e) {
            this.gui.alertError("Die ausgewählte Datei ist fehlerhaft.");
        }

    }

    /**
     * Speichert das Spiel. Schreibt die aktuelle Spielfeldbelgung als String in
     * eine Datei.
     *
     * @param file Datei in die geschrieben wird.
     */
    public void saveGame(File file) {
        FileUtil util = new FileUtil();
        try {
            util.writeGamefield(file, gamefield.toString());
        } catch (IOException ex) {
            this.gui.alertError("Die Datei konnte nicht gespeichert werden.");
        }
    }

    /**
     * Setzt die Strategie des Computers. Zurueckgenomme Zuege koenen nicht mehr
     * wiederholt werden.
     *
     * @param defensive Defensive Strategie
     */
    public void setStrategy(boolean defensive) {
        this.defensiveAI = defensive;
        while (lastRecordPos + 1 < recordList.size()) {
            recordList.remove(recordList.size() - 1);
        }
    }

    /**
     * Setzt das Spiel auf den Startzustand zurueck.
     */
    public void reset() {
        this.gamefield = new Board();
        this.actualPlayer = Player.HUMANPLAYER;
        this.recordList.clear();
        this.recordList.add(new LogEntry(this.gamefield.toString()));
        this.lastRecordPos = 0;
        this.gui.clearLog(true);
        this.gui.resetImages();
        this.rotationRemain = false;
        this.tree = null;
    }

    /**
     * Setzt die Schwierigkeitsstufe des Spiels indem die maximale des Baums
     * veraendert wird.
     *
     * @param diffLevel Schwierigkeitsstufe
     */
    public void setDifficulty(int diffLevel) {
        switch (diffLevel) {
            case 0:
                this.maxTreeDepth = 1;
                break;
            case 1:
                this.maxTreeDepth = 2;
                break;
            case 2:
                this.maxTreeDepth = 3;
                break;
        }
    }

}
