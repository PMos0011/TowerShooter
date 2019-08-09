package pmos0011.biox;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.nfc.Tag;
import android.opengl.GLES31;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.SystemClock;
import android.util.Log;


public class GamePlayRenderer implements GLSurfaceView.Renderer {

    private final float Z_DIMENSION = -1.0000001f;
    private final float GAME_CONTROL_OBJECT_SIZE = 0.25f;

    Context mContext;
    Texture backgroundTextures;
    Texture towerTextures;
    Texture buttonsTextures;
    Square laserSight;

    GameControlObjects rightArrow;
    GameControlObjects leftArrow;

    float ratio;

    int[] staticBitmapID;
    float[] mProjectionMatrix = new float[16];
    float[] mModelMatrix = new float[16];
    float[] mMVPMatrix = new float[16];
    float[] mBackgroundProjectionMatrix = new float[16];

    float turretAngle = 0;
    public volatile boolean rotateRight = false;
    public volatile boolean rotateLeft = false;

    public GamePlayRenderer(Context context) {
        mContext = context;
    }

    public void onSurfaceCreated(GL10 unused, EGLConfig config) {

        GLES31.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

        //GLES31.glGenTextures(TEXTURE_COUNT, textureHandle, 0);

        staticBitmapID = BitmapID.getStaticBitmapID();

        backgroundTextures = new Texture(1.0f);
        backgroundTextures.loadTexture(mContext, staticBitmapID[0]);

        towerTextures = new Texture(0.6f);
        for (int i = 1; i < staticBitmapID.length - 1; i++)
            towerTextures.loadTexture(mContext, staticBitmapID[i]);

        buttonsTextures = new Texture(GAME_CONTROL_OBJECT_SIZE);
        buttonsTextures.loadTexture(mContext, staticBitmapID[staticBitmapID.length - 1]);

        leftArrow = new GameControlObjects();
        rightArrow = new GameControlObjects();

        laserSight = new Square();

    }

    public void onDrawFrame(GL10 unused) {

        GLES31.glClear(GLES31.GL_COLOR_BUFFER_BIT);

        Matrix.setIdentityM(mModelMatrix, 0);
        Matrix.translateM(mModelMatrix, 0, 0, 0, Z_DIMENSION);
        backgroundTextures.draw(mModelMatrix, staticBitmapID[0], 1);
        towerTextures.draw(mModelMatrix, staticBitmapID[1], 1);
        laserSight.draw(mModelMatrix,turretAngle);

        if (rotateLeft) {
            turretAngle += 0.2f;
            if (turretAngle > 360)
                turretAngle = 0.2f;
        }
        if (rotateRight) {
            turretAngle -= 0.2f;
            if (turretAngle < 0)
                turretAngle = 359.8f;
        }

        Matrix.rotateM(mModelMatrix, 0, turretAngle, 0, 0, 1.0f);
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mModelMatrix, 0);

        for (int i = 2; i < staticBitmapID.length - 1; i++)
            towerTextures.draw(mModelMatrix, staticBitmapID[i], 1);

        Matrix.setIdentityM(mModelMatrix, 0);
        Matrix.translateM(mModelMatrix, 0, -ratio + GAME_CONTROL_OBJECT_SIZE, -1 + GAME_CONTROL_OBJECT_SIZE, Z_DIMENSION);
        Matrix.rotateM(mModelMatrix, 0, 180.0f, 0, 0, 1.0f);
        buttonsTextures.draw(mModelMatrix, staticBitmapID[staticBitmapID.length - 1], 1);

        Matrix.setIdentityM(mModelMatrix, 0);
        Matrix.translateM(mModelMatrix, 0, -ratio + 3 * GAME_CONTROL_OBJECT_SIZE, -1 + GAME_CONTROL_OBJECT_SIZE, Z_DIMENSION);
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mModelMatrix, 0);
        buttonsTextures.draw(mModelMatrix, staticBitmapID[staticBitmapID.length - 1], 1);


    }

    public void onSurfaceChanged(GL10 unused, int width, int height) {
        GLES31.glViewport(0, 0, width, height);
        Matrix.frustumM(mBackgroundProjectionMatrix, 0, -1.0f, 1.0f, -1.0f, 1.0f, 1.0f, 2.0f);
        ratio = (float) width / height;
        Matrix.frustumM(mProjectionMatrix, 0, -ratio, ratio, -1.0f, 1.0f, 1.0f, 2.0f);

        backgroundTextures.setmProjectionMatrix(mBackgroundProjectionMatrix);
        towerTextures.setmProjectionMatrix(mProjectionMatrix);
        buttonsTextures.setmProjectionMatrix(mProjectionMatrix);

        leftArrow.setObject(width, height, mProjectionMatrix, 0.2f, -ratio + GAME_CONTROL_OBJECT_SIZE, -1 + GAME_CONTROL_OBJECT_SIZE);
        rightArrow.setObject(width, height, mProjectionMatrix, 0.2f, -ratio + 3 * GAME_CONTROL_OBJECT_SIZE, -1 + GAME_CONTROL_OBJECT_SIZE);

        laserSight.setSquare(mProjectionMatrix, ratio);

    }

    public static int loadShader(int type, String shaderCode) {

        int shader = GLES31.glCreateShader(type);

        GLES31.glShaderSource(shader, shaderCode);
        GLES31.glCompileShader(shader);

        return shader;
    }


}
