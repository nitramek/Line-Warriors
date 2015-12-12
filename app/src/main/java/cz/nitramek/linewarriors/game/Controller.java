package cz.nitramek.linewarriors.game;


import android.view.MotionEvent;
import android.view.View;

import cz.nitramek.linewarriors.game.objects.MainCharacter;
import cz.nitramek.linewarriors.game.utils.Vector;

public class Controller implements View.OnTouchListener {
    boolean loaded = false;
    private MainCharacter mainMainCharacter;

    private Vector direction = new Vector();

    public Controller(MainCharacter mainMainCharacter) {
        this.mainMainCharacter = mainMainCharacter;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        this.direction.x(event.getX() - v.getWidth() / 2);
        this.direction.y(v.getHeight() / 2 - event.getY());
        this.direction.normalize();
        this.mainMainCharacter.move(this.direction);
        return true;
    }


}
