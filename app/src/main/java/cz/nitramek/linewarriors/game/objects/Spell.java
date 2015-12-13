package cz.nitramek.linewarriors.game.objects;


public abstract class Spell extends Model {
    private int damage;

    public Spell(Sprite sprite, int damage) {
        super(sprite);
        this.damage = damage;
    }


    @Override
    public float getSpeed() {
        return 0;
    }

    @Override
    public boolean collide(Model other) {
        final boolean collide = super.collide(other);
        if (collide && other instanceof Enemy) {
            ((Enemy) other).obtainDamage(this.damage);
        }
        return collide;
    }

    public abstract void animate();

}
