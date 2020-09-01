package logic;

/**
 * Interface für die GUI-Oberfläche(JavaFX) und für die FakeGUI
 *
 * @author Dimitri Solodownik, inf102736
 */
public interface GUIConnector {

    /**
     * Setzt das Bild der Spielbelegung anhand der Koordinaten
     *
     * @param pl Spieler
     * @param posX x Koordinate
     * @param posY y Koordinate
     */
    void setImage(Player pl, int posX, int posY);

    /**
     * Fuhrt die Animation fuer die Drehung eines Quadranten um 90Grad durch.
     * Ruft im Anschluss an die Animation die Logik auf, um ggf ein
     * Computerspielzug durchzufuehren
     *
     * @param qdr Quadrant der gedreht wird
     * @param leftTwist Drehung gegen den Uhrzeigersinn
     * @param duration Dauer der Animation
     * @param logic aufrufende Logikinstanz
     */
    void rotateQuadrant(Quadrant qdr, boolean leftTwist, long duration, GameLogic logic);

    /**
     * Fuegt dem Protokollfeld den uebergebenen Eintraege fuer die Spielzuege
     * hinzu und hanegt ein Zeilenumbruch an
     *
     * @param str Text der dem Protkoll hinzugefuegt werden soll
     */
    void writeLog(String str);

    /**
     * Benachrichtig ueber den Gewinn eines Spielers
     *
     * @param p Gewinner
     * @param logic Logik die wieder aufgerufen werden soll
     */
    void alertWin(Player p, GameLogic logic);

    /**
     * Benachrichtigt ueber ein Unentschieden
     *
     * @param p0 Gewinner 1
     * @param p1 Gewinner 2
     * @param logic Logik die wieder aufgerufen werden soll
     */
    void alertDraw(Player p0, Player p1, GameLogic logic);

    /**
     * Entfernt Eintraege der Spielzuege aus dem Protokoll
     *
     * @param allEntrys alle Eintraege, false - die letzten 4
     */
    void clearLog(boolean allEntrys);
    
    /**
     * Benachrichtig über eine Fehlermedlung
     * @param err Fehlermeldung
     */
    void alertError(String err);
    
    /**
     * Markiert den Spielstein, der den uebergebenen Koordinaten zugeordnet ist
     * @param x x-Koordinate
     * @param y y-koordinate
     */
    void markWin(int x, int y);
    
    /**
     * Stellt den Initialzustand der Bilder wieder her, indem es die
     * Hintergrundbilder plaziert.
     */
    void resetImages();
    
    /**
     * Deaktivert das MenuItem Speichern sowie die Buttons vor und zurueck.
     * @param disable Deaktivieren
     */
    void disableItmSaveAndHistoryButtons(boolean disable);
    
     /**
     * Deaktivert die Drehbuttons.
     * @param disable Deaktivieren
     */
    void disableRotButtons(boolean disable);
    

}
