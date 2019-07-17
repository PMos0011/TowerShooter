package pmos0011.biox;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLES31;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.SystemClock;
import android.util.Log;


public class GamePlayRenderer implements GLSurfaceView.Renderer {
    
    Context mContext;
    Texture mBackgroundTexture;
    Texture mMainMenuBacteria;

    int[] staticBitmapID;
    int[] eyeBitmapID;
    int[] mouthBitmapID;
    int[] barrierBitmapID;
    int[] antennalBitmapID;
    float[] mProjectionMatrix = new float[16];
    float[] mViewMatrix = new float[16];
    float[] mModelMatrix = new float[16];
    float[] mMVPMatrix = new float[16];
    float[] mBackgroundProjectionMatrix = new float[16];

    int barrierFrame = 0;
    long last_millis = 0;


    public GamePlayRenderer(Context context) {
        mContext = context;
    }

    public void onSurfaceCreated(GL10 unused, EGLConfig config) {

        GLES31.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

        //GLES31.glGenTextures(TEXTURE_COUNT, textureHandle, 0);

        staticBitmapID = BitmapID.getStaticBitmapID();
        eyeBitmapID = BitmapID.getEyesBitmapID();
        mouthBitmapID = BitmapID.getMouthBitmapID();
        antennalBitmapID = BitmapID.getAntennalBitmapID();
        barrierBitmapID = BitmapID.getBarrierBitmapID();

        mBackgroundTexture = new Texture(1.0f);
        mBackgroundTexture.loadTexture(mContext, staticBitmapID[0]);

        mMainMenuBacteria = new Texture(1.0f);
        for (int i = 1; i < staticBitmapID.length; i++)
            mMainMenuBacteria.loadTexture(mContext, staticBitmapID[i]);

        for (int i : barrierBitmapID)
            mMainMenuBacteria.loadTexture(mContext, i);
        for (int i : eyeBitmapID)
            mMainMenuBacteria.loadTexture(mContext, i);
        for (int i : mouthBitmapID)
            mMainMenuBacteria.loadTexture(mContext, i);
        for (int i : antennalBitmapID)
            mMainMenuBacteria.loadTexture(mContext, i);

    }

    public void onDrawFrame(GL10 unused) {

        GLES31.glClear(GLES31.GL_COLOR_BUFFER_BIT);

        Matrix.setIdentityM(mModelMatrix, 0);
        Matrix.translateM(mModelMatrix, 0, -0, 0, 0.0000001f);
        Matrix.setLookAtM(mViewMatrix, 0, 0.0f, -0.0f, -1.0f, 0.0f, 0.0f, -0.0f, 0.0f, 1.0f, 0.0f);
        Matrix.multiplyMM(mMVPMatrix, 0, mViewMatrix, 0, mModelMatrix, 0);
        Matrix.multiplyMM(mMVPMatrix, 0, mBackgroundProjectionMatrix, 0, mMVPMatrix, 0);
        mBackgroundTexture.draw(mMVPMatrix, staticBitmapID[0], 1);


        Matrix.setIdentityM(mModelMatrix, 0);
        Matrix.translateM(mModelMatrix, 0, 1.0f, 0.5f, 0.0000001f);
        Matrix.setLookAtM(mViewMatrix, 0, 0.0f, -0.0f, -2.3f, -0.0f, 0.0f, -0.0f, 0.0f, 1.0f, 0.0f);
        Matrix.multiplyMM(mMVPMatrix, 0, mViewMatrix, 0, mModelMatrix, 0);
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mMVPMatrix, 0);
        mMainMenuBacteria.draw(mMVPMatrix, staticBitmapID[1], 1);

        long time = SystemClock.uptimeMillis() / 50;
        if (last_millis != time) {
            last_millis = time;
            barrierFrame++;
            if (barrierFrame > 28)
                barrierFrame = 0;
        }

        mMainMenuBacteria.draw(mMVPMatrix, barrierBitmapID[barrierFrame], 0.55f);

        for (int i : eyeBitmapID)
            mMainMenuBacteria.draw(mMVPMatrix, i, 1);
        for (int i : mouthBitmapID)
            mMainMenuBacteria.draw(mMVPMatrix, i, 1);

        mMainMenuBacteria.draw(mMVPMatrix, staticBitmapID[2], 1);

        for (int i : eyeBitmapID)
            mMainMenuBacteria.draw(mMVPMatrix, i, 1);
        for (int i : mouthBitmapID)
            mMainMenuBacteria.draw(mMVPMatrix, i, 1);
        for (int i : antennalBitmapID)
            mMainMenuBacteria.draw(mMVPMatrix, i, 1);

    }

    public void onSurfaceChanged(GL10 unused, int width, int height) {
        GLES31.glViewport(0, 0, width, height);
        Matrix.frustumM(mBackgroundProjectionMatrix, 0, -1, 1, -1.0f, 1.0f, 1.0f, 10.0f);
        final float ratio = (float) width / height;
        Matrix.frustumM(mProjectionMatrix, 0, -ratio, ratio, -1.0f, 1.0f, 1.0f, 10.0f);

    }

    public static int loadShader(int type, String shaderCode) {

        int shader = GLES31.glCreateShader(type);

        GLES31.glShaderSource(shader, shaderCode);
        GLES31.glCompileShader(shader);

        return shader;
    }
}
