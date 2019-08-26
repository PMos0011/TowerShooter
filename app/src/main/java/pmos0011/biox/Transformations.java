package pmos0011.biox;

import android.opengl.Matrix;

public class Transformations {

    private TextureModel textureModel;
    public static float[] projectionMatrix = new float[16];
    public static float[] model1 = new float[16];
    public static float[] model2 = new float[16];
    public static float[] modelMatrix = new float[32];
    public static float[] testMatrix = new float[16];

    public Transformations(TextureModel textureModel, float ratio) {
        this.textureModel = textureModel;
        Matrix.frustumM(projectionMatrix, 0, -ratio, ratio, -1.0f, 1.0f, 1.0f, 2.0f);

        Matrix.setIdentityM(model1, 0);
        Matrix.translateM(model1, 0, 0.5f, 0, -1.01f);
        Matrix.scaleM(model1,0,0.5f,0.5f,1.0f);

        Matrix.setIdentityM(model2, 0);
        Matrix.translateM(model2, 0, -0.5f, 0, -1.01f);
        Matrix.scaleM(model2,0,0.5f,0.5f,1.0f);

        for (int i=0;i<16;i++){
            modelMatrix[i]=model1[i];
            modelMatrix[i+16]=model2[i];
        }

        //Matrix.multiplyMM(projectionMatrix,0,testMatrix,0,modelMatrix,0);
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
