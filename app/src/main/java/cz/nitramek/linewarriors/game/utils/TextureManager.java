package cz.nitramek.linewarriors.game.utils;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;

import java.util.EnumMap;
import java.util.Map;

public class TextureManager {


    private static TextureManager instance;
    private Map<TextureKey, Integer> textures;
    private Context context;

    private TextureManager() {
        this.textures = new EnumMap<>(TextureKey.class);
    }

    public static TextureManager getInstance() {
        if (instance == null) {
            instance = new TextureManager();
        }
        return instance;
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

    public void setContext(Context context) {
        this.context = context;
    }

    public void addTexture(TextureKey key, int textureId) {
        this.textures.put(key, textureId);
    }

    public int getTextureId(TextureKey key) {
        return this.textures.get(key);
    }

    public void addTexture(TextureKey key, String name) {
        this.textures.put(key, TextureManager.loadTexture(this.context, name));
    }

    public void freeTextures() {
        int[] textureIds = new int[this.textures.size()];
        int i = 0;
        for (Integer value : this.textures.values()) {
            textureIds[i] = value;
            i++;
        }
        GLES20.glDeleteTextures(this.textures.size(), textureIds, 0);
    }


}
