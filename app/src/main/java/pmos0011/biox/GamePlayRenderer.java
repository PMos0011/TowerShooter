package pmos0011.biox;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLES31;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.SystemClock;

public class GamePlayRenderer implements GLSurfaceView.Renderer {

    private final float Z_DIMENSION = -1.0000001f;
    public static final float GAME_CONTROL_OBJECT_SIZE = 0.2f;

    Context mContext;
    Texture backgroundTextures;
    Texture towerTextures;
    Texture buttonsTextures;
    Square laserSight;
    Square leftCannonReload;
    Square rightCannonReload;

    GameControlObjects rightArrow;
    GameControlObjects leftArrow;
    GameControlObjects leftCannonButton;
    GameControlObjects rightCannonButton;

    public static float ratio;

    int[] staticBitmapID;
    public static float[] mProjectionMatrix = new float[16];
    float[] mModelMatrix = new float[16];
    float[] mMVPMatrix = new float[16];

    float turretAngle = 0;
    public volatile boolean rotateRight = false;
    public volatile boolean rotateLeft = false;

    public volatile boolean isLeftCannonReloading = true;
    public volatile boolean isRightCannonReloading = true;
    public volatile float leftCannonReloadStatus = 100.0f;
    public volatile float rightCannonReloadStatus = 100.0f;
    long leftCannonLastSecond = 0;
    long rightCannonLastSecond = 0;

    public GamePlayRenderer(Context context) {
        mContext = context;
    }

    public void onSurfaceCreated(GL10 unused, EGLConfig config) {

        GLES31.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

        staticBitmapID = BitmapID.getStaticBitmapID();

        backgroundTextures = new Texture(mContext, 2);
        backgroundTextures.loadTexture(staticBitmapID[BitmapID.textureNames.BACKGROUND.getValue()]);

        towerTextures = new Texture(mContext, 0.5f);
        for (int i = BitmapID.textureNames.TURRET_BASE.getValue(); i <= BitmapID.textureNames.TURRET_TOWER.getValue(); i++)
            towerTextures.loadTexture(staticBitmapID[i]);

        buttonsTextures = new Texture(mContext, GAME_CONTROL_OBJECT_SIZE);
        for (int i = BitmapID.textureNames.LEFT_ARROW.getValue(); i <= BitmapID.textureNames.RIGHT_CANNON_BUTTON.getValue(); i++)
            buttonsTextures.loadTexture(staticBitmapID[i]);

        leftArrow = new GameControlObjects();
        rightArrow = new GameControlObjects();
        leftCannonButton = new GameControlObjects();
        rightCannonButton = new GameControlObjects();

        laserSight = new Square(mContext);
        leftCannonReload = new Square(mContext);
        rightCannonReload = new Square(mContext);
    }

    public void onDrawFrame(GL10 unused) {

        GLES31.glClear(GLES31.GL_COLOR_BUFFER_BIT);

        Matrix.setIdentityM(mModelMatrix, 0);
        Matrix.translateM(mModelMatrix, 0, 0, 0, Z_DIMENSION);
        backgroundTextures.draw(mModelMatrix, staticBitmapID[BitmapID.textureNames.BACKGROUND.getValue()], 1);
        towerTextures.draw(mModelMatrix, staticBitmapID[BitmapID.textureNames.TURRET_BASE.getValue()], 1);
        laserSight.draw(mModelMatrix, turretAngle, true);

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

        for (int i = BitmapID.textureNames.TURRET_L_CANNON.getValue(); i <= BitmapID.textureNames.TURRET_TOWER.getValue(); i++)
            towerTextures.draw(mModelMatrix, staticBitmapID[i], 1);

        Matrix.setIdentityM(mModelMatrix, 0);
        Matrix.translateM(mModelMatrix, 0, leftArrow.xOpenGLPosition, rightArrow.yOpenGLPosition, Z_DIMENSION);
        Matrix.rotateM(mModelMatrix, 0, turretAngle, 0, 0, 1.0f);
        buttonsTextures.draw(mModelMatrix, staticBitmapID[BitmapID.textureNames.LEFT_ARROW.getValue()], 1);

        Matrix.setIdentityM(mModelMatrix, 0);
        Matrix.translateM(mModelMatrix, 0, rightArrow.xOpenGLPosition, rightArrow.yOpenGLPosition, Z_DIMENSION);
        Matrix.rotateM(mModelMatrix, 0, turretAngle, 0, 0, 1.0f);
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mModelMatrix, 0);
        buttonsTextures.draw(mModelMatrix, staticBitmapID[BitmapID.textureNames.RIGHT_ARROW.getValue()], 1);

