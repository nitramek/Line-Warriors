package cz.nitramek.linewarriors.game.objects;


import android.graphics.RectF;

import cz.nitramek.linewarriors.game.utils.Vector;

public class MainCharacter extends Model {

    float speed;

    boolean moveable;

    public MainCharacter(Sprite sprite) {
        super(sprite);
        this.speed = 0.05f;
        this.moveable = true;
    }

    @Override
    public float getSpeed() {
        return this.speed;
    }

    @Override
    public void move(Vector direction) {
        if(this.moveable) {
            final RectF b = this.sprite.getModelMatrix().getBoundingBox();

            final boolean isOut = b.top < 0.9f &&
                    b.left > -0.9f &&
                    b.bottom > -0.9f &&
                    b.right < 0.9f;
            if (isOut) {
                super.move(direction);
                //TODO vyřešit zaseknutí na kraji
            }
        }
    }

    public void setMoveable(boolean moveable) {
        this.moveable = moveable;
    }
}
