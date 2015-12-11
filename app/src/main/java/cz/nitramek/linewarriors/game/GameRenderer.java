package cz.nitramek.linewarriors.game;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import cz.nitramek.linewarriors.game.models.Square;
import cz.nitramek.linewarriors.game.objects.Drawable;
import cz.nitramek.linewarriors.game.shaders.Shader;
import cz.nitramek.linewarriors.game.shaders.ShaderException;
import cz.nitramek.linewarriors.game.shaders.ShaderInitiator;

public class GameRenderer implements GLSurfaceView.Renderer {

    private final Context context;
    private List<Drawable> drawables;
    private ShaderInitiator shaderInitiator;
    private Shader shader;

    public GameRenderer(Context context, ShaderInitiator shaderInitiator) {
        this.context = context;
        this.shaderInitiator = shaderInitiator;
        this.drawables = new ArrayList<>();

    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        try {
            this.shader = shaderInitiator.compileLinkAndGetShader();
            this.shader.useShader();

            GLES20.glEnable(GLES20.GL_BLEND);
            GLES20.glBlendFunc(GLES20.GL_ONE, GLES20.GL_ONE_MINUS_SRC_ALPHA);

            GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
            int[] textureId = new int[1];
            GLES20.glGenTextures(1, textureId, 0);
            int id = this.context.getResources().getIdentifier("drawable/redmage_m", null, context.getPackageName());
            Bitmap bmp = BitmapFactory.decodeResource(context.getResources(), id);

            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId[0]);

            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER,
                    GLES20.GL_LINEAR);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER,
                    GLES20.GL_LINEAR);

            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S,
                    GLES20.GL_CLAMP_TO_EDGE);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T,
                    GLES20.GL_CLAMP_TO_EDGE);


            GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bmp, 0);


            bmp.recycle();
            final Square squ = new Square(new int[]{0});
            drawables.add(squ);

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
        for (Drawable drawable : drawables) {
            drawable.draw(this.shader);
        }
    }
}
