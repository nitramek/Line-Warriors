package cz.nitramek.linewarriors.game;

import cz.nitramek.linewarriors.game.objects.Enemy;
import cz.nitramek.linewarriors.game.objects.MainCharacter;
import cz.nitramek.linewarriors.game.objects.Sprite;
import cz.nitramek.linewarriors.game.objects.Square;
import cz.nitramek.linewarriors.game.utils.GameRendererListener;
import cz.nitramek.linewarriors.game.utils.TextureKey;
import cz.nitramek.linewarriors.game.utils.TextureManager;

/**
 * Must be instantiated
 */
public class GameWorld {
    private final GameRendererListener listener;
    /**
     * Reprezents enemies in line, one enemy per given width
     */
    private Enemy[] enemies;
    private MainCharacter mainCharacter;

    /**
     * Reprezents square with 1/4 UV mapping
     */
    private Square square4;

    public GameWorld(final GameRendererListener listener) {
        this.listener = listener;
        this.enemies = new Enemy[10];
        this.square4 = new Square(4);
        final Sprite mainCharacterSprite = new Sprite(square4, 4, 4, TextureManager.getInstance().getTextureId(TextureKey.MAGE));
        mainCharacterSprite.getModelMatrix().scale(0.15f, 0.15f * this.listener.getRatio());
        this.mainCharacter = new MainCharacter(mainCharacterSprite);
        this.listener.addDrawable(mainCharacterSprite);


    }


    public MainCharacter getMainCharacter() {
        return mainCharacter;
    }

    public void setMainCharacter(MainCharacter mainCharacter) {
        this.mainCharacter = mainCharacter;
    }
}
