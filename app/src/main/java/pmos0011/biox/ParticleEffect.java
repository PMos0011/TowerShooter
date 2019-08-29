package pmos0011.biox;

public class ParticleEffect {

    private float[] modelMatrix = new float[16];
    private float[] innerColor;
    private float[] outerColor;
    private float[] options;

    public ParticleEffect(float[] innerColor, float[] outerColor, float[] options) {
        this.innerColor = innerColor;
        this.outerColor = outerColor;
        this.options = options;
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

    public final static float[] WHITE = {1.0f, 1.0f, 1,0f, 1.0f};
    public final static float[] RED = {1.0f, 0.0f, 0.0f, 1.0f};

    public final static float[] RELOAD ={1.0f, 1.0f, 1.0f, 1.0f};


}

