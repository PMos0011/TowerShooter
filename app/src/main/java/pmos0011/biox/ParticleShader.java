package pmos0011.biox;

import android.content.Context;
import android.util.Log;

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
        super.bindAttributes(6, "innerColor");
        super.bindAttributes(7, "outerColor");
        super.bindAttributes(8, "options");
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
