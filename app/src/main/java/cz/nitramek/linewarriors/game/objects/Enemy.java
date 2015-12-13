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
        return 0.005f;
    }

    public boolean behindLine(float y) {
        return this.sprite.getModelMatrix().getBoundingBox().bottom < y;
    }

    public void attack(MainCharacter character) {
        character.obtainDamage(this.damage);
    }

    public void obtainDamage(int damage) {
        this.health -= damage;
        if (this.isDead()) {
            this.requestRemoval();
        }
    }

    @Override
    public boolean collide(Model other) {
        final boolean collide = super.collide(other);
        if(collide && other instanceof MainCharacter){
            ((MainCharacter) other).obtainDamage(this.damage);
        }
        return collide;
    }

    public boolean isDead() {
        return this.health <= 0;
    }


}
