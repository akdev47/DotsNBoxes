package com.example.dotsandboxes;

import javafx.scene.paint.Color;

import static com.example.dotsandboxes.DotsAndBoxes.GRID_SIZE;

public class Game {

    private Player player1;
    private Player player2;

    private boolean gameTurnPlayer1 = true;

    private Color currentPlayerColor;



    private double maxScore;

    public Game() {
        player1 = new Player(Color.RED);
        player2 = new Player(Color.BLUE);
        maxScore = Math.pow(GRID_SIZE-1,2);
    }

    public void incrementPlayer1Score() {
        player1.incrementScore();
    }

    public void incrementPlayer2Score() {
        player2.incrementScore();
    }

    public boolean isGameOver() {
        return (player1.getPlayerScore()== maxScore || player2.getPlayerScore()==maxScore);
    }

    public void switchTurn() {
        gameTurnPlayer1 = !gameTurnPlayer1;
    }

    public Color getCurrentPlayerColor() {
        if(gameTurnPlayer1) {
            return player1.getColor();
        }
        else {
            return player2.getColor();
        }
    }

    public void incrementCurrentPlayer() {
        if(gameTurnPlayer1) {
            incrementPlayer1Score();
        }
        else {
            incrementPlayer2Score();
        }
    }

    public String toString() {
        if(gameTurnPlayer1) {
            return "Player 1 Turn";
        }
        else {
            return "Player 2 Turn";
        }
    }
    public boolean isGameTurnPlayer1() {
        return gameTurnPlayer1;
    }
    public Player getPlayer1() {
        return player1;
    }

    public Player getPlayer2() {
        return player2;
    }


}
