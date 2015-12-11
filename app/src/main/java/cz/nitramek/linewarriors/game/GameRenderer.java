package cz.nitramek.linewarriors.game;

import android.opengl.GLSurfaceView;
import android.util.Log;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import cz.nitramek.linewarriors.game.shaders.Shader;
import cz.nitramek.linewarriors.game.shaders.ShaderException;
import cz.nitramek.linewarriors.game.shaders.ShaderInitiator;

public class GameRenderer implements GLSurfaceView.Renderer {

    private ShaderInitiator shaderInitiator;
    private Shader shader;


    public GameRenderer(ShaderInitiator shaderInitiator) {
        this.shaderInitiator = shaderInitiator;
    }


    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        try {
            this.shader = shaderInitiator.compileLinkAndGetShader();
            this.shader.useShader();
        } catch (ShaderException e) {
            Log.e(GameRenderer.class.getName(), "Error in creating shader", e);
        }
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {

    }

    @Override
    public void onDrawFrame(GL10 gl) {
    }
}
