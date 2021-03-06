package cz.nitramek.linewarriors.game.objects;


import cz.nitramek.linewarriors.game.StateChangedListener;
import cz.nitramek.linewarriors.game.utils.OnAbilityCast;
import cz.nitramek.linewarriors.game.utils.SpellManager;

public class Mage extends MainCharacter {


    public Mage(Sprite sprite, OnAbilityCast listener, StateChangedListener stateChangedListener) {
        super(sprite, listener, stateChangedListener);
    }

    @Override
    public void castFirstAbility() {
        super.cast(SpellManager.SpellType.FIRE);
    }
}
