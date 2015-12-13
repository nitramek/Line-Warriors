package cz.nitramek.linewarriors.game.utils;


public interface GameStateListener {
    void goldChanged(int gold);

    void healthChanged(int health);

    void escapedChanged(int remainingEscapes);

}
