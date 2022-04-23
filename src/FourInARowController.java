import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;



public class FourInARowController implements Initializable {
    private final int N_COLUMNS = 7;
    private final int N_ROWS = 6;
    private final int TILE_SIZE = 120;
    private enum Player {
        RED, GOLD
    }
    private Player currentPlayer;
    private final boolean[][] pointsPlayer1 = new boolean[N_ROWS][N_COLUMNS];
    private final boolean[][] pointsPlayer2 = new boolean[N_ROWS][N_COLUMNS];
    private final boolean[][] gridPoints = new boolean[N_ROWS][N_COLUMNS];

    private int gridPosX;
    private int gridPosY;
    @FXML
    private Pane gameBoard;
    @FXML
    private TextField playerTextField;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        drawGrid();
        currentPlayer = Player.RED;
        resetPoints();
    }
    private void drawGrid() {
        Shape gameGrid = new Rectangle(N_COLUMNS * TILE_SIZE, N_ROWS * TILE_SIZE);
        for (int i = 0; i < N_ROWS; i++) {
            for (int j = 0; j< N_COLUMNS; j++) {
                Circle circle = new Circle(
                        (double) TILE_SIZE * (j + 0.5),
                        (double) TILE_SIZE * (i + 0.5),
                        (double) TILE_SIZE / 3
                );
                gameGrid = Shape.subtract(gameGrid, circle);
            }
        }
        gameBoard.getChildren().add(gameGrid);
        gameGrid.setFill(Color.BLUE);
    }
    private boolean isFourInARow(boolean[][] gridPoints) {
        for (int i = 0; i < N_ROWS - 3; i++) {
            for (int j = 0; j < N_COLUMNS - 3; j++) {
                if (gridPoints[i][j]) {
                    if (isDiag(gridPoints, i, j) || isHorizontal(gridPoints, i, j) || isVertical(gridPoints, i, j)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean isVertical(boolean[][] gridPoints, int i, int j) {
        return gridPoints[i][j] && gridPoints[i+1][j+1] && gridPoints[i+2][j+2] && gridPoints[i+3][j+3];
    }

    private boolean isHorizontal(boolean[][] gridPoints, int i, int j) {
        return gridPoints[i][j] && gridPoints[i][j+1] && gridPoints[i][j+2] && gridPoints[i][j+3];
    }

    private boolean isDiag(boolean[][] gridPoints, int i, int j) {
        return gridPoints[i][j] && gridPoints[i+1][j] && gridPoints[i+2][j] && gridPoints[i+3][j];
    }
    @FXML
    private void gameBoardClicked(MouseEvent e) {
        gridPosX = (int) e.getX()/TILE_SIZE;
        gridPosY = (int) e.getY()/TILE_SIZE;
        if (currentPlayer == Player.RED) {
            playerTextField.setText("RED");
            if (gridPoints[gridPosY][gridPosX]) {
                return;
            } else {
                movePlayer1();
            }
        } else if(currentPlayer == Player.GOLD) {
            playerTextField.setText("GOLD");
            if (gridPoints[gridPosY][gridPosX]) {
                return;
            } else {
                movePlayer2();
            }
        }
        gridPoints[gridPosY][gridPosX] = true;
        checkVictory();
        changePlayer();
    }

    private void movePlayer2() {
        pointsPlayer2[gridPosY][gridPosX] = true;
        Circle disk = new Circle(
                (double) TILE_SIZE * ((double) gridPosX + 0.5),
                (double) TILE_SIZE * ((double) gridPosY + 0.5),
                (double) TILE_SIZE / 3
        );
        disk.setFill(Color.GOLD);
        gameBoard.getChildren().add(disk);
    }

    private void movePlayer1() {
        pointsPlayer1[gridPosY][gridPosX] = true;
        Circle disk = new Circle(
                (double) TILE_SIZE * ((double) gridPosX + 0.5),
                (double) TILE_SIZE * ((double) gridPosY + 0.5),
                (double) TILE_SIZE / 3
        );
        disk.setFill(Color.RED);
        gameBoard.getChildren().add(disk);
    }

    private void checkVictory() {
        if (isFourInARow(pointsPlayer1)) {
            Alert alert = new Alert(
                    Alert.AlertType.NONE,
                    "Player RED has won! Would you like to play again?",
                    ButtonType.OK
            );
            alert.setTitle("Victory!");
            alert.showAndWait();
            reset();
        } else if (isFourInARow(pointsPlayer2)) {
            Alert alert = new Alert(
                    Alert.AlertType.NONE,
                    "Player GOLD has won! Would you like to play again?",
                    ButtonType.OK
            );
            alert.setTitle("Victory!");
            alert.showAndWait();
            reset();
        }
    }

    private void changePlayer() {
        if (currentPlayer == Player.RED) {
            currentPlayer = Player.GOLD;
        } else if (currentPlayer == Player.GOLD) {
            currentPlayer = Player.RED;
        }
    }
    private void resetPoints() {
        Arrays.stream(pointsPlayer1).forEach(a -> Arrays.fill(a, false));
        Arrays.stream(pointsPlayer2).forEach(a -> Arrays.fill(a, false));
        Arrays.stream(gridPoints).forEach(a -> Arrays.fill(a, false));
    }
    @FXML
    private void clearButtonClicked(MouseEvent e) {
        reset();
    }

    private void reset() {
        resetPoints();
        gameBoard.getChildren().clear();
        drawGrid();
        currentPlayer = Player.RED;
    }
}
