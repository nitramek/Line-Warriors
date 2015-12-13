package cz.nitramek.linewarriors.game.utils;


public enum Monster {
    JENOVA(5, 10, 0.005f),
    BARRET(2, 10, 0.010f);
    public final int damage;
    public final int health;
    public float speed;

    Monster(int damage, int health, float speed) {

        this.damage = damage;
        this.health = health;
        this.speed = speed;
    }
}
