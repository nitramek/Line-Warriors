package cz.nitramek.linewarriors.game.objects;


import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import cz.nitramek.linewarriors.game.shaders.Shader;
import cz.nitramek.linewarriors.game.shaders.ShaderConstants;
import cz.nitramek.linewarriors.game.utils.ModelMatrix;

public class Square {


    private final float[] vertices = {
            -1f, 1f, //vlevo nahoře
            1f, -1f, //vpravo dole
            -1f, -1f, //vlevo dole

            1f, -1f, //vpravo dole
            -1f, 1f, //vlevo nahoře
            1f, 1f, //vpravo nahoře
    };
    private final int bytesPerFloat = 4;
    private FloatBuffer verticesBuffer;
    private FloatBuffer uvsBuffer;
    private float[] uvs;


    public Square(int spriteCount) {
        this.init();
        recountUvs(spriteCount);
    }

    public Square(int spriteCountX, int spriteCountY) {
        this.init();
        recountUvs(spriteCountX, spriteCountY);
    }

    private void init() {
        this.verticesBuffer =
                ByteBuffer.allocateDirect(vertices.length * bytesPerFloat)
                        .order(ByteOrder.nativeOrder())
                        .asFloatBuffer();
        this.verticesBuffer.put(this.vertices).position(0);
    }

    private void recountUvs(int spriteCountX, int spriteCountY) {
        uvs = new float[]{
                0.0f, 1.0f / spriteCountY,
                1.0f / spriteCountX, 0.0f,
                0.0f, 0.0f,

                1.0f / spriteCountX, 0.f,
                0.f, 1.0f / spriteCountY,
                1.0f / spriteCountX, 1.0f / spriteCountY,
        };
        this.bufferUVs();
    }

    private void recountUvs(int spriteCount) {
        this.recountUvs(spriteCount, spriteCount);
    }

    private void bufferUVs() {
        this.uvsBuffer =
                ByteBuffer.allocateDirect(uvs.length * bytesPerFloat)
                        .order(ByteOrder.nativeOrder())
                        .asFloatBuffer();
        this.uvsBuffer.put(uvs).position(0);
    }

    public void draw(final Shader shader,
                     final int spriteX, final int spriteY,
                     final int spriteXCount, final int spriteYCount,
                     final int textureId, ModelMatrix modelMatrix) {
        verticesBuffer.position(0);

        final int positionHandle = shader.getAttributeHandle(ShaderConstants.POSITION);
        final int textureHandle = shader.getAttributeHandle(ShaderConstants.TEXTURE_UV);


        GLES20.glVertexAttribPointer(positionHandle, 2, GLES20.GL_FLOAT, false, 2 * bytesPerFloat, verticesBuffer);
        GLES20.glEnableVertexAttribArray(positionHandle);

        uvsBuffer.position(0);
        GLES20.glVertexAttribPointer(textureHandle, 2, GLES20.GL_FLOAT, false, 2 * bytesPerFloat, uvsBuffer);
        GLES20.glEnableVertexAttribArray(textureHandle);


        GLES20.glUniform1f(shader.getUniformHandle(ShaderConstants.X_SPRITE), spriteX * 1.0f / spriteXCount);
        GLES20.glUniform1f(shader.getUniformHandle(ShaderConstants.Y_SPRITE), spriteY * 1.0f / spriteYCount);
        GLES20.glUniformMatrix4fv(shader.getUniformHandle(ShaderConstants.MODEL_MATRIX), 1, false, modelMatrix.getValues(), 0);
        GLES20.glUniform1i(shader.getUniformHandle(ShaderConstants.TEXTURE_UNIT), 0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertices.length);
    }
}
