package tictactoe;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.TranslateTransition;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;

public class TttController implements Initializable {

    @FXML
    private Label b1, b2, b3, b4, b5, b6, b7, b8, b9, turn, title;
    @FXML
    private ToggleButton refresh, toggle;
    @FXML
    private GridPane grid;
    
    private boolean xTurn = true;
    private boolean gameActive = true;
    private Label[][] board;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        board = new Label[][]{{b1, b2, b3}, {b4, b5, b6}, {b7, b8, b9}};
        updateTurnLabel();
        
        for (Label[] row : board) {
            for (Label cell : row) {
                cell.setOnMouseClicked(this::handleMove);
                applyFloatingEffect(cell);
                applyFloatingEffectToTitle();
            }
        }
        
        refresh.setOnMouseClicked(e -> resetGame());
        toggle.setOnAction(e -> toggleFirstPlayer());

        // Apply CSS when scene is set
        grid.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                newScene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
                newScene.setOnKeyPressed(e -> {
                    if (e.getCode() == KeyCode.R) resetGame();
                    if (e.getCode() == KeyCode.P) toggleFirstPlayer();
                });
            }
        });
    }

    private void applyFloatingEffect(Label label) {
        TranslateTransition transition = new TranslateTransition(Duration.seconds(2), label);
        transition.setByY(-2);
        transition.setAutoReverse(true);
        transition.setCycleCount(Timeline.INDEFINITE);
        transition.play();
    }
    private void applyFloatingEffectToTitle() {
    TranslateTransition transition = new TranslateTransition(Duration.seconds(1.5), title);
    transition.setByY(-5);
    transition.setAutoReverse(true);
    transition.setCycleCount(Timeline.INDEFINITE);
    transition.play();
}

    private void handleMove(MouseEvent event) {
        if (!gameActive) return;
        
        Label clicked = (Label) event.getSource();
        if (!clicked.getText().isEmpty()) return;

        clicked.setText(xTurn ? "X" : "O");
        if (checkWinner()) {
            gameActive = false;
            return;
        }

        xTurn = !xTurn;
        updateTurnLabel();
    }

    private boolean checkWinner() {
        String winner = null;
        String[][] values = new String[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                values[i][j] = board[i][j].getText();
            }
        }

        for (int i = 0; i < 3; i++) {
            if (!values[i][0].isEmpty() && values[i][0].equals(values[i][1]) && values[i][1].equals(values[i][2]))
                winner = values[i][0];
            if (!values[0][i].isEmpty() && values[0][i].equals(values[1][i]) && values[1][i].equals(values[2][i]))
                winner = values[0][i];
        }

        if (!values[0][0].isEmpty() && values[0][0].equals(values[1][1]) && values[1][1].equals(values[2][2]))
            winner = values[0][0];
        if (!values[0][2].isEmpty() && values[0][2].equals(values[1][1]) && values[1][1].equals(values[2][0]))
            winner = values[0][2];

        if (winner != null) {
            showPrompt(winner + " Wins!");
            gameActive = false;
            return true;
        }

        boolean draw = true;
        for (Label[] row : board) {
            for (Label cell : row) {
                if (cell.getText().isEmpty()) draw = false;
            }
        }
        if (draw) {
            showPrompt("It's a Draw!");
            gameActive = false;
            return true;
        }
        return false;
    }

    private void showPrompt(String message) {
        turn.setText(message);
    }

    private void updateTurnLabel() {
        if (gameActive) {
            turn.setText("Turn: " + (xTurn ? "X" : "O"));
        }
        toggle.setText("Start with: " + (xTurn ? "X" : "O"));
    }

    private void resetGame() {
        resetBoard();
        xTurn = true;
        gameActive = true;
        updateTurnLabel();
    }

    private void resetBoard() {
        for (Label[] row : board) {
            for (Label cell : row) {
                cell.setText("");
            }
        }
        gameActive = true;
    }

    private void toggleFirstPlayer() {
        xTurn = !xTurn;
        updateTurnLabel();
    }
}
