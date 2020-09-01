package logic;

/**
 * Repraesentiert die Rotation eines Quadranten.
 * 
 * @author Dimitri Solodownik, inf102736
 */
public class Rotation {

    /* Quadrant */
    private Quadrant qdr;
    /* Linksdrehung */
    private boolean toLeft;

    /**
     * Eine Rotation die aus ihrem Quadranten und ihrer Richtung besteht.
     *
     * @param qdr Quadrant dieser Rotation
     * @param toLeft Rotation nach links
     */
    public Rotation(Quadrant qdr, boolean toLeft) {
        this.qdr = qdr;
        this.toLeft = toLeft;
    }

    /**
     * Eine Rotation die aus ihrem Quadranten und ihrer Richtung besteht. Als
     * Start wird der obenlinks gelegende Quadrant des Spielfeldes und die
     * Richtung nach links gesetzt.
     */
    public Rotation() {
        this.qdr = Quadrant.TOP_LEFT;
        this.toLeft = true;
    }

    /**
     * Veraendert die Attribute der Rotation. Bei einer Linksdrehung bleibt der
     * Quadrant und die Richtung wechselt zu rechts. Bei einer Rechtsdrehung
     * wird der nachste Quadrant im Uhrzeigersinn ausgewaehlt und die Richtung
     * wechselt zu links;
     */
    public void next() {
        Quadrant[] vals = Quadrant.values();
        if (!this.toLeft) {
            Quadrant q = vals[(this.qdr.ordinal() + 1) % vals.length];
            this.qdr = q;
        }
        this.toLeft = !this.toLeft;
    }

    /**
     * Erstellt eine Kopie dieser Instanz
     * @return Kopie
     */
    public Rotation copy() {
        return new Rotation(this.qdr, this.toLeft);
    }

    /**
     * Liefert den aktuellen Quadranten.
     * @return Quadrant
     */
    public Quadrant getQuadrant() {
        return this.qdr;
    }

    /**
     * Gibt die Richtung der Rotation an.
     * @return true - nach links, false - nach rechts
     */
    public boolean getToLeft() {
        return this.toLeft;
    }

}
