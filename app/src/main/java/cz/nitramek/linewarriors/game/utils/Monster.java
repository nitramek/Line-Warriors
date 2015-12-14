package cz.nitramek.linewarriors.game.utils;


public enum Monster {
    JENOVA(5, 10, 0.005f, 5),
    BARRET(2, 10, 0.010f, 10),
    MAGICPOT(9, 15, 0.050f, 30),
    BAHAMOUT(20, 30, 0.003f, 100);


    public final int damage;
    public final int health;
    public final int reward;
    public float speed;

    Monster(int damage, int health, float speed, int reward) {

        this.damage = damage;
        this.health = health;
        this.speed = speed;
        this.reward = reward;
    }
}
