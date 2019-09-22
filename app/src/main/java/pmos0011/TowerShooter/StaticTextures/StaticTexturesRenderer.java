package pmos0011.TowerShooter.StaticTextures;

import android.graphics.PointF;
import android.opengl.GLES31;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import pmos0011.TowerShooter.AbstractClasses.ParticleEffects;
import pmos0011.TowerShooter.CommonObjects.BitmapID;
import pmos0011.TowerShooter.Enemies.Enemy;
import pmos0011.TowerShooter.Enemies.EnemyGenerator;
import pmos0011.TowerShooter.Enemies.EnemySupervisor;
import pmos0011.TowerShooter.GameControlObjects;
import pmos0011.TowerShooter.CommonObjects.ObjectsLoader;
import pmos0011.TowerShooter.GameLoopRenderer;
import pmos0011.TowerShooter.ParticleEffect.FireParticleEffect;
import pmos0011.TowerShooter.ParticleEffect.ParticleModelRenderer;
import pmos0011.TowerShooter.AbstractClasses.StaticModel;
import pmos0011.TowerShooter.CommonObjects.Transformations;
import pmos0011.TowerShooter.ParticleEffect.SmokeParticleEffect;
import pmos0011.TowerShooter.Weapons.Shells;

public class StaticTexturesRenderer extends StaticModel {

    public static final float GAME_CONTROL_OBJECT_SIZE = 0.18f;

    private static final float TOWER_HIT_SIZE = 0.1f;
    private final float TOWER_SIZE = 0.4f;
    private final float RADAR_SIZE = 0.065f;

    private ParticleModelRenderer particleModelRenderer;
    private EnemySupervisor enemySupervisor;
    private StaticShader staticShader;

    private GameControlObjects rightArrow = new GameControlObjects();
    private GameControlObjects leftArrow = new GameControlObjects();
    private GameControlObjects leftCannonButton = new GameControlObjects();
    private GameControlObjects rightCannonButton = new GameControlObjects();

    private float[] modelMatrix = new float[16];
    private float[] color = {1.0f, 1.0f, 1.0f, 1.0f};

    private boolean rotateLeft;
    private boolean rotateRight;
    private boolean isLeftCannonLoaded;
    private boolean isRightCannonLoaded;

    private float turretAngle;
    private float rotateDeltaSpeed;
    private float towerHP;
    private int hpIncreaserTimer;

    private float radarAngle;

    private float leftCannonReloadStatus;
    private float rightCannonReloadStatus;
    private float leftCannonPosition;
    private float rightCannonPosition;
    private SmokeParticleEffect hitPoints;

    private List<Shells> shells = new ArrayList<>();
    private List<Enemy> enemies = new ArrayList<>();

    public StaticTexturesRenderer(int vaoID) {
        super(vaoID);
    }

    public void setParticleModelRenderer(ParticleModelRenderer particleModelRenderer) {
        this.particleModelRenderer = particleModelRenderer;
        enemySupervisor = new EnemySupervisor(enemies);
        beginStatement();

    }

    @Override
    protected void enableVertexArrays() {
        super.enableVertexArray(0);
        super.enableVertexArray(1);
    }

    @Override
    protected void drawElements(ObjectsLoader loader) {

        staticShader.start();
        GLES31.glEnable(GLES31.GL_BLEND);
        GLES31.glBlendFunc(GLES31.GL_SRC_ALPHA, GLES31.GL_ONE_MINUS_SRC_ALPHA);

        drawBackground(loader);
        drawTowerBase(loader);
        drawShells(loader);
        drawLeftCannon(loader);
        drawRightCannon(loader);
        drawTurret(loader);
        drawRadar(loader);
        drawEnemies(loader);
        drawLeftRotateButton(loader);
        drawRightRotateButton(loader);
        drawLeftCannonButton(loader);
        drawRightCannonButton(loader);

        GLES31.glDisable(GLES31.GL_BLEND);
        staticShader.stop();
    }

    @Override
    protected void disableVertexArrays() {
        super.disableVertexArray(0);
        super.disableVertexArray(1);
    }




