package logic;

/**
 * Repraesentiert einen Protokolleintrag. Besitzt die Spielfeldbelegung sowie
 * die Informationen ueber den durchgefuehrten Zug eines Spielers
 *
 * @author Dimitri Solodownik, inf102736
 */
public class LogEntry {

    /* Spielfeld */
    private String field;
    /* Spieler */
    private String player;
    /* x-Koordinate */
    private int xCoord;
    /* y-Koordinate */
    private int yCoord;
    /* Rotation */
    private Rotation rot;

    /**
     * Default-Konstruktor
     */
    public LogEntry(){
        
    }
    
    /**
     * Erstellt ein LogEntry nur mit der Angabe der Spielfeldbelegung.
     * @param fieldAsString Spielfeld
     */
    public LogEntry(String fieldAsString) {
        this.field = fieldAsString;
    }

    /**
     * Erstellt einen vollstaendigen LogEntry.
     * @param fieldAsString Spielfeld
     * @param plName Spieler
     * @param x x-Koordinate
     * @param y yKorrdinate
     * @param r Rotation
     */
    public LogEntry(String fieldAsString, String plName, int x, int y, Rotation r) {
        this.field = fieldAsString;
        this.player = plName;
        this.xCoord = x;
        this.yCoord = y;
        this.rot = r;
    }

    /**
     * Liefert die Spielfeldbelegung in String-Darstellung
     * @return Spielfeld
     */
    public String getField() {
        return this.field;
    }

    /**
     * Liefert den Spieler
     * @return Spieler
     */
    public String getPlayer() {
        return this.player;
    }

    /**
     * Liefert x-Koordinate
     * @return x-Koordinate
     */
    public int getX() {
        return this.xCoord;
    }

    /**
     * Liefert y-Koordinate
     * @return y-Koordinate
     */
    public int getY() {
        return this.yCoord;
    }

    /**
     * Liefert Rotation
     * @return Rotation
     */
    public Rotation getRotation() {
        return this.rot;
    }

    /**
     * setzt den Spieler und die Koordinaten des Spiezugs.
     * @param playerName Spieler
     * @param x x-Koordinate
     * @param y y-Koordinate
     */
    public void setMove(String playerName, int x, int y) {
        this.player = playerName;
        this.xCoord = x;
        this.yCoord = y;
    }

    /**
     * setzt die Spielfeldbelgung und die Rotation des Spielzuges.
     * @param field Spielfed
     * @param r Rotation
     */
    public void setFieldAndRotation(String field, Rotation r) {
        this.field = field;
        this.rot = r;
    }


    /**
     * Liefert eine Kopie dieser Instanz
     * @return
     */
    public LogEntry copy() {
        return new LogEntry(field, player, xCoord, yCoord, rot);
    }

}
