package cz.nitramek.linewarriors.game.objects;


import android.graphics.RectF;

import cz.nitramek.linewarriors.game.utils.Vector;

public class MainCharacter extends Model {

    float speed;

    public MainCharacter(Sprite sprite) {
        super(sprite);
        this.speed = 0.05f;
    }

    @Override
    public float getSpeed() {
        return this.speed;
    }

    @Override
    public void move(Vector direction) {

        final RectF b = this.sprite.getModelMatrix().getBoundingBox();
        final boolean isOut = b.top < 0.8f &&
                b.left > -0.8f &&
                b.bottom > -0.8f &&
                b.right < 0.8f;
        if (isOut) {
            super.move(direction);
            //TODO vyřešit zaseknutí na kraji
        }


    }
}
