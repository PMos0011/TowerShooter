package pmos0011.biox.AbstractClasses;

import android.graphics.PointF;

import java.util.Random;

public abstract class ParticleEffects {

    public final static int PARTICLE_DATA_LENGTH = 28;
    public final static int PARTICLE_MAX_COUNT = 100;

    protected float[] modelMatrix = new float[16];
    protected float[] innerColor = new float[4];
    protected float[] outerColor = new float[4];
    protected float[] options = new float[4];
    protected float worldAngle;
    protected float objectAngle;
    protected float travelDistance;
    protected float scaleX;
    protected float scaleY;

    protected PointF particlePosition = new PointF();

    protected effectKind effect;

    public ParticleEffects(effectKind effect, float worldAngle, float objectAngle, float effectOffset, float xPos, float yPos, float size, float travelDistance) {
        this.effect = effect;
        this.worldAngle = worldAngle;
        this.objectAngle = objectAngle;
        this.travelDistance = travelDistance;

        int start_time = new Random().nextInt(500);

        particleInitial(effectOffset, xPos, yPos, size);

        this.options[0] += start_time;
    }

    protected abstract void particleInitial(float effectOffset, float xPos, float yPos, float size);

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

    public PointF getParticlePosition() {
        return particlePosition;
    }

    public float getScaleX() {
        return scaleX;
    }

    public float getScaleY() {
        return scaleY;
    }

    protected void addTime(float time) {
        options[0] += time;
    }

    protected void changeVisibility(float visibility) {
        options[1] += visibility;
    }

    public float getVisibility() {
        return options[1];
    }

    protected void changeOpacity(float innerOpacity, float outerOpacity) {
        innerColor[3] += innerOpacity;
        outerColor[3] += outerOpacity;
    }

    public float getInnerOpacity() {
        return innerColor[3];
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

    public enum effectKind {
        CANNON_FIRE, CANNON_SMOKE, RELOAD_STATUS, SHELL_STREAK, TANK_EXHAUST, TRACK_DUST
    }
}
