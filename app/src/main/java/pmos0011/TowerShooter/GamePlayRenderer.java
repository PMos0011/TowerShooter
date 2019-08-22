package pmos0011.TowerShooter;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLES31;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import TextCreator.FontRenderer;
import TextCreator.FontSettingsReader;

public class GamePlayRenderer implements GLSurfaceView.Renderer {

    public static final float Z_DIMENSION = -1.0000001f;
    public static final float GAME_CONTROL_OBJECT_SIZE = 0.18f;
    public static final float TOWER_SIZE = 0.4f;
    public static final float RADAR_SIZE = 0.065f;
    public static final float SHELL_SIZE = 0.009f;
    public static final float SHELL_ASPECT = 300.0f / 76.0f;
    public static final float SMOKE_EFFECT_SIZE = 0.2f;
    public static final float CANNON_X_POSITION = 0.0265f;
    public static final float WIND_FLOW_X = new Random().nextFloat() / 1000f;
    public static final float WIND_FLOW_Y = new Random().nextFloat() / 1000f;
    public static final float TOWER_CANNON_Y_POSITION = 0.55f;
    public static final float TANK_CANNON_Y_POSITION = 0.35f;
    public static final float SHELL_SPEED = 0.1f;
    public static final float LASER_SIGHT_DISPERSION = 0.015f;
    public static final float TANK_SIZE = 0.13f;
    public static final float TANK_ASPECT = 1900.0f/800.f;
    public static final float TANK_MAX_SPEED = 0.001f;

    private Context mContext;
    private Texture gameObjectTextures;
    private FontRenderer fontRenderer = new FontRenderer();

    private Square laserSight;
    private Square reloadStatus;

    GameControlObjects rightArrow;
    GameControlObjects leftArrow;
    GameControlObjects leftCannonButton;
    GameControlObjects rightCannonButton;

    private static float ratio;

    public static int[] staticBitmapID;
    public static float[] mProjectionMatrix = new float[16];
    private float[] mModelMatrix = new float[16];

    private float turretAngle = 0;
    private float radarAngle = 0;
    public boolean rotateRight = false;
    public boolean rotateLeft = false;
    public boolean gamePlay = false;

    public boolean isLeftCannonReloading = false;
    public boolean isRightCannonReloading = false;
    private float leftCannonReloadStatus = 1.0f;
    private float rightCannonReloadStatus = 1.0f;
    private float leftCannonPosition = 0;
    private float rightCannonPosition = 0;

    private List<SmokeEffect> smokeEffects = new ArrayList<>();
    private List<Shells> shells = new ArrayList<>();
    private List<Enemy> enemies = new ArrayList<>();

    int score = 0;

    public GamePlayRenderer(Context context) {
        mContext = context;
    }

    public void onSurfaceCreated(GL10 unused, EGLConfig config) {

        FontSettingsReader.reader(mContext, fontRenderer);

        enemies.add(new Enemy(0,-1,0,TANK_MAX_SPEED));

        GLES31.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

        staticBitmapID = BitmapID.getStaticBitmapID();
        ShadersManager.loadShaders(mContext);

        gameObjectTextures = new Texture();
        for (int i : staticBitmapID) gameObjectTextures.loadTexture(mContext, i);

        leftArrow = new GameControlObjects();
        rightArrow = new GameControlObjects();
        leftCannonButton = new GameControlObjects();
        rightCannonButton = new GameControlObjects();

        laserSight = new Square(true);
        reloadStatus = new Square(false);


        //enemies.add(new Enemy(turretAngle, -1,0,SHELL_SIZE));
        //smokeEffects.add(new SmokeEffect(SmokeEffect.effectsNames.EXHAUST,0,-0.23f,-1.02f,0,SMOKE_EFFECT_SIZE/2.5f));
        //smokeEffects.add(new SmokeEffect(SmokeEffect.effectsNames.EXHAUST,0,-0.23f,-0.98f,0,SMOKE_EFFECT_SIZE/2.5f));
    }

