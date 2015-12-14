package cz.nitramek.linewarriors.game.utils;


public enum TextureKey {
    BACKGROUND("drawable/background"),
    MAGE("drawable/character_redmage_m"),
    JENOVA("drawable/enemy_jenova"),
    EXPLOSION("drawable/spell_fireball"),
    BARRET("drawable/enemy_barret");

    public final String resourcePath;

    TextureKey(String s) {
        this.resourcePath = s;
    }
}
