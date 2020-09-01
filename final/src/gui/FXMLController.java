package gui;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import logic.GameLogic;
import logic.Quadrant;

/**
 * FXML Controller class, erstellt die Oberflaeche, instanziert die GUI und die
 * Logik. Reagiert auf Ereignisse auf der Oberflaeche und kommuniziert mit der
 * Logik.
 *
 * @author Dimitri Solodownik, inf102736
 */
public class FXMLController implements Initializable {

    @FXML
    private GridPane main_grid;

    private GameLogic logic;

    private JavaFXGUI gui;
    @FXML
    private BorderPane borPane;
    @FXML
    private AnchorPane anchorPn;
    @FXML
    private TextArea textArea;
    @FXML
    private Button btn0Right;
    @FXML
    private Button btn0Left;
    @FXML
    private Button btn1Left;
    @FXML
    private Button btn3Right;
    @FXML
    private Button btn2Left;
    @FXML
    private Button btn2Right;
    @FXML
    private Button btn1Right;
    @FXML
    private Button btn3Left;
    @FXML
    private CheckMenuItem checkItmDiff0;
    @FXML
    private CheckMenuItem checkItmDiff1;
    @FXML
    private CheckMenuItem checkItmDiff2;
    @FXML
    private CheckMenuItem checkItmDefensive;
    @FXML
    private CheckMenuItem checkItmOffensive;
    @FXML
    private MenuItem btnSave;
    @FXML
    private Button btnUndo;
    @FXML
    private Button btnRedo;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        manageButtons();

        main_grid.setStyle("-fx-border-style: solid;-fx-border-width: 2");

        this.gui = new JavaFXGUI(initImages(), main_grid.getChildren().toArray(new Node[4]), textArea, btnSave, new Button[]{btn0Left, btn0Right,
            btn1Left, btn1Right, btn2Left, btn2Right, btn3Left, btn3Right},
                btnUndo, btnRedo);
        this.logic = new GameLogic(gui, null);

        anchorPn.maxWidthProperty().bind(anchorPn.heightProperty());
        anchorPn.maxHeightProperty().bind(borPane.widthProperty().subtract(textArea.widthProperty()));

        textArea.textProperty().addListener(new ChangeListener<Object>() {
            @Override
            public void changed(ObservableValue<? extends Object> observable, Object oldValue, Object newValue) {
                textArea.setScrollTop(Double.MIN_VALUE);
            }

        });

