package cz.nitramek.linewarriors.game;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;
import android.util.Log;
import android.view.SurfaceHolder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cz.nitramek.linewarriors.game.models.Square;
import cz.nitramek.linewarriors.game.objects.Drawable;
import cz.nitramek.linewarriors.game.objects.Sprite;
import cz.nitramek.linewarriors.game.shaders.ShaderConstants;
import cz.nitramek.linewarriors.game.shaders.ShaderInitiator;
import cz.nitramek.linewarriors.util.AssetLoader;


public class GameView extends GLSurfaceView implements GameRenderer.OnInitiation {
    private final GameRenderer renderer;

    private Controller controller;


    private Sprite mainCharacter;


    public GameView(Context context) throws IOException {
        super(context);
        this.setEGLContextClientVersion(2);

        try {
            String vertexShader = AssetLoader.loadText(context.getAssets(), "shaders/normal.vert");
            String fragmentShader = AssetLoader.loadText(context.getAssets(), "shaders/normal.frag");
            List<String> attributes = new ArrayList<>(1);
            attributes.add(ShaderConstants.POSITION);
            attributes.add(ShaderConstants.TEXTURE_UV);

            List<String> uniforms = new ArrayList<>();
            uniforms.add(ShaderConstants.MODEL_MATRIX);
            uniforms.add(ShaderConstants.TEXTURE_UNIT);
            uniforms.add(ShaderConstants.X_SPRITE);
            uniforms.add(ShaderConstants.Y_SPRITE);

            renderer = new GameRenderer(new ShaderInitiator(vertexShader, fragmentShader, attributes, uniforms), this);
            this.setRenderer(renderer);
        } catch (IOException e) {
            Log.e(GameView.class.getName(), "Error in creating/loading shaders", e);
            throw e;
        }
        this.setRenderMode(RENDERMODE_WHEN_DIRTY);
    }

    private static int loadTexture(Context context, String name) {
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        int[] textureId = new int[1];
        GLES20.glGenTextures(1, textureId, 0);
        int id = context.getResources().getIdentifier(name, null, context.getPackageName());
        final BitmapFactory.Options opts = new BitmapFactory.Options();
        Bitmap bmp = BitmapFactory.decodeResource(context.getResources(), id, opts);

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
        return textureId[0];
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        super.surfaceCreated(holder);
        Log.d(GameView.class.getName(), "SurfaceCreated");
    }

    @Override
    public void rendererInitiated() {
        Log.d(GameView.class.getName(), "renderInitiated");
        final Square square = new Square(1);
        Drawable background = new Sprite(square, 1, 1, loadTexture(this.getContext(), "drawable/background"));
        this.renderer.add(background);

        final Square square4 = new Square(4);
        mainCharacter = new Sprite(square4, 4, 4, loadTexture(this.getContext(), "drawable/redmage_m"));

        mainCharacter.getModelMatrix().scale(0.15f, 0.15f * this.renderer.getRatio());
        controller = new Controller(mainCharacter);
        this.setOnTouchListener(controller);


        this.renderer.add(mainCharacter);
    }
}
