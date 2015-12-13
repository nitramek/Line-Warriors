package cz.nitramek.linewarriors.game.objects;


import cz.nitramek.linewarriors.game.utils.OnAbilityCast;
import cz.nitramek.linewarriors.game.utils.SpellManager;

public class Mage extends MainCharacter {


    public Mage(Sprite sprite, OnAbilityCast listener) {
        super(sprite, listener);
    }

    @Override
    public void castFirstAbility() {
        super.cast(SpellManager.SpellType.FIRE);
    }
}
