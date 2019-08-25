package pmos0011.biox;

import android.content.Context;

public class TextureShader extends Shader {

    public static int projectionMatrixHandle;
    public static int modelMatrixHandle;

    public TextureShader(Context context, int vertexFileID, int fragmentFileID) {
        super(context, vertexFileID, fragmentFileID);
    }

    @Override
    protected void bindAttributes() {
        super.bindAttributes(0, "vPosition");
        super.bindAttributes(1, "vTextureCoords");

    }

    @Override
    protected void getAllUniformsHandle() {
        projectionMatrixHandle=super.getUniformHandle("vProjectionMatrix");
        modelMatrixHandle=super.getUniformHandle("vModelMatrix");
    }

}
