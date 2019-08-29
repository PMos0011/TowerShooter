package pmos0011.biox.StaticTextures;

import android.content.Context;

import pmos0011.biox.AbstractClasses.Shader;

public class StaticShader extends Shader {

    private int colorHandle;
    private int modelMatrixHandle;
    private int uniformBlockProjectionMatrixIndex;

    public StaticShader(Context context, int vertexFileID, int fragmentFileID) {
        super(context, vertexFileID, fragmentFileID);
    }

    @Override
    protected void bindAttributes() {
        super.bindAttributes(0, "vPosition");
        super.bindAttributes(1, "vTextureCoords");

    }

    @Override
    protected void getAllUniformsHandle() {
        //colorHandle=super.getUniformHandle("vColor");
        modelMatrixHandle=super.getUniformHandle("vModelMatrix");

    }

    @Override
    protected void getAllUniformBlocksIndex() {
        uniformBlockProjectionMatrixIndex =super.getUniformBlockIndex("projectionMatrix");
        super.uniformBlockBinding(uniformBlockProjectionMatrixIndex,0);

    }

    public int getColorHandle() {
        return colorHandle;
    }

    public int getModelMatrixHandle() {
        return modelMatrixHandle;
    }
}
