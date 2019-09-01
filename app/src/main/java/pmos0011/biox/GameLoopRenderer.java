package pmos0011.biox;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLES31;
import android.opengl.GLSurfaceView;

import java.util.Random;

import pmos0011.biox.AbstractClasses.ParticleEffects;
import pmos0011.biox.AbstractClasses.StaticModel;
import pmos0011.biox.CommonObjects.BitmapID;
import pmos0011.biox.CommonObjects.ObjectsLoader;
import pmos0011.biox.CommonObjects.Transformations;
import pmos0011.biox.ParticleEffect.ParticleModel;
import pmos0011.biox.ParticleEffect.ParticleShader;
import pmos0011.biox.StaticTextures.StaticShader;
import pmos0011.biox.StaticTextures.StaticTextures;


public class GameLoopRenderer implements GLSurfaceView.Renderer {

    public static final float WIND_FLOW_X = new Random().nextFloat() / 1000f;
    public static final float WIND_FLOW_Y = new Random().nextFloat() / 1000f;

    private Context context;

    private ObjectsLoader loader;
    private StaticShader staticShader;
    private ParticleShader particleShader;
    private StaticTextures staticTextures;
    private ParticleModel particleModel;
    private Transformations textureTransformations;


    public GameLoopRenderer(Context context) {
        this.context = context;
    }

    public void onSurfaceCreated(GL10 unused, EGLConfig config) {
        GLES31.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

        loader = new ObjectsLoader(context, BitmapID.getStaticBitmapID());
        staticShader = new StaticShader(context, R.raw.texture_vertex_shader, R.raw.texture_fragment_shader);
        particleShader = new ParticleShader(context, R.raw.particle_vertex_shader, R.raw.particle_framgent_shader);
        staticTextures = loader.loadToVAO(StaticModel.SQUERE_CORDS, StaticModel.COORDS_PER_VERTEX, StaticModel.TEXTURE_COORDS, StaticModel.DRAW_ORDER);
        particleModel = loader.loadTOVAO(StaticModel.SQUERE_CORDS, StaticModel.COORDS_PER_VERTEX, StaticModel.TEXTURE_COORDS, StaticModel.DRAW_ORDER, ParticleEffects.PARTICLE_MAX_COUNT);
        staticTextures.setStaticShader(staticShader);
        particleModel.setParticleShader(particleShader);

        staticTextures.setParticleModel(particleModel);
    }

    public void onDrawFrame(GL10 unused) {
        GLES31.glClear(GLES31.GL_COLOR_BUFFER_BIT);

        staticTextures.drawClassElements(loader);
        staticTextures.turretStateUpdate();
        particleModel.drawClassElements(loader);

    }

    public void onSurfaceChanged(GL10 unused, int width, int height) {
        GLES31.glViewport(0, 0, width, height);
        float ratio = (float) width / height;
        textureTransformations = new Transformations(ratio);

        loader.addUniformBlockBuffer(0, textureTransformations.getProjectionMatrix());
        staticTextures.setGameButtons(width, height, ratio, textureTransformations.getProjectionMatrix());

    }

    public StaticTextures getStaticTextures() {
        return staticTextures;
    }

}
