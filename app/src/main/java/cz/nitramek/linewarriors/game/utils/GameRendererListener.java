package cz.nitramek.linewarriors.game.utils;


import cz.nitramek.linewarriors.game.objects.Drawable;

public interface GameRendererListener {
    void addDrawable(Drawable drawable);

    float getRatio();
}
