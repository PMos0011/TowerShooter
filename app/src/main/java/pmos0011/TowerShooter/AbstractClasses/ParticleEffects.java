package pmos0011.TowerShooter.AbstractClasses;

import android.graphics.PointF;

import java.util.Random;

public abstract class ParticleEffects {

    public final static int PARTICLE_DATA_LENGTH = 28;
    public final static int PARTICLE_MAX_COUNT = 150;
    public final static float RADAR_SIZE = 0.13f;
    public final static float HIT_POINT_SIZE = 0.08f;
    public final static float TANK_EXPLOSION_SIZE = 0.25f;

    private float[] modelMatrix = new float[16];
    private float[] innerColor = new float[4];
    private float[] outerColor = new float[4];
    private float[] options = new float[4];
    private float worldAngle;
    private float objectAngle;
    private float distanceParameter;
    private float scaleX;
    private float scaleY;
    private float hitSize;

    private PointF particlePosition = new PointF();
    private PointF deltaSpeed = new PointF();

    private effectKind effect;

    public ParticleEffects(effectKind effect, float worldAngle, float objectAngle, float effectOffset, float xPos, float yPos, float size, float distanceParameter) {
        this.effect = effect;
        this.worldAngle = worldAngle;
        this.objectAngle = objectAngle;
        this.distanceParameter = distanceParameter;

        int start_time = new Random().nextInt(500);

        particleInitial(effectOffset, xPos, yPos, size);

        this.options[0] += start_time;
    }

    protected abstract void particleInitial(float effectOffset, float xPos, float yPos, float size);




    protected float getWorldAngle() {
        return worldAngle;
    }

    protected float getObjectAngle() {
        return objectAngle;
    }

    protected float getDistanceParameter() {
        return distanceParameter;
    }

    protected PointF getDeltaSpeed() {
        return deltaSpeed;
    }

    protected float getHitSize() {
        return hitSize;
    }

    protected float getScaleX() {
        return scaleX;
    }

    protected float getScaleY() {
        return scaleY;
    }

    protected effectKind getEffect() {
        return effect;
    }




    protected void setInnerColor(float[] innerColor) {
        this.innerColor = innerColor;
    }

    protected void setOuterColor(float[] outerColor) {
        this.outerColor = outerColor;
    }

    protected void setOptions(float[] options) {
        this.options = options;
    }

    protected void setScaleY(float scaleY) {
        this.scaleY = scaleY;
    }

    protected void setDeltaSpeed(PointF deltaSpeed) {
        this.deltaSpeed = deltaSpeed;
    }

    protected void setHitSize(float hitSize) {
        this.hitSize = hitSize;
    }

    protected void setParticlePosition(PointF particlePosition) {
        this.particlePosition = particlePosition;
    }




    protected void addTime(float time) {
        options[0] += time;
    }

    protected void changeVisibility(float visibility) {
        options[1] += visibility;
    }

    protected void changeScaleX(float scale) {
        this.scaleX += scale;
    }

    protected void changeScaleY(float scale) {
        this.scaleY += scale;
    }

    protected void changeOpacity(float innerOpacity, float outerOpacity) {
        innerColor[3] += innerOpacity;
        outerColor[3] += outerOpacity;
    }




    protected final static float[] WHITE = {1.0f, 1.0f, 1.0f, 0.9f};
    protected final static float[] GRAY = {0.85f, 0.85f, 0.85f, 0.9f};
    protected final static float[] LIGHT_GRAY = {0.6f, 0.6f, 0.6f, 0.9f};
    protected final static float[] LIGHT_DUST_GRAY = {0.5f, 0.5f, 0.5f, 0.9f};
    protected final static float[] EXHAUST_LIGHT_BLUE = {0.3f, 0.4f, 0.5f, 0.9f};
    protected final static float[] EXHAUST_DARK_GRAY = {0.3f, 0.3f, 0.3f, 0.9f};
    protected final static float[] RED = {1.0f, 0.0f, 0.0f, 0.75f};
    protected final static float[] YELLOW = {1.0f, 1.0f, 0.0f, 0.9f};
    protected final static float[] LIGHT_RED = {1.0f, 0.25f, 0.15f, 0.9f};
    protected final static float[] LIGHT_YELLOW = {1.0f, 0.8f, 0.3f, 0.9f};
    protected final static float[] BLACK = {0.3f, 0.3f, 0.3f, 0.4f};
    protected final static float[] GREEN = {0.0f, 1.0f, 0.0f, 0.9f};
    protected final static float[] BLUE = {0.0f, 0.2f, 1.0f, 1.0f};
    protected final static float[] MEDIUM_BLACK = {0.45f, 0.45f, 0.45f, 0.9f};

    //shader options:
    //{time, visibility, size_mod, reload_status}
    //time - use for animation, below 0 affects noise
    //visibility - increasing cause colors more intensive
    //size_mode - makes "egg shape"
    //reload_status - fill texture with innerColor from 0 to 1
    protected final static float[] CANNON_FIRE = {0.0f, 2.6f, 1.4f, 0.0f};
    protected final static float[] GRAY_SMOKE = {0.0f, 3.0f, 0.0f, 0.0f};
    protected final static float[] RELOAD_STATUS = {0.0f, 1.0f, 0.0f, 1.0f};
    protected final static float[] SHEL_STREAK = {-1000.0f, 3.0f, 0.0f, 0.0f};
    protected final static float[] TANK_EXHAUST = {0.0f, 1.0f, 2.0f, 0.0f};
    protected final static float[] TRACK_DUST = {0.0f, 1.0f, 4.0f, 0.0f};
    protected final static float[] HIT_SPARK = {0.0f, 2.0f, 4.0f, 0.0f};
    protected final static float[] TANK_EXPLOSION = {0.0f, 5.0f, 0.0f, 0.0f};
    protected final static float[] TANK_EXPLOSION_SMOKE = {0.0f, 4.0f, 0.0f, 0.0f};






    public float[] getModelMatrix() {
        return modelMatrix;
    }

    public float[] getInnerColor() {
        return innerColor;
    }

    public float[] getOuterColor() {
        return outerColor;
    }

    public float[] getOptions() {
        return options;
    }

    public float getInnerOpacity() {
        return innerColor[3];
    }

    public float getVisibility() {
        return options[1];
    }

    public PointF getParticlePosition() {
        return particlePosition;
    }




    public void setVisibility(float visibility) {
        options[1] = visibility;
    }

    public void setScaleX(float scaleX) {
        this.scaleX = scaleX;
    }



    public enum effectKind {
        CANNON_FIRE, CANNON_SMOKE, RELOAD_STATUS, SHELL_STREAK, TANK_EXHAUST, TRACK_DUST, HIT_SPARK, RADAR_BACKGROUND, TOWER_DOT, ENEMY_DOT, HIT_POINTS,
        TANK_EXPLOSION, TANK_EXPLOSION_SMOKE
    }
}
