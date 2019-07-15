package pmos0011.biox;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLES31;
import android.opengl.GLSurfaceView;


public class GamePlayRenderer implements GLSurfaceView.Renderer {

    Context mContext;
    Texture mTexture;

    public GamePlayRenderer(Context context){
        mContext=context;
    }

    public void onSurfaceCreated(GL10 unused, EGLConfig config) {
        // Set the background frame color
        GLES31.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

        mTexture = new Texture();
        mTexture.loadTexture(mContext,R.drawable.background_red);

    }

    public void onDrawFrame(GL10 unused) {
        // Redraw background color
        GLES31.glClear(GLES31.GL_COLOR_BUFFER_BIT);
        mTexture.draw();
    }

    public void onSurfaceChanged(GL10 unused, int width, int height) {
        GLES31.glViewport(0, 0, width, height);
    }

    public static int loadShader(int type, String shaderCode){

        int shader = GLES31.glCreateShader(type);

        GLES31.glShaderSource(shader, shaderCode);
        GLES31.glCompileShader(shader);

        return shader;
    }
}