    private void drawBackground(ObjectsLoader loader) {
        Transformations.setModelTranslation(modelMatrix, 0, 0, 0, 0, Transformations.getRatio(), Transformations.getRatio());
        loader.loadUniformMatrix4fv(staticShader.getModelMatrixHandle(), modelMatrix);
        GLES31.glBindTexture(GLES31.GL_TEXTURE_2D, loader.getTextureID(BitmapID.textureNames.BACKGROUND.getValue()));
        GLES31.glDrawElements(GLES31.GL_TRIANGLES, StaticTexturesRenderer.DRAW_ORDER.length, GLES31.GL_UNSIGNED_SHORT, 0);
    }

    private void drawTowerBase(ObjectsLoader loader) {
        Transformations.setModelTranslation(modelMatrix, 0, 0, 0, 0, TOWER_SIZE, TOWER_SIZE);
        loader.loadUniformMatrix4fv(staticShader.getModelMatrixHandle(), modelMatrix);
        GLES31.glBindTexture(GLES31.GL_TEXTURE_2D, loader.getTextureID(BitmapID.textureNames.TOWER_BASE.getValue()));
        GLES31.glDrawElements(GLES31.GL_TRIANGLES, StaticTexturesRenderer.DRAW_ORDER.length, GLES31.GL_UNSIGNED_SHORT, 0);
    }

    private void drawLeftCannon(ObjectsLoader loader) {
        Transformations.setModelTranslation(modelMatrix, turretAngle, 0, 0, leftCannonPosition, TOWER_SIZE, TOWER_SIZE);
        loader.loadUniformMatrix4fv(staticShader.getModelMatrixHandle(), modelMatrix);
        GLES31.glBindTexture(GLES31.GL_TEXTURE_2D, loader.getTextureID(BitmapID.textureNames.TOWER_LEFT_CANNON.getValue()));
        GLES31.glDrawElements(GLES31.GL_TRIANGLES, StaticTexturesRenderer.DRAW_ORDER.length, GLES31.GL_UNSIGNED_SHORT, 0);
    }

    private void drawRightCannon(ObjectsLoader loader) {
        Transformations.setModelTranslation(modelMatrix, turretAngle, 0, 0, rightCannonPosition, TOWER_SIZE, TOWER_SIZE);
        loader.loadUniformMatrix4fv(staticShader.getModelMatrixHandle(), modelMatrix);
        GLES31.glBindTexture(GLES31.GL_TEXTURE_2D, loader.getTextureID(BitmapID.textureNames.TOWER_RIGHT_CANNON.getValue()));
        GLES31.glDrawElements(GLES31.GL_TRIANGLES, StaticTexturesRenderer.DRAW_ORDER.length, GLES31.GL_UNSIGNED_SHORT, 0);
    }

    private void drawTurret(ObjectsLoader loader) {
        Transformations.setModelTranslation(modelMatrix, turretAngle, 0, 0, 0, TOWER_SIZE, TOWER_SIZE);
        loader.loadUniformMatrix4fv(staticShader.getModelMatrixHandle(), modelMatrix);
        GLES31.glBindTexture(GLES31.GL_TEXTURE_2D, loader.getTextureID(BitmapID.textureNames.TURRET.getValue()));
        GLES31.glDrawElements(GLES31.GL_TRIANGLES, StaticTexturesRenderer.DRAW_ORDER.length, GLES31.GL_UNSIGNED_SHORT, 0);
        hpIncreaser();
    }

    private void drawRadar(ObjectsLoader loader) {
        Transformations.setModelTranslation(modelMatrix, turretAngle, radarAngle, 0.065f, -0.1f, RADAR_SIZE, RADAR_SIZE);
        loader.loadUniformMatrix4fv(staticShader.getModelMatrixHandle(), modelMatrix);
        GLES31.glBindTexture(GLES31.GL_TEXTURE_2D, loader.getTextureID(BitmapID.textureNames.RADAR.getValue()));
        GLES31.glDrawElements(GLES31.GL_TRIANGLES, StaticTexturesRenderer.DRAW_ORDER.length, GLES31.GL_UNSIGNED_SHORT, 0);
    }

