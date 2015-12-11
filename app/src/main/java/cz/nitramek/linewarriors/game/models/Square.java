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
            -0.5f, 0.5f,
            0.5f, -0.5f,
            -0.5f, -0.5f,

            0.5f, -0.5f,
            -0.5f, 0.5f,
            0.5f, 0.5f,
    };
    private final FloatBuffer verticesBuffer;

    private final int bytesPerFloat = 4;

    public Square() {
        this.verticesBuffer =
                ByteBuffer.allocateDirect(vertices.length * bytesPerFloat)
                        .order(ByteOrder.nativeOrder())
                        .asFloatBuffer();
        this.verticesBuffer.put(this.vertices).position(0);
    }


    @Override
    public void draw(final Shader shader) {
        verticesBuffer.position(0);

        final int positionHandle = shader.getAttributeHandle(ShaderConstants.POSITION);
        GLES20.glVertexAttribPointer(positionHandle, 2, GLES20.GL_FLOAT, false, 2 * bytesPerFloat, verticesBuffer);
        GLES20.glEnableVertexAttribArray(positionHandle);
        float[] modelMatrix = new float[16];
        Matrix.setIdentityM(modelMatrix, 0);
        GLES20.glUniformMatrix4fv(shader.getUniformHandle(ShaderConstants.MODEL_MATRIX), 1, false, modelMatrix, 0);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 6);
    }
}
