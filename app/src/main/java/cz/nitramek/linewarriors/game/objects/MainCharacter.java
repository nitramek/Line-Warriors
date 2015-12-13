package cz.nitramek.linewarriors.game.objects;


import android.graphics.RectF;

import cz.nitramek.linewarriors.game.utils.Vector;

public abstract class MainCharacter extends Model {

    float speed;

    boolean movable;

    private int casting = 0;
    private int health;

    public MainCharacter(Sprite sprite) {
        super(sprite);
        this.speed = 0.005f;
        this.movable = true;
    }

    @Override
    public float getSpeed() {
        return this.speed;
    }

    @Override
    public void move(Vector direction) {
        if (this.movable) {
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

    public void setMovable(boolean movable) {
        this.movable = movable;
    }

    public void castFirstAbility() {
        casting = 0;
    }

    public int getCasting() {
        return casting;
    }

    public void obtainDamage(int damage) {
        this.health -= damage;
    }

}
