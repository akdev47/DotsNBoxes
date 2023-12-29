package com.example.dotsandboxes;

import javafx.scene.paint.Color;

public class Player {


    private int playerScore = 0;
    private Color playerColor;

    public Player(Color color) {
        playerColor = color;
    }

    public int getPlayerScore() {
        return playerScore;
    }

    public void incrementScore() {
        playerScore++;
    }

    public Color getColor() {
        return playerColor;
    }
}
