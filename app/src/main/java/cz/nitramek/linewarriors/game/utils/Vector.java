package cz.nitramek.linewarriors.game.utils;


public class Vector {


    private float[] values = new float[2];

    public Vector(float x, float y) {
        this.values[0] = x;
        this.values[1] = y;
    }


    public Vector() {
    }

    public void normalize() {
        MatrixUtils.normalize(this.getAsArray());
    }

    private float[] getAsArray() {
        return values;
    }

    public float dot(Vector other) {
        return MatrixUtils.dot(this.getAsArray(), other.getAsArray());
    }


    public float x() {
        return this.values[0];
    }

    public float y() {
        return this.values[1];
    }

    public void x(float x) {
        this.values[0] = x;
    }

    public void y(float y) {
        this.values[1] = y;
    }

    public float angle() {
        return (float) Math.atan2(this.y(), this.x());
    }

    public float angleDegrees() {
        float degrees = (float) Math.toDegrees(Math.atan2(this.y(), this.x()));
        if(this.getQuadrant() > 2){
            degrees += 360;
        }

        return degrees;
    }

    public void multiply(float m) {
        this.values[0] = this.values[0] * m;
        this.values[1] = this.values[1] * m;
    }

    public int getQuadrant(){
        if(this.x() > 0 && this.y() > 0){
            return 1;
        }
        if(this.x() < 0 && this.y() > 0){
            return 2;
        }
        if(this.x() < 0 && this.y() < 0){
            return 3;
        }
        if(this.x() > 0 && this.y() < 0){
            return 4;
        }else{
            return 0;
        }
    }
    public void revert(){
        this.values[0] = -this.values[0];
        this.values[1] = -this.values[1];
    }
}

