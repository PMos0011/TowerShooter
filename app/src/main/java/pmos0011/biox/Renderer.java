package pmos0011.biox;

import android.opengl.GLES31;

public class Renderer {

    public void draw(Transformations transformations, GameObjectsLoader loader){
        GLES31.glBindVertexArray(transformations.getTextureModel().getVao());
        enableVertexArrays(5);
        //loader.loadMatrixUniform(TextureShader.projectionMatrixHandle,transformations.getProjectionMatrix());
        //loader.loadMatrixUniform(TextureShader.modelMatrixHandle,transformations.getModelMatrix());
        GLES31.glActiveTexture(GLES31.GL_TEXTURE0);
        GLES31.glBindTexture(GLES31.GL_TEXTURE_2D,loader.getTextureID(BitmapID.textureNames.BACKGROUND.getValue()));
        GLES31.glDrawElementsInstanced(GLES31.GL_TRIANGLES, TextureModel.DRAW_ORDER.length,GLES31.GL_UNSIGNED_SHORT,0,2);
        disableVertexArrays(5);
        GLES31.glBindVertexArray(0);
    }

    private void enableVertexArrays(int arrays){
        for (int i= 0; i<=arrays; i++)
            GLES31.glEnableVertexAttribArray(i);
    }

    private void disableVertexArrays(int arrays){
        for (int i= 0; i<=arrays; i++)
            GLES31.glDisableVertexAttribArray(i);

    }
}
