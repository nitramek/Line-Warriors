package cz.nitramek.linewarriors.game.utils;


import android.graphics.Rect;

public class Dimensions {
    public final int width;

    public final int height;

    public final float ratio;

    public final Rect boundaries;

    public Dimensions(int width, int height, float ratio, Rect boundaries) {
        this.width = width;
        this.height = height;
        this.ratio = ratio;
        this.boundaries = boundaries;
    }
}