        Matrix.setIdentityM(mModelMatrix, 0);
        Matrix.translateM(mModelMatrix, 0, leftCannonButton.xOpenGLPosition, leftCannonButton.yOpenGLPosition, Z_DIMENSION);
        buttonsTextures.draw(mModelMatrix, staticBitmapID[BitmapID.textureNames.LEFT_CANNON_BUTTON.getValue()], 1);

        Matrix.setIdentityM(mModelMatrix, 0);
        Matrix.translateM(mModelMatrix, 0, rightCannonButton.xOpenGLPosition, rightCannonButton.yOpenGLPosition, Z_DIMENSION);
        buttonsTextures.draw(mModelMatrix, staticBitmapID[BitmapID.textureNames.RIGHT_CANNON_BUTTON.getValue()], 1);

        if (isLeftCannonReloading) {
            Matrix.setIdentityM(mModelMatrix, 0);
            Matrix.translateM(mModelMatrix, 0, leftCannonButton.xOpenGLPosition, leftCannonButton.yOpenGLPosition, Z_DIMENSION);
            leftCannonReload.draw(mModelMatrix, leftCannonReloadStatus, false);

            long time = SystemClock.uptimeMillis() / 10;
            if (leftCannonLastSecond != time) {
                leftCannonReloadStatus -= 0.2f;
                leftCannonLastSecond = time;
            }

            if (leftCannonReloadStatus <= 0) {
                isLeftCannonReloading = false;
                leftCannonReloadStatus = 100;
            }
        }

        if (isRightCannonReloading) {
            Matrix.setIdentityM(mModelMatrix, 0);
            Matrix.translateM(mModelMatrix, 0, rightCannonButton.xOpenGLPosition, rightCannonButton.yOpenGLPosition, Z_DIMENSION);
            Matrix.rotateM(mModelMatrix, 0, 180, 0, 0, 1.0f);
            rightCannonReload.draw(mModelMatrix, rightCannonReloadStatus, false);

            long time = SystemClock.uptimeMillis() / 10;
            if (rightCannonLastSecond != time) {
                rightCannonReloadStatus -= 0.2f;
                rightCannonLastSecond = time;
            }

            if (rightCannonReloadStatus <= 0) {
                isRightCannonReloading = false;
                rightCannonReloadStatus = 100;
            }
        }
    }

    public void onSurfaceChanged(GL10 unused, int width, int height) {
        GLES31.glViewport(0, 0, width, height);
        ratio = (float) width / height;
        Matrix.frustumM(mProjectionMatrix, 0, -ratio, ratio, -1.0f, 1.0f, 1.0f, 2.0f);

        leftArrow.setObject(width, height, GAME_CONTROL_OBJECT_SIZE, -ratio + 1.5f * GAME_CONTROL_OBJECT_SIZE, -1 + GAME_CONTROL_OBJECT_SIZE);
        rightArrow.setObject(width, height, GAME_CONTROL_OBJECT_SIZE, -ratio + 4.0f * GAME_CONTROL_OBJECT_SIZE, -1 + GAME_CONTROL_OBJECT_SIZE);
        leftCannonButton.setObject(width, height, GAME_CONTROL_OBJECT_SIZE, ratio - 4.0f * GAME_CONTROL_OBJECT_SIZE, -1 + GAME_CONTROL_OBJECT_SIZE);
        rightCannonButton.setObject(width, height, GAME_CONTROL_OBJECT_SIZE, ratio - 1.5f * GAME_CONTROL_OBJECT_SIZE, -1 + GAME_CONTROL_OBJECT_SIZE);

        laserSight.setSquare();
        leftCannonReload.setSquare();
        rightCannonReload.setSquare();
    }

    public static int loadShader(int type, String shaderCode) {

        int shader = GLES31.glCreateShader(type);

        GLES31.glShaderSource(shader, shaderCode);
        GLES31.glCompileShader(shader);

        return shader;
    }
}
