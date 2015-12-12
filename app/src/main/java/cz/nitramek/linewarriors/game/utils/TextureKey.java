package cz.nitramek.linewarriors.game.utils;


public enum TextureKey {
    BACKGROUND("drawable/background"),
    MAGE("drawable/redmage_m"),
    JENOVA("drawable/jenova2");

    public final String resourcePath;
    TextureKey(String s) {
        this.resourcePath = s;
    }
}
