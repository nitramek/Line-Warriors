package cz.nitramek.linewarriors.activities;


import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import java.io.IOException;

import cz.nitramek.linewarriors.game.Controller;
import cz.nitramek.linewarriors.game.GameView;
import cz.nitramek.linewarriors.game.GameWorld;

public class GameActivity extends Activity {

    private GameView gameView;
    private Controller controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            gameView = new GameView(this, new GameView.InitiationCallback() {
                @Override
                public void onInitiation(GameView view) {
                    GameActivity.this.controller.init(
                            GameActivity.this.gameView.getWorld().getMainCharacter(),
                            GameActivity.this.gameView.getWidth(),
                            GameActivity.this.gameView.getHeight());
                    GameActivity.this.gameView.setOnTouchListener(GameActivity.this.controller);
                }
            });
        } catch (IOException e) {
            Log.e(GameActivity.class.getName(), "Error in loading shaders", e);
            this.finish();
        }
        GameActivity.this.controller = new Controller(GameActivity.this);
        setContentView(gameView);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (gameView != null) {
            gameView.onResume();
            final GameWorld world = gameView.getWorld();
            if ((world != null)) {
                world.onResume();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (gameView != null) {
            gameView.onPause();
            gameView.getWorld().onPause();
        }
    }
}
