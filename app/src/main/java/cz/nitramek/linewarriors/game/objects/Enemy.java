package cz.nitramek.linewarriors.game.objects;


import cz.nitramek.linewarriors.game.StateChangedListener;
import cz.nitramek.linewarriors.game.utils.Monster;

public class Enemy extends Model {
    private final StateChangedListener stateChangedListener;
    private int damage;
    private int health;
    private Monster monster;
    private float speed;


    public Enemy(Sprite sprite, Monster monster, StateChangedListener stateChangedListener) {
        super(sprite);
        this.monster = monster;
        this.stateChangedListener = stateChangedListener;
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
            this.requestSpriteRemoval();
            this.stateChangedListener.onDeath(true);
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

    public Monster getMonster() {
        return monster;
    }
}
