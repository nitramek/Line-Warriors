package cz.nitramek.linewarriors.game.objects;


import cz.nitramek.linewarriors.game.models.Square;
import cz.nitramek.linewarriors.game.shaders.Shader;

public class Sprite implements Drawable {

    private Square square;

    public Sprite(Square square) {
        this.square = square;
    }


    @Override
    public void draw(Shader shader) {

    }
}
