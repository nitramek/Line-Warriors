package cz.nitramek.linewarriors.game.utils;


import android.graphics.RectF;
import android.opengl.Matrix;

public class ModelMatrix {

    /**
     * this is so I have everything separately
     */
    private float translateMatrix[] = new float[16];
    private float scaleMatrix[] = new float[16];
    private float rotateMatrix[] = new float[16];

    private RectF boundingBox = new RectF();
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

    public synchronized void recount() {
        Matrix.multiplyMM(this.values, 0, this.rotateMatrix, 0, this.translateMatrix, 0);
        Matrix.multiplyMM(this.values, 0, this.values, 0, this.scaleMatrix, 0);
    }

    public void translate(float x, float y) {
        //TODO check boundaries relative to object width/height
        Matrix.translateM(translateMatrix, 0, x, y, 0);
        this.recount();


    }
    public void setPosition(float x, float y){
        Matrix.setIdentityM(this.translateMatrix, 0);
        this.translate(x, y);
    }

    /**
     * Calculates current X relative to scale
     */
    private float getRelativeX() {
        return this.translateMatrix[12] * this.scaleMatrix[0];
    }

    private float getRelativeY() {
        return this.translateMatrix[13] * this.scaleMatrix[5];
    }

    public RectF getBoundingBox() {
        //horni levy X
        this.boundingBox.left = this.values[12] - (0.5f * this.scaleMatrix[0]);
        //horni levy Y
        this.boundingBox.top = this.values[13] + (0.5f * this.scaleMatrix[5]);
        //dolni pravy X
        this.boundingBox.right = this.values[12] + (0.5f * this.scaleMatrix[0]);
        //dolni pravy Y
        this.boundingBox.bottom = this.values[13] - (0.5f * this.scaleMatrix[5]);

        return this.boundingBox;
    }

    //12 x
    //13 y
    public void rotate(float angle, float x, float y) {
        Matrix.rotateM(rotateMatrix, 0, angle, x, y, 0);
        this.recount();
    }

    //0, 5 pro scale
    public void scale(float x, float y) {
        Matrix.scaleM(scaleMatrix, 0, x, y, 0);
        this.recount();

    }


    public synchronized float[] getValues() {
        return values;
    }

    public void translate(Vector direction) {
        this.translate(direction.x(), direction.y());
    }
}
