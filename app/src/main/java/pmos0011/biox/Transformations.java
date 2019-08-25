package pmos0011.biox;

import android.opengl.Matrix;

public class Transformations {

    private TextureModel textureModel;
    public static float[] projectionMatrix = new float[16];
    public static float[] modelMatrix = new float[16];

    public Transformations(TextureModel textureModel, float ratio) {
        this.textureModel = textureModel;
        Matrix.frustumM(projectionMatrix, 0, -ratio, ratio, -1.0f, 1.0f, 1.0f, 2.0f);

        Matrix.setIdentityM(modelMatrix, 0);
        Matrix.translateM(modelMatrix, 0, 0, 0, -1.01f);
    }

    public TextureModel getTextureModel() {
        return textureModel;
    }

    public float[] getProjectionMatrix() {
        return projectionMatrix;
    }

    public float[] getModelMatrix() {
        return modelMatrix;
    }
}
