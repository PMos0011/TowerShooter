package pmos0011.TowerShooter;

import android.graphics.Point;
import android.graphics.PointF;
import android.opengl.Matrix;

public class GameControlObjects {
    private Point position;
    private int halfDimension;
    private PointF openGLPosition;

    public void setObject(int width, int height, float size_mod, float xPosition, float yPosition, float[] projectionMatrix) {

        halfDimension = (int) (height * size_mod) / 2;
        this.openGLPosition = new PointF(xPosition, yPosition);

        float[] mModelMatrix = new float[16];

        Matrix.setIdentityM(mModelMatrix, 0);
        Matrix.translateM(mModelMatrix, 0, xPosition, yPosition, 1.0f);
        Matrix.multiplyMM(mModelMatrix, 0, projectionMatrix, 0, mModelMatrix, 0);

        position = new Point();
        int halfHeight = height / 2;
        int halfWidth = width / 2;

        float tmpPosition = halfHeight * mModelMatrix[13];
        tmpPosition = halfHeight - tmpPosition;
        position.y = (int) tmpPosition;

        tmpPosition = halfWidth * mModelMatrix[12];
        tmpPosition = halfWidth + tmpPosition;
        position.x = (int) tmpPosition;

    }

    public Point getPosition() {
        return position;
    }

    public PointF getOpenGLPosition() {
        return openGLPosition;
    }

    public int getHalfDimension() {
        return halfDimension;
    }
}
