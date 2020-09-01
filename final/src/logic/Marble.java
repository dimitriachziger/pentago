package logic;

/**
 * Repraesentiert einen Spielstein. Ist entweder von Typ Computer, Spieler oder
 * Empty
 *
 * @author Dimitri Solodownik, inf102736
 */
public class Marble {

    /* Spieler */
    private final Player player;

    /**
     * Erstellt einen Spielstein
     *
     * @param n Name des Typs
     */
    public Marble(String n) {
        switch (n) {
            case "P":
                this.player = Player.HUMANPLAYER;
                break;
            case "C":
                this.player = Player.COMPUTER;
                break;
            case "-":
                this.player = Player.EMPTY;
                break;
            default:
                throw new AssertionError("Incorrect Marble");
        }
    }

    /**
     * Binaerwert fuer diesen Spielstein als String. empty - 00, com - 01, ply -
     * 10
     *
     * @return Binaerwert der Marble
     */
    public String toBinary() {
        String result = Integer.toBinaryString(this.player.ordinal());
        assert (result.equals("1") || result.equals("10"));
        return result.equals("10") ? result : "01";

    }

    @Override
    public String toString() {
        switch (this.player) {
            case COMPUTER:
                return "C";
            case HUMANPLAYER:
                return "P";
            case EMPTY:
                return "-";
        }
        return null;
    }

    /**
     * Liefert den zugehoerigen Spieler 
     * @return Spieler
     */
    public Player getPlayer() {
        return this.player;
    }

}
