package com.example.dotsandboxes;

import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

public class ConnectLine extends Line {
    private Color lineColor;
    private Dot startDot;
    private Dot endDot;

    public ConnectLine(double startX, double startY, double endX, double endY, Color lineColor, Dot startDot, Dot endDot) {
        super(startX, startY, endX, endY);
        this.lineColor = lineColor;
        this.startDot = startDot;
        this.endDot = endDot;
        setStroke(lineColor);
    }

    public Color getLineColor() {
        return lineColor;
    }

    public void setLineColor(Color lineColor) {
        this.lineColor = lineColor;
        setStroke(lineColor);
    }

    public Dot getStartDot() {
        return startDot;
    }

    public Dot getEndDot() {
        return endDot;
    }
}