    public void onDrawFrame(GL10 unused) {

        GLES31.glClear(GLES31.GL_COLOR_BUFFER_BIT);

        Matrix.setIdentityM(mModelMatrix, 0);
        Matrix.translateM(mModelMatrix, 0, 0, 0, Z_DIMENSION);
        Matrix.scaleM(mModelMatrix, 0, ratio, ratio, 1);
        gameObjectTextures.draw(mModelMatrix, staticBitmapID[BitmapID.textureNames.BACKGROUND.getValue()], 1);

        Matrix.setIdentityM(mModelMatrix, 0);
        Matrix.translateM(mModelMatrix, 0, 0, 0, Z_DIMENSION);
        Matrix.scaleM(mModelMatrix, 0, TOWER_SIZE, TOWER_SIZE, 1);
        gameObjectTextures.draw(mModelMatrix, staticBitmapID[BitmapID.textureNames.TURRET_BASE.getValue()], 1);

        Matrix.setIdentityM(mModelMatrix, 0);
        Matrix.rotateM(mModelMatrix, 0, turretAngle, 0, 0, 1.0f);
        Matrix.translateM(mModelMatrix, 0, 0, 0, Z_DIMENSION);
        laserSight.draw(mModelMatrix, ratio, true);

        Iterator<Enemy> enemyIterator = enemies.iterator();
        while(enemyIterator.hasNext()){
            Enemy enemy = enemyIterator.next();
            Matrix.setIdentityM(mModelMatrix, 0);
            Matrix.translateM(mModelMatrix, 0, enemy.position.x, enemy.position.y, Z_DIMENSION);
            Matrix.rotateM(mModelMatrix, 0, enemy.angle, 0, 0, 1.0f);
            Matrix.scaleM(mModelMatrix, 0, enemy.scale.x, enemy.scale.y, 1);
            gameObjectTextures.draw(mModelMatrix, staticBitmapID[BitmapID.textureNames.TANK_CHASSIS.getValue()], 1);

            Matrix.setIdentityM(mModelMatrix, 0);
            Matrix.translateM(mModelMatrix, 0, enemy.position.x, enemy.position.y, Z_DIMENSION);
            Matrix.rotateM(mModelMatrix, 0, enemy.turretAngle, 0, 0, 1.0f);
            Matrix.scaleM(mModelMatrix, 0, enemy.scale.x, enemy.scale.y, 1);
            gameObjectTextures.draw(mModelMatrix, staticBitmapID[BitmapID.textureNames.TANK_TURRET.getValue()], 1);

            enemy.turretAngle += 0.2f;
            if (enemy.turretAngle > 360)
                enemy.turretAngle = 0.2f;

            enemy.position.x+=enemy.deltaSpeed.x;
            enemy.position.y+=enemy.deltaSpeed.y;

            if(enemy.position.y>1.0f){
                enemy.position.y=-1.0f;
            }

        }

        Iterator<Shells> shellsIterator = shells.iterator();
        while (shellsIterator.hasNext()) {
            Shells shell = shellsIterator.next();
            Matrix.setIdentityM(mModelMatrix, 0);
            Matrix.translateM(mModelMatrix, 0, shell.position.x, shell.position.y, Z_DIMENSION);
            Matrix.rotateM(mModelMatrix, 0, shell.angle, 0, 0, 1.0f);
            Matrix.scaleM(mModelMatrix, 0, shell.scale.x, shell.scale.y, 1);
            gameObjectTextures.draw(mModelMatrix, staticBitmapID[BitmapID.textureNames.SHELL.getValue()], 1);

            shell.position.x += shell.deltaSpeed.x;
            shell.position.y += shell.deltaSpeed.y;
            if (shell.position.x > 2 * ratio || shell.position.y > 2)
                shellsIterator.remove();
        }

        Matrix.setIdentityM(mModelMatrix, 0);
        Matrix.rotateM(mModelMatrix, 0, turretAngle, 0, 0, 1.0f);
        Matrix.translateM(mModelMatrix, 0, 0, leftCannonPosition, Z_DIMENSION);
        Matrix.scaleM(mModelMatrix, 0, TOWER_SIZE, TOWER_SIZE, 1);
        gameObjectTextures.draw(mModelMatrix, staticBitmapID[BitmapID.textureNames.TURRET_L_CANNON.getValue()], 1);

        Matrix.setIdentityM(mModelMatrix, 0);
        Matrix.rotateM(mModelMatrix, 0, turretAngle, 0, 0, 1.0f);
        Matrix.translateM(mModelMatrix, 0, 0, rightCannonPosition, Z_DIMENSION);
        Matrix.scaleM(mModelMatrix, 0, TOWER_SIZE, TOWER_SIZE, 1);
        gameObjectTextures.draw(mModelMatrix, staticBitmapID[BitmapID.textureNames.TURRET_R_CANNON.getValue()], 1);

        Matrix.setIdentityM(mModelMatrix, 0);
        Matrix.translateM(mModelMatrix, 0, 0, 0, Z_DIMENSION);
        Matrix.rotateM(mModelMatrix, 0, turretAngle, 0, 0, 1.0f);
        Matrix.scaleM(mModelMatrix, 0, TOWER_SIZE, TOWER_SIZE, 1);
        gameObjectTextures.draw(mModelMatrix, staticBitmapID[BitmapID.textureNames.TURRET_TOWER.getValue()], 1);

        Matrix.setIdentityM(mModelMatrix, 0);
        Matrix.rotateM(mModelMatrix, 0, turretAngle, 0, 0, 1.0f);
        Matrix.translateM(mModelMatrix, 0, 0.065f, -0.1f, Z_DIMENSION);
        Matrix.rotateM(mModelMatrix, 0, radarAngle, 0, 0, 1.0f);
        Matrix.scaleM(mModelMatrix, 0, RADAR_SIZE, RADAR_SIZE, 1);
        gameObjectTextures.draw(mModelMatrix, staticBitmapID[BitmapID.textureNames.RADAR.getValue()], 1);

        Matrix.setIdentityM(mModelMatrix, 0);
        Matrix.translateM(mModelMatrix, 0, leftArrow.xOpenGLPosition, rightArrow.yOpenGLPosition, Z_DIMENSION);
        Matrix.rotateM(mModelMatrix, 0, turretAngle, 0, 0, 1.0f);
        Matrix.scaleM(mModelMatrix, 0, GAME_CONTROL_OBJECT_SIZE, GAME_CONTROL_OBJECT_SIZE, 1);
        gameObjectTextures.draw(mModelMatrix, staticBitmapID[BitmapID.textureNames.LEFT_ARROW.getValue()], 1);

        Matrix.setIdentityM(mModelMatrix, 0);
        Matrix.translateM(mModelMatrix, 0, rightArrow.xOpenGLPosition, rightArrow.yOpenGLPosition, Z_DIMENSION);
        Matrix.rotateM(mModelMatrix, 0, turretAngle, 0, 0, 1.0f);
        Matrix.scaleM(mModelMatrix, 0, GAME_CONTROL_OBJECT_SIZE, GAME_CONTROL_OBJECT_SIZE, 1);
        gameObjectTextures.draw(mModelMatrix, staticBitmapID[BitmapID.textureNames.RIGHT_ARROW.getValue()], 1);

        Matrix.setIdentityM(mModelMatrix, 0);
        Matrix.translateM(mModelMatrix, 0, leftCannonButton.xOpenGLPosition, leftCannonButton.yOpenGLPosition, Z_DIMENSION);
        Matrix.scaleM(mModelMatrix, 0, GAME_CONTROL_OBJECT_SIZE, GAME_CONTROL_OBJECT_SIZE, 1);
        gameObjectTextures.draw(mModelMatrix, staticBitmapID[BitmapID.textureNames.LEFT_CANNON_BUTTON.getValue()], 1);

        Matrix.setIdentityM(mModelMatrix, 0);
        Matrix.translateM(mModelMatrix, 0, rightCannonButton.xOpenGLPosition, rightCannonButton.yOpenGLPosition, Z_DIMENSION);
        Matrix.scaleM(mModelMatrix, 0, GAME_CONTROL_OBJECT_SIZE, GAME_CONTROL_OBJECT_SIZE, 1);
        gameObjectTextures.draw(mModelMatrix, staticBitmapID[BitmapID.textureNames.RIGHT_CANNON_BUTTON.getValue()], 1);

        Iterator<SmokeEffect> smokeEffectIterator = smokeEffects.iterator();
        while (smokeEffectIterator.hasNext()) {
            SmokeEffect smoke = smokeEffectIterator.next();
            smoke.draw(staticBitmapID[BitmapID.textureNames.LEFT_ARROW.getValue()]);
            if (smoke.visibility <= 0)
                smokeEffectIterator.remove();
        }

        if (!gamePlay)
            fontRenderer.writeText(mModelMatrix, -1.2f, 0.3f, "PRESS TO PLAY", 0.13f, staticBitmapID[BitmapID.textureNames.FONT_MAP.getValue()]);
        fontRenderer.writeText(mModelMatrix, -ratio + 0.06f, 0.88f, "KILLS " + score, 0.06f, staticBitmapID[BitmapID.textureNames.FONT_MAP.getValue()]);

            gameActions();
    }

