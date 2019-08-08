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
    Texture mTexture;

    int[] staticBitmapID;
    float[] mProjectionMatrix = new float[16];
    float[] mViewMatrix = new float[16];
    float[] mModelMatrix = new float[16];
    float[] mMVPMatrix = new float[16];
    float[] mBackgroundProjectionMatrix = new float[16];

    float turretAngle = 0;
    public volatile boolean rotateRight=false;
    public volatile boolean rotateLeft=false;



    public GamePlayRenderer(Context context) {
        mContext = context;
    }

    public void onSurfaceCreated(GL10 unused, EGLConfig config) {

        GLES31.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

        //GLES31.glGenTextures(TEXTURE_COUNT, textureHandle, 0);

        staticBitmapID = BitmapID.getStaticBitmapID();

        mTexture = new Texture();
        for (int i = 0; i < staticBitmapID.length; i++)
            mTexture.loadTexture(mContext, staticBitmapID[i]);

    }

    public void onDrawFrame(GL10 unused) {

        GLES31.glClear(GLES31.GL_COLOR_BUFFER_BIT);

        Matrix.setIdentityM(mModelMatrix, 0);
        Matrix.translateM(mModelMatrix, 0, -0, 0, 0.0000001f);
        Matrix.setLookAtM(mViewMatrix, 0, 0.0f, -0.0f, -1.0f, 0.0f, 0.0f, -0.0f, 0.0f, 1.0f, 0.0f);
        Matrix.multiplyMM(mMVPMatrix, 0, mViewMatrix, 0, mModelMatrix, 0);
        Matrix.multiplyMM(mMVPMatrix, 0, mBackgroundProjectionMatrix, 0, mMVPMatrix, 0);
        mTexture.draw(mMVPMatrix, staticBitmapID[0], 1);

        Matrix.setIdentityM(mModelMatrix, 0);
        Matrix.translateM(mModelMatrix, 0, 0.0f, 0.0f, 0.0000001f);
        Matrix.setLookAtM(mViewMatrix, 0, 0.0f, -0.0f, -2.0f, -0.0f, 0.0f, -0.0f, 0.0f, 1.0f, 0.0f);
        Matrix.multiplyMM(mMVPMatrix, 0, mViewMatrix, 0, mModelMatrix, 0);
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mMVPMatrix, 0);
        mTexture.draw(mMVPMatrix, staticBitmapID[1], 1);

        if(rotateLeft)
            turretAngle-=0.2f;
        if(rotateRight)
            turretAngle+=0.2f;

        Matrix.rotateM(mModelMatrix,0,turretAngle,0.0f,0.0f,1.0f);
        Matrix.multiplyMM(mMVPMatrix, 0, mViewMatrix, 0, mModelMatrix, 0);
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mMVPMatrix, 0);

        for (int i=2; i<staticBitmapID.length-1; i++)
            mTexture.draw(mMVPMatrix, staticBitmapID[i], 1);

        Matrix.setIdentityM(mModelMatrix, 0);
        Matrix.translateM(mModelMatrix, 0, 7.5f, -4.0f, 0.0000001f);
        Matrix.setLookAtM(mViewMatrix, 0, -0.0f, 0.0f, -5.0f, -0.0f, 0.0f, -0.0f, 0.0f, 1.0f, 0.0f);
        Matrix.multiplyMM(mMVPMatrix, 0, mViewMatrix, 0, mModelMatrix, 0);
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mMVPMatrix, 0);
        mTexture.draw(mMVPMatrix, staticBitmapID[staticBitmapID.length-1], 1);

        Matrix.setIdentityM(mModelMatrix, 0);
        Matrix.translateM(mModelMatrix, 0, 5.0f, -4.0f, 0.0000001f);
        Matrix.rotateM(mModelMatrix,0,180.0f,0.0f,0.0f,1.0f);
        Matrix.multiplyMM(mMVPMatrix, 0, mViewMatrix, 0, mModelMatrix, 0);
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mMVPMatrix, 0);
        mTexture.draw(mMVPMatrix, staticBitmapID[staticBitmapID.length-1], 1);
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
