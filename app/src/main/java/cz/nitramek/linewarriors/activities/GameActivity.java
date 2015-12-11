package cz.nitramek.linewarriors.activities;


import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cz.nitramek.linewarriors.game.GameRenderer;
import cz.nitramek.linewarriors.game.shaders.ShaderInitiator;
import cz.nitramek.linewarriors.util.AssetLoader;

public class GameActivity extends Activity {

    private GLSurfaceView mGLSurfaceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mGLSurfaceView = new GLSurfaceView(this);

        final ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        final ConfigurationInfo configurationInfo = activityManager.getDeviceConfigurationInfo();
        if (configurationInfo.reqGlEsVersion >= 0x20000) {
            mGLSurfaceView.setEGLContextClientVersion(2);
            try {
                String vertexShader = AssetLoader.loadText(super.getAssets(), "shaders/normal.vert");
                String fragmentShader = AssetLoader.loadText(super.getAssets(), "shaders/normal.frag");
                List<String> attributes = new ArrayList<>(1);
                attributes.add("position");

                List<String> uniforms = new ArrayList<>(0);

                mGLSurfaceView.setRenderer(new GameRenderer(new ShaderInitiator(vertexShader, fragmentShader, attributes, uniforms)));
            } catch (IOException e) {
                Log.e(GameActivity.class.getName(), "Error in creating/loading shaders", e);
            }


        } else {
            finish();
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
