package cz.nitramek.linewarriors.game;


import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import cz.nitramek.linewarriors.game.objects.Sprite;

public class Controller implements View.OnTouchListener {
    boolean loaded = false;
    private Sprite mainCharacter;

    private float widthMinTreshold;
    private float widthMaxTreshold;

    private float heightMinTreshold;
    private float heightMaxTreshold;


    //TODO make character move on long hold
    //TODO okraje pohyb do boku

    private ControllerLoop controllerLoop;

    public Controller(Sprite mainCharacter) {
        this.mainCharacter = mainCharacter;
        this.controllerLoop = new ControllerLoop(0, 0);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        if (!loaded) {
            final float thirdWidth = v.getWidth() / 3;
            final float thirdHeight = v.getHeight() / 3;
            this.heightMinTreshold = thirdHeight;
            this.heightMaxTreshold = thirdHeight * 2;
            this.widthMinTreshold = thirdWidth;
            this.widthMaxTreshold = thirdWidth * 2;
            loaded = true;
        }
        if (event.getY() < heightMinTreshold) {
            mainCharacter.moveUp();
            return true;
        } else if (event.getY() > heightMaxTreshold) {
            mainCharacter.moveDown();
            return true;
        } else if (event.getX() < widthMinTreshold) {
            mainCharacter.moveLeft();
            return true;

        } else if (event.getX() > widthMaxTreshold) {
            mainCharacter.moveRight();
            return true;
        }

        return false;
    }


    class ControllerLoop extends Thread {
        int diffx;
        int diffy;
        boolean running;

        public ControllerLoop(int diffX, int diffY) {
            this.diffx = diffX;
            this.diffy = diffY;
        }

        @Override
        public void run() {
            while (running) {
                Controller.this.mainCharacter.move(diffx, diffy);
            }
        }

        public void tryJoin() {
            this.running = false;
            try {
                this.join();
            } catch (InterruptedException e) {
                Log.e(ControllerLoop.class.getName(), "exception in joining thread", e);
            }
        }
    }
}
