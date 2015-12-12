package cz.nitramek.linewarriors.game.objects;


public class Enemy extends Model {
    private int damage;
    private int health;


    public Enemy(int damage, int health, Sprite sprite) {
        super(sprite);
        this.damage = damage;
        this.health = health;
    }

    @Override
    public float getSpeed() {
        return 0.05f;
    }

    public boolean behindLine(float y) {
        return this.sprite.getModelMatrix().getBoundingBox().bottom < y;
    }


}
