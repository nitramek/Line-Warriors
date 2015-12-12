package cz.nitramek.linewarriors.game.utils;


public enum SpriteDirection {
    LEFT(1),
    RIGHT(2),
    TOP(0),
    BOTTOM(3);

    public final int row;

    SpriteDirection(int row) {
        this.row = row;
    }

}