    private void drawLeftRotateButton(ObjectsLoader loader) {
        Transformations.setModelTranslation(modelMatrix, 0, turretAngle, leftArrow.getOpenGLPosition().x, leftArrow.getOpenGLPosition().y,
                GAME_CONTROL_OBJECT_SIZE, GAME_CONTROL_OBJECT_SIZE);
        loader.loadUniformMatrix4fv(staticShader.getModelMatrixHandle(), modelMatrix);
        GLES31.glBindTexture(GLES31.GL_TEXTURE_2D, loader.getTextureID(BitmapID.textureNames.LEFT_ARROW.getValue()));
        GLES31.glDrawElements(GLES31.GL_TRIANGLES, StaticTexturesRenderer.DRAW_ORDER.length, GLES31.GL_UNSIGNED_SHORT, 0);
    }

    private void drawRightRotateButton(ObjectsLoader loader) {
        Transformations.setModelTranslation(modelMatrix, 0, turretAngle, rightArrow.getOpenGLPosition().x, rightArrow.getOpenGLPosition().y,
                GAME_CONTROL_OBJECT_SIZE, GAME_CONTROL_OBJECT_SIZE);
        loader.loadUniformMatrix4fv(staticShader.getModelMatrixHandle(), modelMatrix);
        GLES31.glBindTexture(GLES31.GL_TEXTURE_2D, loader.getTextureID(BitmapID.textureNames.RIGHT_ARROW.getValue()));
        GLES31.glDrawElements(GLES31.GL_TRIANGLES, StaticTexturesRenderer.DRAW_ORDER.length, GLES31.GL_UNSIGNED_SHORT, 0);
    }

    private void drawLeftCannonButton(ObjectsLoader loader) {
        Transformations.setModelTranslation(modelMatrix, 0, 0, leftCannonButton.getOpenGLPosition().x, leftCannonButton.getOpenGLPosition().y,
                GAME_CONTROL_OBJECT_SIZE, GAME_CONTROL_OBJECT_SIZE);
        loader.loadUniformMatrix4fv(staticShader.getModelMatrixHandle(), modelMatrix);
        GLES31.glBindTexture(GLES31.GL_TEXTURE_2D, loader.getTextureID(BitmapID.textureNames.LEFT_CANNON_BUTTON.getValue()));
        GLES31.glDrawElements(GLES31.GL_TRIANGLES, StaticTexturesRenderer.DRAW_ORDER.length, GLES31.GL_UNSIGNED_SHORT, 0);
    }

    private void drawRightCannonButton(ObjectsLoader loader) {
        Transformations.setModelTranslation(modelMatrix, 0, 0, rightCannonButton.getOpenGLPosition().x, rightArrow.getOpenGLPosition().y,
                GAME_CONTROL_OBJECT_SIZE, GAME_CONTROL_OBJECT_SIZE);
        loader.loadUniformMatrix4fv(staticShader.getModelMatrixHandle(), modelMatrix);
        GLES31.glBindTexture(GLES31.GL_TEXTURE_2D, loader.getTextureID(BitmapID.textureNames.RIGHT_CANNON_BUTTON.getValue()));
        GLES31.glDrawElements(GLES31.GL_TRIANGLES, StaticTexturesRenderer.DRAW_ORDER.length, GLES31.GL_UNSIGNED_SHORT, 0);
    }

    private void drawShells(ObjectsLoader loader) {

        Iterator<Shells> shellsIterator = shells.iterator();
        while (shellsIterator.hasNext()) {
            Shells shell = shellsIterator.next();

            drawShell(shell, loader);
            if (GameLoopRenderer.isGamePlay()) {
                addFireEffects(shell);

                shell.getPosition().x += shell.getDeltaSpeed().x;
                shell.getPosition().y += shell.getDeltaSpeed().y;

                checkHit(shell, shellsIterator);
            }
        }
    }

    private void drawShell(Shells shell, ObjectsLoader loader) {
        Transformations.setModelTranslation(modelMatrix, 0, shell.getAngle(), shell.getPosition().x, shell.getPosition().y,
                shell.getScale().x, shell.getScale().y);
        loader.loadUniformMatrix4fv(staticShader.getModelMatrixHandle(), modelMatrix);
        GLES31.glBindTexture(GLES31.GL_TEXTURE_2D, loader.getTextureID(BitmapID.textureNames.SHELL.getValue()));
        GLES31.glDrawElements(GLES31.GL_TRIANGLES, StaticTexturesRenderer.DRAW_ORDER.length, GLES31.GL_UNSIGNED_SHORT, 0);
    }

