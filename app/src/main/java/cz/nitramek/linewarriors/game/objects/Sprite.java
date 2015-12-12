package cz.nitramek.linewarriors.game.objects;


import cz.nitramek.linewarriors.game.shaders.Shader;
import cz.nitramek.linewarriors.game.utils.ModelMatrix;

@SuppressWarnings("SuspiciousNameCombination")
public class Sprite implements Drawable {

    public static final int RIGHT_DIRECTION = 2;
    public static final int LEFT_DIRECTION = 1;
    public static final int DOWN_DIRECTION = 3;
    public static final int UP_DIRECTION = 0;
    private final int textureId;
    private Square square;


    private int spriteX;
    private int spriteY;

    private int maxSpriteX;
    private int maxSpriteY;

    private int changeSprite = 0;
    private int changeSpriteTreshold = 5;

    private ModelMatrix modelMatrix;

    private boolean remove;


    public Sprite(Square square, int maxSpriteX, int maxSpriteY, int textureId) {
        this(square, maxSpriteX, maxSpriteY, textureId, new ModelMatrix());
    }

    public Sprite(Square square, int maxSpriteX, int maxSpriteY, int textureId, ModelMatrix modelMatrix) {
        this.square = square;
        this.maxSpriteX = maxSpriteX;
        this.maxSpriteY = maxSpriteY;
        this.textureId = textureId;
        this.remove = false;
        this.modelMatrix = modelMatrix;
    }


    public ModelMatrix getModelMatrix() {
        return modelMatrix;
    }

    @Override
    public void draw(Shader shader) {
        square.draw(shader, this.spriteX, this.spriteY, maxSpriteX, maxSpriteY, textureId, modelMatrix);
    }

    @Override
    public boolean shouldBeRemoved() {
        return remove;
    }


    public void setRemove(boolean remove) {
        this.remove = remove;
    }

    public void setSpriteY(int spriteY) {
        this.spriteY = spriteY;
    }

    public void incrementX() {
        if (this.changeSprite > this.changeSpriteTreshold) {
            this.spriteX = (this.spriteX + 1) % this.maxSpriteX;
            this.changeSprite = 0;
        }
        this.changeSprite++;
    }
}
