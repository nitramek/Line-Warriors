package cz.nitramek.linewarriors.game;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.Log;
import android.view.SurfaceHolder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cz.nitramek.linewarriors.game.objects.Drawable;
import cz.nitramek.linewarriors.game.objects.Sprite;
import cz.nitramek.linewarriors.game.objects.Square;
import cz.nitramek.linewarriors.game.shaders.ShaderConstants;
import cz.nitramek.linewarriors.game.shaders.ShaderInitiator;
import cz.nitramek.linewarriors.game.utils.TextureKey;
import cz.nitramek.linewarriors.game.utils.TextureManager;
import cz.nitramek.linewarriors.util.AssetLoader;


public class GameView extends GLSurfaceView {
    private final GameRenderer renderer;

    private Controller controller;


    private Sprite mainCharacter;
    private GameWorld world;


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


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        super.surfaceCreated(holder);
        Log.d(GameView.class.getName(), "SurfaceCreated");
    }

    public void rendererInitiated() {
        Log.d(GameView.class.getName(), "renderInitiated");


        final TextureManager textureManager = TextureManager.getInstance();
        textureManager.setContext(this.getContext());

        Drawable background = new Sprite(new Square(1), 1, 1, textureManager.getTextureId(TextureKey.BACKGROUND));
        this.renderer.add(background);


        world = new GameWorld(this.renderer);
        controller = new Controller(this.world.getMainCharacter());
        this.setOnTouchListener(controller);
    }
}