    private void addFireEffects(Shells shell) {
        if (shell.getPosition().equals(shell.getStartPosition())) {

            particleModelRenderer.addParticleEffect(new FireParticleEffect(ParticleEffects.effectKind.CANNON_FIRE, shell.getAngle(), 0,
                    shell.getOffset(), shell.getStartPosition().x, shell.getStartPosition().y, shell.getScale().x, 0));
            particleModelRenderer.addParticleEffect(new SmokeParticleEffect(ParticleEffects.effectKind.CANNON_SMOKE, shell.getAngle(), 0,
                    shell.getOffset(), shell.getStartPosition().x, shell.getStartPosition().y, shell.getScale().x, 0));
        }
    }

    private void checkHit(Shells shell, Iterator<Shells> shellsIterator) {
        try {
            boolean shellRemove;
            if (shell.isEnemy())
                shellRemove = checkTowerHit(shell);
            else {
                shellRemove = checkEnemyHit(shell);
                if (!shellRemove)
                    shellRemove = checkOutOfBounds(shell);
            }
            if (shellRemove) {
                shellsIterator.remove();
                particleModelRenderer.addParticleEffect(new SmokeParticleEffect(ParticleEffects.effectKind.SHELL_STREAK, 0, shell.getAngle(),
                        shell.getOffset(), shell.getStartPosition().x, shell.getStartPosition().y, shell.getScale().x, shell.getTravelDistance()));
            }
        } catch (Exception e) {

        }
    }

    private boolean checkTowerHit(Shells shell) {
        if (shell.getStartPosition().x < 0 && shell.getPosition().x > 0 || shell.getStartPosition().x > 0 && shell.getPosition().x < 0) {
            addSparks(shell);
            calculateDamage();
            return true;
        }
        return false;
    }

    private void calculateDamage() {
        float damage = new Random().nextFloat() / 10;
        towerHP -= 0.1f - damage;
        if (towerHP < 0) {
            towerHP = 0;
            if (!GameLoopRenderer.isGameOver()) {
                particleModelRenderer.addExplosion(0, 0);
                GameLoopRenderer.endGame();
            }
        }
    }

    private void hpIncreaser() {
        if (enemies.size() == 0 && towerHP < 1 && hpIncreaserTimer != GameLoopRenderer.getWaveTimer()
                && GameLoopRenderer.isGamePlay()) {
            towerHP += 0.02f;
            hpIncreaserTimer = GameLoopRenderer.getWaveTimer();
        }
    }

    private boolean checkOutOfBounds(Shells shell) {
        return shell.getPosition().x > 2 * Transformations.getRatio() || shell.getPosition().y > 2
                || shell.getPosition().x < -2 * Transformations.getRatio() || shell.getPosition().y < -2;
    }

    private boolean checkEnemyHit(Shells shell) {
        Iterator<Enemy> enemyIterator = enemies.iterator();
        while (enemyIterator.hasNext()) {
            Enemy enemy = enemyIterator.next();
            if (shell.getPosition().y > enemy.getPosition().y - Enemy.TANK_HIT_SIZE && shell.getPosition().y < enemy.getPosition().y + Enemy.TANK_HIT_SIZE)
                if (shell.getPosition().x > enemy.getPosition().x - Enemy.TANK_HIT_SIZE && shell.getPosition().x < enemy.getPosition().x + Enemy.TANK_HIT_SIZE) {
                    enemy.hitCalculation(shell.getPosition().y);
                    addSparks(shell);
                    return true;
                }
        }
        return false;
    }

    private void addSparks(Shells shell) {
        PointF deltaPosition = new PointF(0, 0);
        if (shell.isEnemy())
            deltaPosition = Transformations.calculatePoint(shell.getAngle(), -TOWER_HIT_SIZE);

        int sparksCount = new Random().nextInt(8) + 4;
        float deltaAngle = 60 / sparksCount;
        float currentAngle = shell.getAngle() - sparksCount * deltaAngle;
        for (int i = 0; i < 2 * sparksCount + 1; i++) {

            particleModelRenderer.addParticleEffect(new FireParticleEffect(ParticleEffects.effectKind.HIT_SPARK, currentAngle, 0, 0,
                    shell.getPosition().x + deltaPosition.x, shell.getPosition().y + deltaPosition.y,
                    shell.getScale().x, 0));
            currentAngle += deltaAngle;
        }
    }

