package pmos0011.TowerShooter;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLES31;
import android.opengl.GLSurfaceView;

import java.util.Random;

import pmos0011.TowerShooter.AbstractClasses.ParticleEffects;
import pmos0011.TowerShooter.AbstractClasses.StaticModel;
import pmos0011.TowerShooter.CommonObjects.BitmapID;
import pmos0011.TowerShooter.CommonObjects.ObjectsLoader;
import pmos0011.TowerShooter.CommonObjects.Transformations;
import pmos0011.TowerShooter.ParticleEffect.ParticleModelRenderer;
import pmos0011.TowerShooter.ParticleEffect.ParticleShader;
import pmos0011.TowerShooter.StaticTextures.StaticShader;
import pmos0011.TowerShooter.StaticTextures.StaticTexturesRenderer;
import pmos0011.TowerShooter.TextHendler.FontRenderer;
import pmos0011.TowerShooter.TextHendler.FontSettingsReader;
import pmos0011.TowerShooter.TextHendler.FontShader;


public class GameLoopRenderer implements GLSurfaceView.Renderer {

    public static final float WIND_FLOW_X = new Random().nextFloat() / 1000f;
    public static final float WIND_FLOW_Y = new Random().nextFloat() / 1000f;

    private Context context;
    public static boolean restartGame;
    public static boolean enableTouchEvents;

    private ObjectsLoader loader;
    private StaticShader staticShader;
    private ParticleShader particleShader;
    private FontShader fontShader;
    private StaticTexturesRenderer staticTexturesRenderer;
    private ParticleModelRenderer particleModelRenderer;
    private FontRenderer fontRenderer;
    private Transformations textureTransformations;

    private static int bodyCount;
    private static int waveTimer;
    private static boolean gamePlay;
    private static boolean gameOver;

    public GameLoopRenderer(Context context) {
        this.context = context;
    }

    public void onSurfaceCreated(GL10 unused, EGLConfig config) {
        GLES31.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        restartGame = false;
        enableTouchEvents = true;

        loader = new ObjectsLoader(context, BitmapID.getStaticBitmapID());
        staticShader = new StaticShader(context, R.raw.texture_vertex_shader, R.raw.texture_fragment_shader);
        particleShader = new ParticleShader(context, R.raw.particle_vertex_shader, R.raw.particle_framgent_shader);
        fontShader = new FontShader(context, R.raw.text_vertex_shader, R.raw.text_fragment_shader);
        staticTexturesRenderer = loader.loadToVAO(StaticModel.SQUERE_CORDS, StaticModel.COORDS_PER_VERTEX, StaticModel.TEXTURE_COORDS, StaticModel.DRAW_ORDER);
        particleModelRenderer = loader.loadTOVAO(StaticModel.SQUERE_CORDS, StaticModel.COORDS_PER_VERTEX, StaticModel.TEXTURE_COORDS, StaticModel.DRAW_ORDER, ParticleEffects.PARTICLE_MAX_COUNT);
        fontRenderer = loader.loadToVAO(StaticModel.SQUERE_CORDS, StaticModel.COORDS_PER_VERTEX, StaticModel.TEXTURE_COORDS, StaticModel.DRAW_ORDER, FontRenderer.LETTERS_MAX_COUNT);
        staticTexturesRenderer.setStaticShader(staticShader);
        particleModelRenderer.setParticleShader(particleShader);
        fontRenderer.setFontShader(fontShader);

        staticTexturesRenderer.setParticleModelRenderer(particleModelRenderer);
    }

    public void onDrawFrame(GL10 unused) {
        GLES31.glClear(GLES31.GL_COLOR_BUFFER_BIT);

        if (restartGame)
            resetGame();

        staticTexturesRenderer.drawClassElements(loader);
        staticTexturesRenderer.turretStateUpdate();
        particleModelRenderer.drawClassElements(loader);

        textHandling();
    }

    public void onSurfaceChanged(GL10 unused, int width, int height) {
        GLES31.glViewport(0, 0, width, height);
        float ratio = (float) width / height;
        textureTransformations = new Transformations(ratio);

        loader.addUniformBlockBuffer(0, textureTransformations.getProjectionMatrix());
        staticTexturesRenderer.setGameButtons(width, height, ratio, textureTransformations.getProjectionMatrix());

        FontSettingsReader.loadCharacters(context, fontRenderer);
    }


    private void textHandling() {
        fontRenderer.writeText(-Transformations.getRatio() + 0.06f, 0.88f, 0.06f, FontRenderer.GREEN_FONT_COLOR, "KILLS " + bodyCount);
        fontRenderer.writeText(Transformations.getRatio() - 0.9f, 0.9f, 0.04f, FontRenderer.GREEN_FONT_COLOR, "NEXT WAVE " + waveTimer);

        if (!gamePlay && enableTouchEvents)
            fontRenderer.writeText(-1.0f, 0.3f, 0.1f, FontRenderer.GREEN_FONT_COLOR, "PRESS TO PLAY");

        if (gameOver)
            fontRenderer.writeText(-0.7f, -0.3f, 0.1f, FontRenderer.GREEN_FONT_COLOR, "GAME OVER");

        fontRenderer.drawClassElements(loader);
    }

    private static void resetBodyCount() {
        bodyCount = 0;
    }

    private static void startGame() {
        resetBodyCount();
        gamePlay = true;
        gameOver = false;
    }

    private void resetGame() {
        particleModelRenderer.effectsClear();
        restartGame = false;
        waveTimer = 5;
        staticTexturesRenderer.beginStatement();
        startGame();
        particleModelRenderer.drawRadar();
    }

    private static void endGameTimer() {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        gamePlay = false;
        enableTouchEvents = true;
    }


    public static void endGame() {
        enableTouchEvents = false;
        gameOver = true;

        new Thread(new Runnable() {
            @Override
            public void run() {
                endGameTimer();
            }
        }).start();
    }

    public static void decreaseTimer() {
        if (waveTimer > 0)
            waveTimer--;
    }

    public static void addKill() {
        bodyCount++;
    }


    public static void setWaveTimer(int waveTimer) {
        GameLoopRenderer.waveTimer = waveTimer;
    }

    public static int getWaveTimer() {
        return waveTimer;
    }

    public StaticTexturesRenderer getStaticTexturesRenderer() {
        return staticTexturesRenderer;
    }

    public static boolean isGamePlay() {
        return gamePlay;
    }

    public static boolean isGameOver() {
        return gameOver;
    }


}