        checkItmDiff1.setSelected(true);
        checkItmOffensive.setSelected(true);
        textArea.setEditable(false);

    }

    private void manageButtons() {
        ImageView left0 = new ImageView(new Image("/gui/images/left0.png"));
        left0.setFitWidth(20);
        left0.setFitHeight(45);
        btn0Left.setGraphic(left0);
        btn0Left.prefHeightProperty().bind(main_grid.heightProperty().divide(5));

        ImageView left1 = new ImageView(new Image("/gui/images/left1.png"));
        left1.setFitWidth(45);
        left1.setFitHeight(20);
        btn1Left.setGraphic(left1);
        btn1Left.prefWidthProperty().bind(main_grid.widthProperty().divide(4));

        ImageView left2 = new ImageView(new Image("/gui/images/left2.png"));
        left2.setFitWidth(45);
        left2.setFitHeight(20);
        btn2Left.setGraphic(left2);
        btn2Left.prefWidthProperty().bind(main_grid.widthProperty().divide(4));

        ImageView left3 = new ImageView(new Image("/gui/images/left3.png"));
        left3.setFitWidth(20);
        left3.setFitHeight(45);
        btn3Left.setGraphic(left3);
        btn3Left.prefHeightProperty().bind(main_grid.heightProperty().divide(4));

        ImageView right0 = new ImageView(new Image("/gui/images/right0.png"));
        right0.setFitWidth(45);
        right0.setFitHeight(20);
        btn0Right.setGraphic(right0);
        btn0Right.prefWidthProperty().bind(main_grid.widthProperty().divide(4));

        ImageView right1 = new ImageView(new Image("/gui/images/right1.png"));
        right1.setFitWidth(20);
        right1.setFitHeight(45);
        btn1Right.setGraphic(right1);
        btn1Right.prefHeightProperty().bind(main_grid.heightProperty().divide(4));

        ImageView right2 = new ImageView(new Image("/gui/images/right2.png"));
        right2.setFitWidth(20);
        right2.setFitHeight(45);
        btn2Right.setGraphic(right2);
        btn2Right.prefHeightProperty().bind(main_grid.heightProperty().divide(4));

        ImageView right3 = new ImageView(new Image("/gui/images/right3.png"));
        right3.setFitWidth(45);
        right3.setFitHeight(20);
        btn3Right.setGraphic(right3);
        btn3Right.prefWidthProperty().bind(main_grid.widthProperty().divide(4));

    }

    /*
    
     */
    private ImageView[][][] initImages() {
        ImageView[][][] result_ivs = new ImageView[4][3][3];

        int i = 0;

        for (Node child : main_grid.getChildren()) {
            GridPane gp = (GridPane) child;

            int rowcount = gp.getRowConstraints().size();
            int colcount = gp.getColumnConstraints().size();

            ImageView[][] imageViews = new ImageView[rowcount][colcount];               //zeile = row

            for (int x = 0; x < rowcount; x++) {
                for (int y = 0; y < colcount; y++) {
                    //creates an empty imageview
                    imageViews[x][y] = new ImageView();

                    //add the imageview to the cell and
                    //assign the correct indicees for this imageview,
                    //so you later can use GridPane.getColumnIndex(Node)
                    gp.add(imageViews[x][y], y, x);

                    //the image shall resize when the cell resizes
                    imageViews[x][y].fitWidthProperty().bind(gp.widthProperty().divide(colcount));
                    imageViews[x][y].fitHeightProperty().bind(gp.heightProperty().divide(rowcount));
                }

            }
            result_ivs[i++] = imageViews;

        }
        return result_ivs;
    }

    @FXML
    private void act_itmNew(ActionEvent event) {
        logic.reset();
    }

    @FXML
    private void actBtn0Right(ActionEvent event) {
        logic.handleRotation(Quadrant.TOP_LEFT, false);
    }

    @FXML
    private void actBtn0Left(ActionEvent event) {
        logic.handleRotation(Quadrant.TOP_LEFT, true);
    }

    @FXML
    private void actBtn1Left(ActionEvent event) {
        logic.handleRotation(Quadrant.TOP_RIGHT, true);
    }

    @FXML
    private void actBtn1Right(ActionEvent event) {
        logic.handleRotation(Quadrant.TOP_RIGHT, false);
    }

    @FXML
    private void actBtn2Left(ActionEvent event) {
        logic.handleRotation(Quadrant.BOTTOM_LEFT, true);
    }

    @FXML
    private void actBtn2Right(ActionEvent event) {
        logic.handleRotation(Quadrant.BOTTOM_LEFT, false);
    }

    @FXML
    private void actBtn3Left(ActionEvent event) {
        logic.handleRotation(Quadrant.BOTTOM_RIGHT, true);
    }

    @FXML
    private void actBtn3Right(ActionEvent event) {
        logic.handleRotation(Quadrant.BOTTOM_RIGHT, false);
    }

    /**
     * Ermittelt den zuerst gefundenen Quadranten der zu den Koordinaten passt
     *
     * @param grid indem gesucht wird
     * @param col Spalte an der gesucht wird
     * @param row Zeile an der gesucht wird
     * @return Node das den Koordinaten angehoert
     */
    public GridPane getQuadrantFromGridPane(int col, int row) {
        Node foundNode = null;
        for (Node node : main_grid.getChildren()) {

            if (GridPane.getColumnIndex(node) == col && GridPane.getRowIndex(node) == row) {
                foundNode = node;
            }

        }
        return (GridPane) foundNode;
    }

    @FXML
    private void mouseClickGridPane(MouseEvent event) {
        GridPane selectedBlock = (GridPane) event.getSource();
        int x = -1;
        int y = -1;

        // die Koordinaten der ausgewaehlten Position ermitteln
        for (Node node : selectedBlock.getChildren()) {
            if (node instanceof ImageView) {

                if (node.getBoundsInParent().contains(event.getX(), event.getY())) {
                    //columnIndex und rowIndex müssen beim Hinzufügen des ImageView
                    //gesetzt worden sein 
                    // x - horizontal
                    y = GridPane.getColumnIndex(node);

                    x = GridPane.getRowIndex(node);
                }
            }
        }

        if (x != -1) {
            int quadrantColNum = main_grid.getColumnIndex(selectedBlock);
            int quadrantRowNum = main_grid.getRowIndex(selectedBlock);
            logic.handlePlayerMove(quadrantRowNum, quadrantColNum, x, y);
        }

    }

    @FXML
    private void actBtnUndo(ActionEvent event) {
        logic.undoMove();
    }

    @FXML
    private void actBtnRedo(ActionEvent event) {
        logic.redoMove();
    }

    @FXML
    private void loadGame(ActionEvent event) {
        File file;
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Spielstand laden");
        String userDirectoryString = System.getProperty("user.dir");
        File userDirectory = new File(userDirectoryString);
        if (!userDirectory.canRead()) {
            userDirectory = new File("c:/");
        }
        fileChooser.setInitialDirectory(userDirectory);
        file = fileChooser.showOpenDialog(null);
        //File file = fileChooser.showOpenDialog(main_grid.getScene().getWindow());
        if (file != null) {
            logic.loadFile(file);
        }
    }

    @FXML
    private void saveGame(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();

        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("PEN files (*.pen)", "*.pen");
        fileChooser.getExtensionFilters().add(extFilter);

        String userDirectoryString = System.getProperty("user.dir");
        File userDirectory = new File(userDirectoryString);
        if (!userDirectory.canRead()) {
            userDirectory = new File("c:/");
        }
        fileChooser.setInitialDirectory(userDirectory);

        //Show save file dialog
        File file = fileChooser.showSaveDialog(null);
        if (file != null) {
            logic.saveGame(file);
        }
    }

    @FXML
    private void actDefensive(ActionEvent event) {
        if (checkItmDefensive.isSelected()) {
            logic.setStrategy(true);
            checkItmOffensive.setSelected(false);
        } else {
            checkItmDefensive.setSelected(true);
        }
    }

    @FXML
    private void actOffensive(ActionEvent event) {
        if (checkItmOffensive.isSelected()) {
            logic.setStrategy(false);
            checkItmDefensive.setSelected(false);
        } else {
            checkItmOffensive.setSelected(true);
        }
    }

    @FXML
    private void actnMenuDifficulty0(ActionEvent event) {
        if (checkItmDiff0.isSelected()) {
            logic.setDifficulty(0);
            checkItmDiff1.setSelected(false);
            checkItmDiff2.setSelected(false);
        } else {
            checkItmDiff0.setSelected(true);
        }
    }

    @FXML
    private void actnMenuDifficulty1(ActionEvent event) {
        if (checkItmDiff1.isSelected()) {
            logic.setDifficulty(1);
            checkItmDiff0.setSelected(false);
            checkItmDiff2.setSelected(false);
        } else {
            checkItmDiff1.setSelected(true);
        }
    }

    @FXML
    private void actMenuDifficulty2(ActionEvent event) {
        if (checkItmDiff2.isSelected()) {
            logic.setDifficulty(2);
            checkItmDiff0.setSelected(false);
            checkItmDiff1.setSelected(false);
        } else {
            checkItmDiff2.setSelected(true);
        }
    }

    @FXML
    private void actClose(ActionEvent event) {
        System.exit(0);
    }

}
