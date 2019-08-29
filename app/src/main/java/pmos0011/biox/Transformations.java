package pmos0011.biox;

import android.opengl.Matrix;

public class Transformations {

    public static final float Z_DIMENSION = -1.0000001f;

    private float ratio;
    private float[] projectionMatrix = new float[16];
    private static float[] modelMatrix = new float[16];

    public Transformations(float ratio) {
        this.ratio = ratio;
        Matrix.frustumM(projectionMatrix, 0, -ratio, ratio, -1.0f, 1.0f, 1.0f, 2.0f);

    }

    public float[] getProjectionMatrix() {
        return projectionMatrix;
    }

    public float[] getModelMatrix() {
        return modelMatrix;
    }

    public float getRatio() {
        return ratio;
    }

    public static float[] setModelTranslation(float wordAngle, float objectAngle, float xPos, float yPos, float xScale, float yScale) {
        Matrix.setIdentityM(modelMatrix, 0);
        Matrix.rotateM(modelMatrix, 0, wordAngle, 0, 0, 1.0f);
        Matrix.translateM(modelMatrix, 0, xPos, yPos, Z_DIMENSION);
        Matrix.rotateM(modelMatrix, 0, objectAngle, 0, 0, 1.0f);
        Matrix.scaleM(modelMatrix, 0, xScale, yScale, 1);

        return modelMatrix;
    }

}
