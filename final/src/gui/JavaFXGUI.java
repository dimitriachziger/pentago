package gui;

import javafx.animation.RotateTransition;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;
import logic.GUIConnector;
import logic.GameLogic;
import logic.Player;
import logic.Quadrant;

/**
 * Platziert Bilder auf dem Spielfeld und manipuliert diese. Stellt der Logik
 * Methoden zu Verfügung, um die Oberfläche(Ausgabe) zu verändern. Setzt Bilder
 * und animiert den Spielablauf.
 *
 * @author Dimitri Solodownik, inf102736
 */
public class JavaFXGUI implements GUIConnector {

    /* Imageviews aller Spielfeldpositionen */
    private ImageView[][][] imageviews;
    /* Pfad der Bilder */
    private final static String PATH = "/gui/images/";
    /* Dateiendung der Bilder */
    private final static String EXT = ".jpg";
    /* Hintergrundbild */
    private Image backgroundImg = new Image(PATH + "background" + EXT);
    private Image marble0Img = new Image(PATH + "marble_0" + EXT);
    private Image marble1Img = new Image(PATH + "marble_1" + EXT);
    // Gridpanes der vier Quadranten
    private Node[] gridQuadrants;
    // Protokolltextfeld
    private TextArea log;
    // MenuItem Speichern
    private MenuItem itmSave;
    // Drehbuttons
    private Button[] rotButtons;
    // Button Spielzug zureucknehmen
    private Button btnUndo;
    // Button Spielzug wiederholen
    private Button btnRedo;

    /**
     * Erstellt eine GUI, die Bilder fuer die Spielfeldpositionen visualiert und
     * konfiguriert
     *
     * @param ivs 3D Imageview-Array, 1dim - Quadranten, 2dim - Reihe, 3dim-
     * Spalte
     * @param grids 4 Quadranten des Spielfeldes, in Lesereihenfolge
     * @param log Protokolltextfeld
     * @param itmSave MenuItem Spielstand speichern
     * @param b Rotationsbuttons
     * @param undo Button Rueckgaengig
     * @param redo Button Wiederholen
     */
    public JavaFXGUI(ImageView[][][] ivs, Node[] grids, TextArea log, MenuItem itmSave, Button[] b, Button undo, Button redo) {
        this.imageviews = ivs;
        this.gridQuadrants = grids;
        this.log = log;
        setBackgroundImages();
        this.itmSave = itmSave;
        this.rotButtons = b;
        this.btnUndo = undo;
        this.btnRedo = redo;
    }

    @Override
    public void writeLog(String str) {
        this.log.appendText(str + "\n");
    }

    /**
     * Stellt den Initialzustand des Spieles her, indem für jedes Imageview das
     * Hintergrundbild gesetzt wird
     */
    private void setBackgroundImages() {
        for (ImageView[][] imageview : imageviews) {
            for (int j = 0; j < 3; j++) {
                for (int k = 0; k < 3; k++) {
                    imageview[j][k].setImage(this.backgroundImg);
                    imageview[j][k].setEffect(null);
                }
            }
        }

    }

    /**
     * Liefert das erste Node das anhand der Zeile und Spalte gefunden wird
     *
     * @param g - Gridpane das durchsucht wird
     * @param col gesuchte Spalte
     * @param row gesuchte Zeile
     * @return Node, das der Zeile und Spalte zugeordnet ist
     */
    public static Node getNodeFromGridPane(GridPane g, int row, int col) {
        Node foundNode = null;
        for (Node node : g.getChildren()) {
            if (GridPane.getColumnIndex(node) == col && GridPane.getRowIndex(node) == row) {
                foundNode = node;
            }
        }
        return foundNode;
    }

    /**
     * Setzt das Bild fuer das Imageview der Zeile und Spalte
     *
     * @param pl - Spieler der gesetzt wird
     * @param row Koordinate x
     * @param col Koordinate y
     */
    @Override
    public void setImage(Player pl, int row, int col) {
        //Image img = null;
        ImageView iv = getImageView(row, col);
        switch (pl) {
            case COMPUTER:
                iv.setImage(marble0Img);
                break;
            case HUMANPLAYER:
                iv.setImage(marble1Img);
                break;
            case EMPTY:
                iv.setImage(backgroundImg);
                break;
        }

        //iv.setImage(null);
        //System.gc();
        //
    }

    private ImageView getImageView(int xCoord, int yCoord) {
        // Koordinaten auf einen einzelnen Quadranten anpassen um Arrayindex zu nutzen
        int qdr = 0;
        // untere beiden Quadranten
        if (xCoord > 2) {
            // unten rechts
            if (yCoord > 2) {
                qdr = 3;
                xCoord = xCoord - 3;
                yCoord = yCoord - 3;
                // unten links
            } else {
                qdr = 2;
                xCoord = xCoord - 3;
            }
            // oben rechts
        } else if (yCoord > 2) {
            qdr = 1;
            yCoord = yCoord - 3;
        }
        return (ImageView) getNodeFromGridPane((GridPane) gridQuadrants[qdr], xCoord, yCoord);
    }

    @Override
    public void resetImages() {
        setBackgroundImages();
    }

