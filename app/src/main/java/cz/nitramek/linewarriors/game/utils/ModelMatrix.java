package cz.nitramek.linewarriors.game.utils;


import android.opengl.Matrix;

public class ModelMatrix {

    /**
     * this is so I have everything separately
     */
    private float translateMatrix[] = new float[16];
    private float scaleMatrix[] = new float[16];
    private float rotateMatrix[] = new float[16];

    /**
     * Holds the real values of model matrix
     */
    private float values[] = new float[16];


    public ModelMatrix() {
        Matrix.setIdentityM(translateMatrix, 0);
        Matrix.setIdentityM(scaleMatrix, 0);
        Matrix.setIdentityM(rotateMatrix, 0);
        Matrix.setIdentityM(values, 0);
    }

    public void recount() {
        Matrix.multiplyMM(this.values, 0, this.rotateMatrix, 0, this.scaleMatrix, 0);
        Matrix.multiplyMM(this.values, 0, this.values, 0, this.translateMatrix, 0);
    }

    public void translate(float x, float y) {
        Matrix.translateM(translateMatrix, 0, x, y, 0);
        this.recount();
    }

    public void rotate(float angle, float x, float y) {
        Matrix.rotateM(rotateMatrix, 0, angle, x, y, 0);
        this.recount();
    }

    public void scale(float x, float y) {
        Matrix.scaleM(scaleMatrix, 0, x, y, 0);
        this.recount();

    }

    public float[] getValues() {
        return values;
    }
}
