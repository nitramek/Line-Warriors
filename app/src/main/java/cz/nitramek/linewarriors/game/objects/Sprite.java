package cz.nitramek.linewarriors.game.objects;


import cz.nitramek.linewarriors.game.models.Square;
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

    private ModelMatrix modelMatrix;

    private int spriteX;
    private int spriteY;

    private int maxSpriteX;
    private int maxSpriteY;
    private float speed;


    public Sprite(Square square, int maxSpriteX, int maxSpriteY, int textureId) {
        this(square, maxSpriteX, maxSpriteY, textureId, new ModelMatrix());
    }

    public Sprite(Square square, int maxSpriteX, int maxSpriteY, int textureId, ModelMatrix modelMatrix) {
        this.square = square;
        this.maxSpriteX = maxSpriteX;
        this.maxSpriteY = maxSpriteY;
        this.textureId = textureId;
        this.modelMatrix = new ModelMatrix();
        speed = 0.05f;
    }


    public ModelMatrix getModelMatrix() {
        return modelMatrix;
    }

    @Override
    public void draw(Shader shader) {
        square.draw(shader, this.spriteX, this.spriteY, maxSpriteX, maxSpriteY, textureId, modelMatrix);
    }

    public void moveUp() {
        this.modelMatrix.translate(0.f, this.speed);
        //sprite is situated so first line is facing upwards
        this.spriteY = UP_DIRECTION;
        this.spriteX = (this.spriteX + 1) % this.maxSpriteX;
    }

    public void moveDown() {
        this.modelMatrix.translate(0.f, -this.speed);
        this.spriteY = DOWN_DIRECTION;
        this.spriteX = (this.spriteX + 1) % this.maxSpriteX;
    }

    public void moveLeft() {
        this.modelMatrix.translate(-this.speed, 0);
        this.spriteY = LEFT_DIRECTION;
        this.spriteX = (this.spriteX + 1) % this.maxSpriteX;
    }

    public void moveRight() {
        this.modelMatrix.translate(this.speed, 0);
        this.spriteY = RIGHT_DIRECTION;
        this.spriteX = (this.spriteX + 1) % this.maxSpriteX;
    }

    /**
     * Moves character to given direction
     *
     * @param x should be 1,-1 or 0
     * @param y should be 1,-1 or 0
     */
    public void move(int x, int y) {
        this.modelMatrix.translate(this.speed * x, this.speed * y);
        if (x > 0) {
            this.spriteY = RIGHT_DIRECTION;
        }
        if (x < 0) {
            this.spriteY = LEFT_DIRECTION;
        }
        if (y > 0) {
            this.spriteY = UP_DIRECTION;
        }
        if (y < 0) {
            this.spriteY = DOWN_DIRECTION;
        }
        this.spriteX = (this.spriteX + 1) % this.maxSpriteX;
    }
}