    private void drawEnemies(ObjectsLoader loader) {

        try {
            Iterator<Enemy> enemyIterator = enemies.iterator();
            while (enemyIterator.hasNext()) {
                Enemy enemy = enemyIterator.next();
                color[3] = enemy.getOpacity();
                Transformations.setModelTranslation(modelMatrix, 0, enemy.getAngle(), enemy.getPosition().x, enemy.getPosition().y, enemy.getScale().x, enemy.getScale().y);
                loader.loadUniform4fv(staticShader.getColorHandle(), color);
                loader.loadUniformMatrix4fv(staticShader.getModelMatrixHandle(), modelMatrix);
                GLES31.glBindTexture(GLES31.GL_TEXTURE_2D, loader.getTextureID(BitmapID.textureNames.TANK_CHASSIS.getValue()));
                GLES31.glDrawElements(GLES31.GL_TRIANGLES, StaticTexturesRenderer.DRAW_ORDER.length, GLES31.GL_UNSIGNED_SHORT, 0);

                Transformations.setModelTranslation(modelMatrix, 0, enemy.getTurretAngle(), enemy.getPosition().x, enemy.getPosition().y, enemy.getScale().x, enemy.getScale().y);
                loader.loadUniformMatrix4fv(staticShader.getModelMatrixHandle(), modelMatrix);
                GLES31.glBindTexture(GLES31.GL_TEXTURE_2D, loader.getTextureID(BitmapID.textureNames.TANK_TURRET.getValue()));
                GLES31.glDrawElements(GLES31.GL_TRIANGLES, StaticTexturesRenderer.DRAW_ORDER.length, GLES31.GL_UNSIGNED_SHORT, 0);

                if (GameLoopRenderer.isGamePlay()) {
                    enemySupervisor.inspectEnemy(enemy);
                    enemy.enemyMove();

                    if (enemy.getOpacity() <= 0)
                        enemyIterator.remove();
                }
            }
        } catch (Exception e) {
        }

        color[3] = 1.0f;
        loader.loadUniform4fv(staticShader.getColorHandle(), color);
    }

    private void addTowerHPBar() {
        hitPoints = new SmokeParticleEffect(ParticleEffects.effectKind.HIT_POINTS,
                0, 0, 0.2f, 0, 0,
                ParticleEffects.HIT_POINT_SIZE, 0);
        particleModelRenderer.addParticleEffect(hitPoints);
    }





    public void setStaticShader(StaticShader staticShader) {
        this.staticShader = staticShader;
    }

    public void beginStatement() {
        towerHP = 1;
        radarAngle = 0;
        turretAngle = 0;
        hpIncreaserTimer = 0;

        leftCannonReloadStatus = 1.0f;
        rightCannonReloadStatus = 1.0f;
        leftCannonPosition = 0.0f;
        rightCannonPosition = 0.0f;
        rotateDeltaSpeed = 0;

        rotateLeft = false;
        rotateRight = false;
        isLeftCannonLoaded = true;
        isRightCannonLoaded = true;

        enemies.clear();
        shells.clear();

        addTowerHPBar();

        EnemyGenerator enemyGenerator = new EnemyGenerator(enemies, this, particleModelRenderer);
        Thread t = new Thread(enemyGenerator);
        t.start();
    }

    public void setGameButtons(int width, int height, float ratio, float[] projectionMatrix) {
        leftArrow.setObject(width, height, GAME_CONTROL_OBJECT_SIZE, -ratio + 1.2f * GAME_CONTROL_OBJECT_SIZE,
                -1 + GAME_CONTROL_OBJECT_SIZE, projectionMatrix);

        rightArrow.setObject(width, height, GAME_CONTROL_OBJECT_SIZE, -ratio + 3.4f * GAME_CONTROL_OBJECT_SIZE,
                -1 + GAME_CONTROL_OBJECT_SIZE, projectionMatrix);

        leftCannonButton.setObject(width, height, GAME_CONTROL_OBJECT_SIZE, ratio - 3.2f * GAME_CONTROL_OBJECT_SIZE,
                -1 + GAME_CONTROL_OBJECT_SIZE, projectionMatrix);

        rightCannonButton.setObject(width, height, GAME_CONTROL_OBJECT_SIZE, ratio - 1.1f * GAME_CONTROL_OBJECT_SIZE,
                -1 + GAME_CONTROL_OBJECT_SIZE, projectionMatrix);
    }

