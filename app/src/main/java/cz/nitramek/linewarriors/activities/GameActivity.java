package cz.nitramek.linewarriors.activities;


import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.Log;

import java.io.IOException;

import cz.nitramek.linewarriors.game.GameView;

public class GameActivity extends Activity {

    private GLSurfaceView mGLSurfaceView;

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
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mGLSurfaceView != null) {
            mGLSurfaceView.onPause();
        }
    }
}
