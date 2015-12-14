package cz.nitramek.linewarriors.game.objects;


import android.graphics.RectF;

import cz.nitramek.linewarriors.game.utils.SpriteDirection;
import cz.nitramek.linewarriors.game.utils.Vector;

public abstract class Model {

    protected Sprite sprite;


    private SpriteDirection direction;
    public Model(Sprite sprite) {
        this.sprite = sprite;
    }

    /**
     * Move into defined direction
     *
     * @param direction defines x,y direction of vector, must be normalized to work properly
     */
    public void move(Vector direction) {

        //represents X
        if (direction.angleDegrees() > 315f) {
            this.sprite.setSpriteY(SpriteDirection.RIGHT.row);
            this.direction = SpriteDirection.RIGHT;
        } else if (direction.angleDegrees() > 225f) {
            this.sprite.setSpriteY(SpriteDirection.BOTTOM.row);
            this.direction = SpriteDirection.BOTTOM;
        } else if (direction.angleDegrees() > 135.f) {
            this.sprite.setSpriteY(SpriteDirection.LEFT.row);
            this.direction = SpriteDirection.LEFT;
        } else if (direction.angleDegrees() > 45.f) {
            this.sprite.setSpriteY(SpriteDirection.TOP.row);
            this.direction = SpriteDirection.TOP;
        } else {
            this.sprite.setSpriteY(SpriteDirection.RIGHT.row);
            this.direction = SpriteDirection.RIGHT;
        }
        direction.multiply(this.getSpeed());
        this.sprite.getModelMatrix().translate(direction);
        this.sprite.incrementX();

    }

    public boolean collide(Model other) {
        final RectF boundingBox = this.sprite.getModelMatrix().getBoundingBox();
        final RectF otherBox = other.sprite.getModelMatrix().getBoundingBox();

        return !(otherBox.left > boundingBox.right
                || otherBox.right < boundingBox.left
                || otherBox.top < boundingBox.bottom
                || otherBox.bottom > boundingBox.top);
    }

    public abstract float getSpeed();

    public void requestSpriteRemoval() {
        this.sprite.setRemove(true);
    }

    public void setPosition(float x, float y) {
        this.sprite.getModelMatrix().setPosition(x, y);
    }

    public RectF getBoundingBox() {
        return this.sprite.getModelMatrix().getBoundingBox();
    }

    public SpriteDirection getDirection() {
        return direction;
    }
}
