package cz.nitramek.linewarriors.game.models;


import android.opengl.GLES20;
import android.opengl.Matrix;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import cz.nitramek.linewarriors.game.objects.Drawable;
import cz.nitramek.linewarriors.game.shaders.Shader;
import cz.nitramek.linewarriors.game.shaders.ShaderConstants;

public class Square implements Drawable {


    private final float[] vertices = {
            -0.5f, 0.5f, //vlevo nahoře
            0.5f, -0.5f, //vpravo dole
            -0.5f, -0.5f, //vlevo dole

            0.5f, -0.5f, //vpravo dole
            -0.5f, 0.5f, //vlevo nahoře
            0.5f, 0.5f, //vpravo nahoře
    };
    private final float[] uvs = {
            0.0f, 1.0f / 4,
            1.0f / 4, 0.0f,
            0.0f, 0.0f,

            1.0f / 4, 0.f,
            0.f, 1.0f / 4,
            1.0f / 4, 1.0f / 4,


    };
    private final FloatBuffer verticesBuffer;

    private final int bytesPerFloat = 4;
    private final int[] textureId;
    private float[] modelMatrix;
    private FloatBuffer uvsBuffer;


    public Square(int[] textureId) {
        this.textureId = textureId;
        this.verticesBuffer =
                ByteBuffer.allocateDirect(vertices.length * bytesPerFloat)
                        .order(ByteOrder.nativeOrder())
                        .asFloatBuffer();
        this.verticesBuffer.put(this.vertices).position(0);
        this.uvsBuffer =
                ByteBuffer.allocateDirect(uvs.length * bytesPerFloat)
                        .order(ByteOrder.nativeOrder())
                        .asFloatBuffer();
        this.uvsBuffer.put(this.uvs).position(0);
        modelMatrix = new float[16];
        Matrix.setIdentityM(modelMatrix, 0);
        Matrix.scaleM(modelMatrix, 0, 0.5f, 0.5f, 1.0f);
    }


    @Override
    public void draw(final Shader shader) {
        verticesBuffer.position(0);

        final int positionHandle = shader.getAttributeHandle(ShaderConstants.POSITION);
        final int textureHandle = shader.getAttributeHandle(ShaderConstants.TEXTURE_UV);
        GLES20.glVertexAttribPointer(positionHandle, 2, GLES20.GL_FLOAT, false, 2 * bytesPerFloat, verticesBuffer);
        GLES20.glEnableVertexAttribArray(positionHandle);

        uvsBuffer.position(0);
        GLES20.glVertexAttribPointer(textureHandle, 2, GLES20.GL_FLOAT, false, 2 * bytesPerFloat, uvsBuffer);
        GLES20.glEnableVertexAttribArray(textureHandle);

//        Matrix.translateM(modelMatrix, 0, 0.005f, 0.005f, 0f);
        GLES20.glUniform1f(shader.getUniformHandle(ShaderConstants.X_SPRITE), 3f * 0.25f);
        GLES20.glUniform1f(shader.getUniformHandle(ShaderConstants.Y_SPRITE), 1f * 0.25f);
        GLES20.glUniformMatrix4fv(shader.getUniformHandle(ShaderConstants.MODEL_MATRIX), 1, false, modelMatrix, 0);
        GLES20.glUniform1i(shader.getUniformHandle(ShaderConstants.TEXTURE_UNIT), 0);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertices.length);
    }
}