    public GameControlObjects getRightArrow() {
        return rightArrow;
    }

    public GameControlObjects getLeftArrow() {
        return leftArrow;
    }

    public GameControlObjects getLeftCannonButton() {
        return leftCannonButton;
    }

    public GameControlObjects getRightCannonButton() {
        return rightCannonButton;
    }

    public void enableLeftRotation() {
        rotateLeft = true;
        rotateRight = false;
    }

    public void enableRightRotation() {
        rotateLeft = false;
        rotateRight = true;
    }

    public void disableRotation() {
        rotateLeft = false;
        rotateRight = false;
    }

    public boolean isLeftCannonLoaded() {
        return isLeftCannonLoaded;
    }

    public boolean isRightCannonLoaded() {
        return isRightCannonLoaded;
    }

    public void fireFromLeft() {
        particleModelRenderer.addParticleEffect(new SmokeParticleEffect(ParticleEffects.effectKind.RELOAD_STATUS, 0, 0, 0,
                leftCannonButton.getOpenGLPosition().x, leftCannonButton.getOpenGLPosition().y, 0, 0));
        shells.add(new Shells(turretAngle, true, Shells.SHELL_SPEED));
        isLeftCannonLoaded = false;
        leftCannonPosition = -0.038f;
    }

    public void fireFromRight() {
        particleModelRenderer.addParticleEffect(new SmokeParticleEffect(ParticleEffects.effectKind.RELOAD_STATUS, 0, 0, 0,
                rightCannonButton.getOpenGLPosition().x, rightCannonButton.getOpenGLPosition().y, 0, 0));
        shells.add(new Shells(turretAngle, false, Shells.SHELL_SPEED));
        isRightCannonLoaded = false;
        rightCannonPosition = -0.038f;
    }

    public void turretStateUpdate() {

        hitPoints.setScaleX(ParticleEffects.HIT_POINT_SIZE * towerHP);

        if (rotateLeft) {
            if (rotateDeltaSpeed < 0.3f)
                rotateDeltaSpeed += 0.005f;
        } else {
            if (rotateDeltaSpeed > 0)
                rotateDeltaSpeed -= 0.003f;
        }

        if (rotateRight) {
            if (rotateDeltaSpeed > -0.3f)
                rotateDeltaSpeed -= 0.005f;
        } else {
            if (rotateDeltaSpeed < 0)
                rotateDeltaSpeed += 0.003f;
        }

        if (Math.abs(rotateDeltaSpeed) < 0.003 && Math.abs(rotateDeltaSpeed) > 0)
            rotateDeltaSpeed = 0f;

        turretAngle += rotateDeltaSpeed;

        if (turretAngle > 359.7)
            turretAngle = 0;
        if (turretAngle < 0)
            turretAngle = 359.7f;

        radarAngle -= 2;
        if (radarAngle < -358)
            radarAngle = 0;

        if (!isLeftCannonLoaded()) {
            leftCannonReloadStatus -= 0.002f;
            if (leftCannonReloadStatus <= 0.0f) {
                leftCannonReloadStatus = 1.0f;
                isLeftCannonLoaded = true;
            }
            if (leftCannonPosition < 0.0f)
                leftCannonPosition += 0.001f;
        }

        if (!isRightCannonLoaded()) {
            rightCannonReloadStatus -= 0.002f;
            if (rightCannonReloadStatus <= 0.0f) {
                rightCannonReloadStatus = 1.0f;
                isRightCannonLoaded = true;
            }
            if (rightCannonPosition < 0.0f)
                rightCannonPosition += 0.001f;
        }
    }

    public void addShell(float angle, float xPos, float yPos, float speed) {

        shells.add(new Shells(angle, xPos, yPos, speed));
    }

}
