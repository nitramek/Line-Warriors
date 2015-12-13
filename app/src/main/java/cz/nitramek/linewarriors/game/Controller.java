package cz.nitramek.linewarriors.game;


import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import cz.nitramek.linewarriors.game.objects.MainCharacter;
import cz.nitramek.linewarriors.game.utils.Vector;

public class Controller extends GestureDetector.SimpleOnGestureListener implements View.OnTouchListener {
    boolean loaded = false;
    private int width, height;
    private float halfWidth, halfHeight;
    private MainCharacter mainMainCharacter;
    private Vector direction = new Vector();
    private GestureDetector gestureDetector;


    public Controller(Context context) {
        this.gestureDetector = new GestureDetector(context, this);
    }

    public void init(MainCharacter mainMainCharacter, int width, int height) {
        this.mainMainCharacter = mainMainCharacter;
        this.width = width;
        this.height = height;
        this.halfHeight = height / 2;
        this.halfWidth = width / 2;
    }

    @Override
    public boolean onTouch(View v, MotionEvent e) {
        this.direction.x(e.getX() - halfWidth);
        this.direction.y(halfHeight / 2 - e.getY());
        this.direction.normalize();
        this.mainMainCharacter.move(this.direction);
        return this.gestureDetector.onTouchEvent(e);
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        return super.onSingleTapConfirmed(e);
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        this.mainMainCharacter.castFirstAbility();
        return true;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return true;
    }
}
