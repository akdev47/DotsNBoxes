package com.example.dotsandboxes;

import javafx.scene.paint.Color;

public class Player {


    private int playerScore = 0;
    private Color playerColor;
    private String name;


    public Player(Color color, String name) {

        playerColor = color;
        this.name = name;
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

    public void resetPlayerScore() {
        playerScore = 0;
    }

    public String getName() {
        return name;
    }
}
