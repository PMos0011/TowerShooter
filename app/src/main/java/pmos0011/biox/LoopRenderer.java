package pmos0011.biox;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLES31;
import android.opengl.GLSurfaceView;
import android.util.Log;


public class LoopRenderer implements GLSurfaceView.Renderer {

    Context context;

    Shader textureShader;
    VertexObjectsLoader loader = new VertexObjectsLoader();
    Renderer renderer = new Renderer();
    Texture texture;

    public LoopRenderer (Context context){
        this.context = context;


    }

    public void onSurfaceCreated(GL10 unused, EGLConfig config) {
        GLES31.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

        textureShader = new Shader(context, R.raw.texture_vertex_shader, R.raw.texture_fragment_shader, 0, "vPosition");
        texture = loader.loadToVAO(Texture.SQUERE_CORDS, Texture.DRAW_ORDER);

    }

    public void onDrawFrame(GL10 unused) {
        GLES31.glClear(GLES31.GL_COLOR_BUFFER_BIT);

        textureShader.start();
        renderer.draw(texture);
        textureShader.stop();

    }

    public void onSurfaceChanged(GL10 unused, int width, int height) {
        GLES31.glViewport(0, 0, width, height);
    }

}
