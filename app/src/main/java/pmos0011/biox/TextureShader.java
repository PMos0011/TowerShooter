package pmos0011.biox;

import android.content.Context;
import android.util.Log;

public class TextureShader extends Shader {

    public static int projectionMatrixHandle;
    public static int modelMatrixHandle;
    int uniformBlockProjectionMatrixIndex;

    public TextureShader(Context context, int vertexFileID, int fragmentFileID) {
        super(context, vertexFileID, fragmentFileID);
    }

    @Override
    protected void bindAttributes() {
        super.bindAttributes(0, "vPosition");
        super.bindAttributes(1, "vTextureCoords");
        super.bindAttributes(2, "vModelMatrix");

    }

    @Override
    protected void getAllUniformsHandle() {
        //projectionMatrixHandle=super.getUniformHandle("vProjectionMatrix");
        //modelMatrixHandle=super.getUniformHandle("vModelMatrix");
    }

    @Override
    protected void getAllUniformBlocksIndex() {
        uniformBlockProjectionMatrixIndex =super.getUniformBlockIndex("projectionMatrix");
        super.uniformBlockBinding(uniformBlockProjectionMatrixIndex,0);

    }

}
