package pmos0011.biox.AbstractClasses;

import android.graphics.PointF;

import java.util.Random;

public abstract class ParticleEffects {

    public final static int PARTICLE_DATA_LENGTH = 28;
    public final static int PARTICLE_MAX_COUNT = 100;

    private float[] modelMatrix = new float[16];
    private float[] innerColor = new float[4];
    private float[] outerColor = new float[4];
    private float[] options = new float[4];
    private float worldAngle;
    private float objectAngle;

    protected  PointF particlePosition = new PointF();

    private effectKind effect;

    public ParticleEffects(effectKind effect, float worldAngle, float objectAngle, float effectOffset, float xPos, float yPos) {
        this.effect = effect;
        this.worldAngle = worldAngle;
        this.objectAngle = objectAngle;

        int start_time = new Random().nextInt(500);

        switch (effect) {
            case CANNON_FIRE:
                this.innerColor = LIGHT_YELLOW.clone();
                this.outerColor = LIGHT_RED.clone();
                this.options = CANNON_FIRE.clone();
                break;
            case CANNON_SMOKE:
                this.innerColor = GRAY.clone();
                this.outerColor = GRAY.clone();
                this.options = GRAY_SMOKE.clone();
                this.innerColor[3]=0.01f;
                this.outerColor[3]=0.0f;
                break;
        }

        this.options[0] += start_time;
    }

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

    public effectKind getEffect() {
        return effect;
    }

    public float getWorldAngle() {
        return worldAngle;
    }

    public float getObjectAngle() {
        return objectAngle;
    }

    protected void addTime(float time) {
        options[0] += time;
    }

    protected void changeVisibility(float visibility) {
        options[1] += visibility;
    }

    public float getVisibility(){
        return  options[1];
    }

    protected void changeOpacity(float innerOpacity, float outerOpacity){
        innerColor[3]+=innerOpacity;
        outerColor[3]+=outerOpacity;
    }

    public float getInnerOpacity(){
        return innerColor[3];
    }

    private final static float[] WHITE = {1.0f, 1.0f, 1.0f, 0.9f};
    private final static float[] GRAY = {0.85f, 0.85f, 0.85f, 0.9f};
    private final static float[] LIGHT_GRAY = {0.6f, 0.6f, 0.6f, 0.9f};
    private final static float[] RED = {1.0f, 0.0f, 0.0f, 0.9f};
    private final static float[] YELLOW = {1.0f, 1.0f, 0.0f, 0.9f};
    private final static float[] LIGHT_RED = {1.0f, 0.25f, 0.15f, 0.9f};
    private final static float[] LIGHT_YELLOW = {1.0f, 0.8f, 0.3f, 0.9f};

    private final static float[] CANNON_FIRE = {0.0f, 2.6f, 1.4f, 0.0f};
    private final static float[] GRAY_SMOKE = {0.0f, 3.0f, 0.0f, 0.0f};

    public enum effectKind {
        CANNON_FIRE, GRAY_SMOKE, CANNON_SMOKE,
    }
}