    /**
     * Führt die Animation einer Drehung eines Quadranten des Spielfeldes um 90
     * Grad bzw -90 Grad.
     *
     * @param qdr Quadrant der gedreht wird
     * @param leftTwist Drehung nach links
     * @param duration Dauer der Animation
     * @param logic aufrufende Logikinstanz
     */
    @Override
    public void rotateQuadrant(Quadrant qdr, boolean leftTwist, long duration, GameLogic logic) {
        GridPane quadrant = (GridPane) gridQuadrants[qdr.ordinal()];

        RotateTransition rt = new RotateTransition(Duration.millis(duration), quadrant);

        if (leftTwist) {
            rt.setByAngle(-90);
        } else {
            rt.setByAngle(90);
        }
        rt.setCycleCount(1);

        // im Anschluss an die Animation, die Drehung zuruecksetzen und 
        // Imageviewarray anpassen
        rt.setOnFinished((ActionEvent actionEvent) -> {
            quadrant.setRotate(0);
            rotateImageViews(qdr.ordinal(), leftTwist);

            logic.proceedAfterGuiRotation();
        });
        // Animation starten
        rt.play();

    }

    /**
     * Rotiert das Imageview-Array der GUI in die angegebene Richtung um 90Grad
     *
     * @param quadNum Index des Quadranten in Leserichtung
     * @param toLeft Drehung nach links
     */
    public void rotateImageViews(int quadNum, boolean toLeft) {
        if (toLeft) {
            for (int i = 0; i < 2; i++) {
                ImageView temp = (ImageView) getNodeFromGridPane((GridPane) gridQuadrants[quadNum], 2 - i, 0);

                //0,0 -> 2,0
                // 0,1 -> 1,0
                ImageView iv = (ImageView) getNodeFromGridPane((GridPane) gridQuadrants[quadNum], 0, 0 + i);

                //Imageview an das neue Ziel setzen
                GridPane.setConstraints(iv, 0, 2 - i);

                // 0,2 -> 0,0
                // 1,2 -> 0,1
                iv = (ImageView) getNodeFromGridPane((GridPane) gridQuadrants[quadNum], 0 + i, 2);
                GridPane.setConstraints(iv, 0 + i, 0);

                //2,2 -> 0,2
                //2,1 -> 1,2
                iv = (ImageView) getNodeFromGridPane((GridPane) gridQuadrants[quadNum], 2, 2 - i);
                GridPane.setConstraints(iv, 2, 0 + i);

                //2,0 -> 2,2
                // 1,0 -> 2,1
                GridPane.setConstraints(temp, 2 - i, 2);
            }
        } else {
            // Rechtsdrehung
            for (int i = 0; i < 2; i++) {
                ImageView temp = (ImageView) getNodeFromGridPane((GridPane) gridQuadrants[quadNum], 0 + i, 2);
                // 0,0 -> 0,2
                // 0,1 -> 1,2
                ImageView iv_to_translate = (ImageView) getNodeFromGridPane((GridPane) gridQuadrants[quadNum], 0, 0 + i);
                GridPane.setConstraints(iv_to_translate, 2, 0 + i);

                //2,0 -> 0,0
                // 1,0 -> 0,1
                iv_to_translate = (ImageView) getNodeFromGridPane((GridPane) gridQuadrants[quadNum], 2 - i, 0);
                GridPane.setConstraints(iv_to_translate, 0 + i, 0);

                //2,2 -> 2,0
                //2,1 -> 1,0
                iv_to_translate = (ImageView) getNodeFromGridPane((GridPane) gridQuadrants[quadNum], 2, 2 - i);
                GridPane.setConstraints(iv_to_translate, 0, 2 - i);

                // 0,2 -> 2,2
                // 1,2 -> 2,1
                GridPane.setConstraints(temp, 2 - i, 2);
            }
        }

    }

    @Override
    public void alertWin(Player pl, GameLogic logic) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Ein Sieger steht fest");
        alert.setHeaderText(null);
        if (pl == Player.HUMANPLAYER) {
            alert.setContentText("Glückwunsch! Du hast gewonnen. Klicke Ok um ein neues Spiel zu starten");
        } else {
            alert.setContentText("Loooooooser!!!. Klicke Ok um es nochmal zu versuchen");
        }
        alert.show();
        alert.setOnHidden(event -> {
            logic.reset();
        });
    }

    @Override
    public void alertDraw(Player p0, Player p1, GameLogic logic) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Unentschieden");
        alert.setHeaderText(null);
        alert.setContentText("Das Spiel endet Unentschieden. Klicke Ok um ein neues Spiel zu starten");
        alert.show();
        alert.setOnHidden(event -> {
            logic.reset();
        });
    }

    @Override
    public void clearLog(boolean allEntrys) {
        // Protkoll vollstandig leeren
        if (allEntrys) {
            this.log.clear();
            // die letzen beiden Eintraege des Protokolls entfernen
        } else {
            //this.log.undo();
            String[] text = this.log.getText().split("\n");
            String removedLastLine = "";

            // Die letzen 5 Zeilen des Protkolls ueberspringen
            for (int i = 0; i < text.length - 5; i++) {
                removedLastLine += text[i] + "\n";
            }
            this.log.setText(removedLastLine);
        }

    }

    @Override
    public void alertError(String err) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Fehler");
        alert.setHeaderText("Da ist was schiefgelaufen");
        alert.setContentText(err);
        alert.show();
    }

    @Override
    public void markWin(int xCoord, int yCoord) {
        Glow glow = new Glow(0.4);
        getImageView(xCoord, yCoord).setEffect(glow);
    }

    @Override
    public void disableItmSaveAndHistoryButtons(boolean disable) {
        this.itmSave.setDisable(disable);
        this.btnUndo.setDisable(disable);
        this.btnRedo.setDisable(disable);
    }

    @Override
    public void disableRotButtons(boolean disable) {
        for (Button btn : this.rotButtons) {
            btn.setDisable(disable);
        }
    }

}
