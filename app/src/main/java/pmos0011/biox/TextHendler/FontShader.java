package pmos0011.biox.TextHendler;

import android.content.Context;

import pmos0011.biox.AbstractClasses.Shader;

public class FontShader extends Shader {

    private int uniformBlockProjectionMatrixIndex;

    public FontShader(Context context, int vertexFileID, int fragmentFileID) {
        super(context, vertexFileID, fragmentFileID);
    }

    @Override
    protected void bindAttributes() {
        super.bindAttributes(0, "vPosition");
        super.bindAttributes(1, "vTextureCoords");
        super.bindAttributes(2, "vModelMatrix");
        super.bindAttributes(6, "vTransition");
        super.bindAttributes(7, "vMovement");
        super.bindAttributes(8, "vColor");
    }

    @Override
    protected void getAllUniformsHandle() {

    }

    @Override
    protected void getAllUniformBlocksIndex() {
        uniformBlockProjectionMatrixIndex = super.getUniformBlockIndex("projectionMatrix");
        super.uniformBlockBinding(uniformBlockProjectionMatrixIndex, 0);
    }
}
