package cz.nitramek.linewarriors.activities;


import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import java.io.IOException;

import cz.nitramek.linewarriors.game.GameView;
import cz.nitramek.linewarriors.game.GameWorld;

public class GameActivity extends Activity {

    private GameView mGLSurfaceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            mGLSurfaceView = new GameView(this);
        } catch (IOException e) {
            Log.e(GameActivity.class.getName(), "Error in loading shaders", e);
            this.finish();
        }
        setContentView(mGLSurfaceView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mGLSurfaceView != null) {
            mGLSurfaceView.onResume();
            final GameWorld world = mGLSurfaceView.getWorld();
            if((world != null)) {
                world.onResume();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mGLSurfaceView != null) {
            mGLSurfaceView.onPause();
            mGLSurfaceView.getWorld().onPause();
        }
    }
}
