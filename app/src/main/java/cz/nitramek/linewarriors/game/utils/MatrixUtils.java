package cz.nitramek.linewarriors.game.utils;


public class MatrixUtils {

    /**
     * Compute the dot product of two vectors
     *
     * @param v1 The first vector
     * @param v2 The second vector
     * @return v1 dot v2
     **/
    public static float dot(float[] v1, float[] v2) {
        float res = 0;
        for (int i = 0; i < v1.length; i++)
            res += v1[i] * v2[i];
        return res;
    }

    /**
     * Converts this vector into a normalized (unit length) vector
     * <b>Modifies the input parameter</b>
     *
     * @param vector The vector to normalize
     **/
    public static void normalize(float[] vector) {
        scalarMultiply(vector, 1 / magnitude(vector));
    }

    /**
     * Multiply a vector by a scalar.  <b>Modifies the input vector</b>
     *
     * @param vector The vector
     * @param scalar The scalar
     **/
    public static void scalarMultiply(float[] vector, float scalar) {
        for (int i = 0; i < vector.length; i++)
            vector[i] *= scalar;
    }

    /**
     * Compute the magnitude (length) of a vector
     *
     * @param vector The vector
     * @return The magnitude of the vector
     **/
    public static float magnitude(float[] vector) {
        return (float) Math.sqrt(vector[0] * vector[0] +
                vector[1] * vector[1]);
    }

}
