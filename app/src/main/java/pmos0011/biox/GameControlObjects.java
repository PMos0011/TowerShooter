package pmos0011.biox;

import android.graphics.Point;
import android.opengl.Matrix;
import android.util.Log;

public class GameControlObjects {
    public Point position;
    public int halfDimension;
    public float xOpenGLPosition;
    public float yOpenGLPosition;

    public GameControlObjects() {
    }

    public void setObject(int width, int height, float[] mProjectionMatrix, float size_mod, float xPosition, float yPosition){

        halfDimension=(int)(height*size_mod)/2;
        xOpenGLPosition=xPosition;
        yOpenGLPosition=yPosition;

        float[] mModelMatrix = new float[16];

        Matrix.setIdentityM(mModelMatrix, 0);
        Matrix.translateM(mModelMatrix, 0, xOpenGLPosition, yOpenGLPosition, 1.0f);
        Matrix.multiplyMM(mModelMatrix, 0, mProjectionMatrix, 0, mModelMatrix, 0);

        position=new Point();
        int halfHeight=height/2;
        int halfWidth=width/2;

        float tmpPosition = halfHeight*mModelMatrix[13];
        tmpPosition=halfHeight-tmpPosition;
        position.y=(int)tmpPosition;

        tmpPosition = halfWidth*mModelMatrix[12];
        tmpPosition = halfWidth+tmpPosition;
        position.x=(int)tmpPosition;

    }
}
