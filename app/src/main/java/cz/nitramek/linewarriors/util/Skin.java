package cz.nitramek.linewarriors.util;


import cz.nitramek.linewarriors.R;
import cz.nitramek.linewarriors.game.utils.TextureKey;

public enum Skin {
    MAGE_MALE(R.drawable.character_redmage_m_one, TextureKey.MAGE),
    MAGE_FEMALE(R.drawable.character_redmage_f_one, TextureKey.MAGE_FEMALE);

    public static final String SKIN_TAG = "skin";
    public final int headDrawableId;
    public final TextureKey textureKey;


    Skin(int headDrawableId, TextureKey textureKey) {
        this.headDrawableId = headDrawableId;

        this.textureKey = textureKey;
    }
}
