package cz.nitramek.linewarriors.game.utils;


import android.graphics.RectF;

public final class Constants {

    public static final RectF UNIT_BOX = new RectF(1f, 1f, -1f, -1f);
    public static final int STARTING_GOLD = 50;
    public static final int STARTING_HEALTH = 20;
    public static final int RESPAWN_TIME = 5;
    public static final int STARTING_ESCAPES = 50;
    public static final String SP_STATS = "STATS";
    public static final String SP_KEY_KILLS = "KILLS";
    public static final String GAME_OVER_MSS = "game_over";

    private Constants() {
    }
}