    public void onSurfaceChanged(GL10 unused, int width, int height) {
        GLES31.glViewport(0, 0, width, height);
        ratio = (float) width / height;
        Matrix.frustumM(mProjectionMatrix, 0, -ratio, ratio, -1.0f, 1.0f, 1.0f, 2.0f);

        leftArrow.setObject(width, height, GAME_CONTROL_OBJECT_SIZE, -ratio + 1.2f * GAME_CONTROL_OBJECT_SIZE, -1 + GAME_CONTROL_OBJECT_SIZE);
        rightArrow.setObject(width, height, GAME_CONTROL_OBJECT_SIZE, -ratio + 3.4f * GAME_CONTROL_OBJECT_SIZE, -1 + GAME_CONTROL_OBJECT_SIZE);
        leftCannonButton.setObject(width, height, GAME_CONTROL_OBJECT_SIZE, ratio - 3.2f * GAME_CONTROL_OBJECT_SIZE, -1 + GAME_CONTROL_OBJECT_SIZE);
        rightCannonButton.setObject(width, height, GAME_CONTROL_OBJECT_SIZE, ratio - 1.1f * GAME_CONTROL_OBJECT_SIZE, -1 + GAME_CONTROL_OBJECT_SIZE);
    }

    private void gameActions() {

        radarAngle -= 2;
        if (radarAngle < -358)
            radarAngle = 0;

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

        if (isLeftCannonReloading) {
            if (leftCannonReloadStatus == 1) {
                leftCannonPosition = -0.035f;
                smokeEffects.add(new SmokeEffect(SmokeEffect.effectsNames.CANNON_SMOKE,turretAngle,0.55f,0,0,SMOKE_EFFECT_SIZE));
                smokeEffects.add(new SmokeEffect(SmokeEffect.effectsNames.CANNON_FIRE,turretAngle,0.55f,0,0,SMOKE_EFFECT_SIZE));
               // smokeEffects.add(new SmokeEffect(SmokeEffect.effectsNames.CANNON_SMOKE,turretAngle,0.35f,-1,0,SMOKE_EFFECT_SIZE/3));
               // smokeEffects.add(new SmokeEffect(SmokeEffect.effectsNames.CANNON_FIRE,turretAngle,0.35f,-1,0,SMOKE_EFFECT_SIZE/3));
                shells.add(new Shells(turretAngle, true,SHELL_SPEED));
            }
            if (leftCannonPosition < 0.0)
                leftCannonPosition += 0.001;

            Matrix.setIdentityM(mModelMatrix, 0);
            Matrix.translateM(mModelMatrix, 0, leftCannonButton.xOpenGLPosition, leftCannonButton.yOpenGLPosition, Z_DIMENSION);
            reloadStatus.draw(mModelMatrix, leftCannonReloadStatus, false);

            leftCannonReloadStatus -= 0.002f;

            if (leftCannonReloadStatus <= 0) {
                isLeftCannonReloading = false;
                leftCannonReloadStatus = 1;
            }
        }

        if (isRightCannonReloading) {
            if (rightCannonReloadStatus == 1) {
                rightCannonPosition = -0.035f;
                smokeEffects.add(new SmokeEffect(SmokeEffect.effectsNames.CANNON_SMOKE,turretAngle,0.45f,0,0,SMOKE_EFFECT_SIZE));
                smokeEffects.add(new SmokeEffect(SmokeEffect.effectsNames.CANNON_FIRE,turretAngle,0.45f,0,0,SMOKE_EFFECT_SIZE));
                //smokeEffects.add(new SmokeEffect(SmokeEffect.effectsNames.CANNON_SMOKE,turretAngle,0.35f,-1,0,SMOKE_EFFECT_SIZE/2));
                //smokeEffects.add(new SmokeEffect(SmokeEffect.effectsNames.CANNON_FIRE,turretAngle,0.35f,-1,0,SMOKE_EFFECT_SIZE/2));
                shells.add(new Shells(turretAngle, false, SHELL_SPEED));
                //shells.add(new Shells(turretAngle, -1,0));
            }
            if (rightCannonPosition < 0.0)
                rightCannonPosition += 0.001;

            Matrix.setIdentityM(mModelMatrix, 0);
            Matrix.translateM(mModelMatrix, 0, rightCannonButton.xOpenGLPosition, rightCannonButton.yOpenGLPosition, Z_DIMENSION);
            Matrix.rotateM(mModelMatrix, 0, 180, 0, 0, 1.0f);
            reloadStatus.draw(mModelMatrix, rightCannonReloadStatus, false);

            rightCannonReloadStatus -= 0.002f;

            if (rightCannonReloadStatus <= 0) {
                isRightCannonReloading = false;
                rightCannonReloadStatus = 1;
            }
        }
    }
}
