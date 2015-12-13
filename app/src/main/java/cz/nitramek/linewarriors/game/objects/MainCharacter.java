package cz.nitramek.linewarriors.game.objects;


import android.graphics.RectF;

import cz.nitramek.linewarriors.game.StateChangedListener;
import cz.nitramek.linewarriors.game.utils.Constants;
import cz.nitramek.linewarriors.game.utils.OnAbilityCast;
import cz.nitramek.linewarriors.game.utils.SpellManager;
import cz.nitramek.linewarriors.game.utils.Vector;

public abstract class MainCharacter extends Model {

    private final OnAbilityCast listener;
    private final StateChangedListener stateChangedListener;
    float speed;

    boolean movable;

    private int health;

    public MainCharacter(Sprite sprite) {
        this(sprite, null, null);
    }

    public MainCharacter(Sprite sprite, OnAbilityCast listener, StateChangedListener stateChangedListener) {
        super(sprite);
        this.listener = listener;
        this.stateChangedListener = stateChangedListener;
        this.speed = 0.005f;
        this.movable = true;
        this.health = Constants.STARTING_HEALTH;
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

    protected void cast(SpellManager.SpellType type) {
        this.listener.onCast(type);
    }

    public abstract void castFirstAbility();

    public void obtainDamage(int damage) {
        this.health -= damage;
        if (this.isDead()) {
            this.stateChangedListener.onDeath(false);
        }
    }

    @Override
    public boolean collide(Model other) {
        return super.collide(other);
    }

    public boolean isDead() {
        return this.health < 0;
    }

    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
    }

    public int getHealth() {
        return this.health;
    }
}
