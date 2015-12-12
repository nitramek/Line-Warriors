package cz.nitramek.linewarriors.game.objects;


import cz.nitramek.linewarriors.game.utils.ModelMatrix;
import cz.nitramek.linewarriors.game.utils.SpriteDirection;
import cz.nitramek.linewarriors.game.utils.Vector;

public abstract class Model {

    protected Sprite sprite;
    protected ModelMatrix modelMatrix;


    public Model(Sprite sprite) {
        this.sprite = sprite;
        this.modelMatrix = new ModelMatrix();
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
        } else if (direction.angleDegrees() > 225f) {
            this.sprite.setSpriteY(SpriteDirection.BOTTOM.row);
        } else if (direction.angleDegrees() > 135.f) {
            this.sprite.setSpriteY(SpriteDirection.LEFT.row);
        } else if (direction.angleDegrees() > 45.f) {
            this.sprite.setSpriteY(SpriteDirection.TOP.row);
        } else {
            this.sprite.setSpriteY(SpriteDirection.RIGHT.row);
        }
        direction.multiply(this.getSpeed());
        this.sprite.getModelMatrix().translate (direction);
        this.sprite.incrementX();

    }

    public abstract float getSpeed();


}
