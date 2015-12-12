package cz.nitramek.linewarriors.game.objects;


public class MainCharacter extends Model{

    float speed;

    public MainCharacter(Sprite sprite) {
        super(sprite);
        this.speed = 0.05f;
    }

    @Override
    public float getSpeed() {
        return this.speed;
    }

    public void requestRemoval(){
        this.sprite.setRemove(true);
    }


}
