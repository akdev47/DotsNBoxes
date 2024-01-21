package BoardDrawingGame.logic;

import BoardDrawingGame.view.SquaresToDrawForUI;

public interface MoveMadeListener {
    void onMoveMade(SquaresToDrawForUI info);

    void gameOver();

}
