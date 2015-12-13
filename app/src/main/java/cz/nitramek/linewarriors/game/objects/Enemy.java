package cz.nitramek.linewarriors.game.objects;


import cz.nitramek.linewarriors.game.utils.Monster;

public class Enemy extends Model {
    private int damage;
    private int health;


    private float speed;


    public Enemy(Monster monster, Sprite sprite) {
        super(sprite);
        this.damage = monster.damage;
        this.health = monster.health;
        this.speed = monster.speed;
    }

    @Override
    public float getSpeed() {
        return this.speed;
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
        if (collide && other instanceof MainCharacter) {
            ((MainCharacter) other).obtainDamage(this.damage);
        }
        return collide;
    }

    public boolean isDead() {
        return this.health <= 0;
    }


}
