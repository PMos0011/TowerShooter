package pmos0011.biox;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLES31;
import android.opengl.GLSurfaceView;
import android.util.Log;


public class GameLoop implements GLSurfaceView.Renderer {

    private Context context;

    private Renderer renderer;
    private GameObjectsLoader loader;
    private TextureShader textureShader;
    private TextureModel textureModel;
    private Transformations transformations;

    public GameLoop(Context context) {
        this.context = context;
    }

    public void onSurfaceCreated(GL10 unused, EGLConfig config) {
        GLES31.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

        renderer = new Renderer();
        loader = new GameObjectsLoader(context, BitmapID.getStaticBitmapID());
        textureShader = new TextureShader(context, R.raw.texture_vertex_shader, R.raw.texture_fragment_shader);
        textureModel = loader.loadToVAO(TextureModel.SQUERE_CORDS, TextureModel.TEXTURE_COORDS, TextureModel.DRAW_ORDER);
    }

    public void onDrawFrame(GL10 unused) {
        GLES31.glClear(GLES31.GL_COLOR_BUFFER_BIT);

        textureShader.start();
        renderer.draw(transformations, loader);
        textureShader.stop();
    }

    public void onSurfaceChanged(GL10 unused, int width, int height) {
        GLES31.glViewport(0, 0, width, height);
        transformations = new Transformations(textureModel,(float)width/height);

        loader.addProjectionMatrixAttribute(textureModel.getVao(),transformations.getModelMatrix());
        loader.addUniformBlockBuffer(0, transformations.getProjectionMatrix());

    }

}
