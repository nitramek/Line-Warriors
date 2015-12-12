package cz.nitramek.linewarriors.game;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.util.Log;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import cz.nitramek.linewarriors.game.objects.Drawable;
import cz.nitramek.linewarriors.game.shaders.Shader;
import cz.nitramek.linewarriors.game.shaders.ShaderException;
import cz.nitramek.linewarriors.game.shaders.ShaderInitiator;

public class GameRenderer implements GLSurfaceView.Renderer {

    private final GameView gameView;
    private List<Drawable> drawables;
    private ShaderInitiator shaderInitiator;
    private Shader shader;
    private float ratio;

    public GameRenderer(ShaderInitiator shaderInitiator, GameView gameView) {
        this.shaderInitiator = shaderInitiator;
        this.gameView = gameView;
        this.drawables = new ArrayList<>();


    }


    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        Log.d(GameRenderer.class.getName(), "SurfaceCreated in renderer");
        try {
            this.shader = shaderInitiator.compileLinkAndGetShader();
            this.shader.useShader();

            GLES20.glEnable(GLES20.GL_BLEND);
            GLES20.glBlendFunc(GLES20.GL_ONE, GLES20.GL_ONE_MINUS_SRC_ALPHA);


        } catch (ShaderException e) {
            Log.e(GameRenderer.class.getName(), "Error in creating shader", e);
        }
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        Log.d(GameRenderer.class.getName(), "onSurfaceChanged");
        GLES20.glViewport(0, 0, width, height);
        this.ratio = (float) width / height;
        this.gameView.rendererInitiated();
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);

        final Iterator<Drawable> iterator = this.drawables.iterator();
        while (iterator.hasNext()) {
            final Drawable drawable = iterator.next();
            if (drawable.shouldBeRemoved()) {
                iterator.remove();
                break;
            }
            drawable.draw(this.shader);

        }
        this.gameView.requestRender();
    }

    public boolean add(Drawable object) {
        return drawables.add(object);
    }

    public float getRatio() {
        return ratio;
    }

    interface OnInitiation {
        void rendererInitiated();
    }
}
