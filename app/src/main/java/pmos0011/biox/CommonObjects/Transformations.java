package pmos0011.biox.CommonObjects;

import android.graphics.PointF;
import android.opengl.Matrix;

public class Transformations {

    public static final float Z_DIMENSION = -1.0000001f;

    private static float ratio;
    private float[] projectionMatrix = new float[16];

    public Transformations(float ratio) {
        this.ratio = ratio;
        Matrix.frustumM(projectionMatrix, 0, -ratio, ratio, -1.0f, 1.0f, 1.0f, 2.0f);
    }

    public float[] getProjectionMatrix() {
        return projectionMatrix;
    }


    public static float getRatio() {
        return ratio;
    }

    public static void setModelTranslation(float[] matrix, float wordAngle, float objectAngle, float xPos, float yPos, float xScale, float yScale) {

        Matrix.setIdentityM(matrix, 0);
        Matrix.rotateM(matrix, 0, wordAngle, 0, 0, 1.0f);
        Matrix.translateM(matrix, 0, xPos, yPos, Z_DIMENSION);
        Matrix.rotateM(matrix, 0, objectAngle, 0, 0, 1.0f);
        Matrix.scaleM(matrix, 0, xScale, yScale, 1);
    }

    public static PointF calculatePoint(float angle, float param) {

        PointF point = new PointF();

        double radians = Math.toRadians(angle);
        point.x = param * (float) Math.sin(radians);
        point.y = param * (float) Math.cos(radians);
        point.x = -point.x;

        return point;
    }

}
