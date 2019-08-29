package pmos0011.biox.AbstractClasses;

import java.util.Random;

import pmos0011.biox.CommonObjects.Transformations;

public abstract class ParticleEffects {

    private float[] modelMatrix = new float[16];
    private float[] innerColor = new float[4];
    private float[] outerColor= new float[4];
    private float[] options = new float[4];

    private effectKind effect;

    public ParticleEffects(effectKind effect, float wordAngle, float objectAngle, float xPos, float yPos) {
        this.effect = effect;

        Transformations.setModelTranslation(modelMatrix, wordAngle, objectAngle, xPos, yPos, 0.3f, 0.3f);

        int start_time = new Random().nextInt(500);

        switch (effect) {
            case FIRE:
                this.innerColor = YELLOW.clone();
                this.outerColor = RED.clone();
                this.options = CANNON_FIRE.clone();
                break;
            case SMOKE:
                this.innerColor = WHITE.clone();
                this.outerColor = WHITE.clone();
                this.options = CANNON_FIRE.clone();
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

    protected void addTime(float time){
        options[0]+=time;
    }

    public final static float[] WHITE = {1.0f, 1.0f, 1.0f, 1.0f};
    public final static float[] RED = {1.0f, 0.0f, 0.0f, 1.0f};
    public final static float[] YELLOW = {0.8f, 0.8f, 0.0f, 1.0f};

    public final static float[] CANNON_FIRE = {1.0f, 4.0f, 1.0f, 1.0f};

    public enum effectKind {
        FIRE, SMOKE
    }
}
