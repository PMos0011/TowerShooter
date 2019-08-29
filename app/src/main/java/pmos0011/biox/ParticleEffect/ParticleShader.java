package pmos0011.biox.ParticleEffect;

import android.content.Context;

import pmos0011.biox.AbstractClasses.Shader;

public class ParticleShader extends Shader {

    int uniformBlockProjectionMatrixIndex;

    public ParticleShader(Context context, int vertexFileID, int fragmentFileID) {
        super(context, vertexFileID, fragmentFileID);
    }

    @Override
    protected void bindAttributes() {
        super.bindAttributes(0, "vPosition");
        super.bindAttributes(1, "vTextureCoords");
        super.bindAttributes(2, "vModelMatrix");
        super.bindAttributes(6, "vInnerColor");
        super.bindAttributes(7, "vOuterColor");
        super.bindAttributes(8, "vOptions");
    }

    @Override
    protected void getAllUniformsHandle() {
    }

    @Override
    protected void getAllUniformBlocksIndex() {
        uniformBlockProjectionMatrixIndex =super.getUniformBlockIndex("projectionMatrix");
        super.uniformBlockBinding(uniformBlockProjectionMatrixIndex,0);
    }
}
