package cz.nitramek.linewarriors.game;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.util.Log;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import cz.nitramek.linewarriors.game.models.Square;
import cz.nitramek.linewarriors.game.shaders.Shader;
import cz.nitramek.linewarriors.game.shaders.ShaderException;
import cz.nitramek.linewarriors.game.shaders.ShaderInitiator;

public class GameRenderer implements GLSurfaceView.Renderer {

    Square square = new Square();
    private ShaderInitiator shaderInitiator;
    private Shader shader;

    private float[] projectionMatrix = new float[16];

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
        GLES20.glViewport(0, 0, width, height);

        float ratio = (float) width / height;

    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);
        square.draw(this.shader);
    }
}
