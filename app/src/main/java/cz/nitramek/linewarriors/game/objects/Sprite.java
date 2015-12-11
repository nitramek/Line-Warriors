package cz.nitramek.linewarriors.game.objects;


import cz.nitramek.linewarriors.game.models.Square;
import cz.nitramek.linewarriors.game.shaders.Shader;
import cz.nitramek.linewarriors.game.utils.ModelMatrix;

public class Sprite implements Drawable {

    private final int textureId;
    private Square square;

    private ModelMatrix modelMatrix;

    private int spriteX;
    private int spriteY;

    private int maxSpriteX;
    private int maxSpriteY;


    public Sprite(Square square, int maxSpriteX, int maxSpriteY, int textureId) {
        this(square, maxSpriteX, maxSpriteY, textureId, new ModelMatrix());
    }

    public Sprite(Square square, int maxSpriteX, int maxSpriteY, int textureId, ModelMatrix modelMatrix) {
        this.square = square;
        this.maxSpriteX = maxSpriteX;
        this.maxSpriteY = maxSpriteY;
        this.textureId = textureId;
        this.modelMatrix = new ModelMatrix();
    }


    public ModelMatrix getModelMatrix() {
        return modelMatrix;
    }

    @Override
    public void draw(Shader shader) {
        square.draw(shader, this.spriteX, this.spriteY, maxSpriteX, maxSpriteY, textureId, modelMatrix);
    }
}
