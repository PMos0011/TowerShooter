package pmos0011.biox;

import android.opengl.GLES31;

public class Renderer {

    public void draw(Transformations transformations, GameObjectsLoader loader){
        GLES31.glBindVertexArray(transformations.getTextureModel().getVao());
        GLES31.glEnableVertexAttribArray(0);
        GLES31.glEnableVertexAttribArray(1);
        loader.loadMatrixUniform(TextureShader.projectionMatrixHandle,transformations.getProjectionMatrix());
        loader.loadMatrixUniform(TextureShader.modelMatrixHandle,transformations.getModelMatrix());
        GLES31.glActiveTexture(GLES31.GL_TEXTURE0);
        GLES31.glBindTexture(GLES31.GL_TEXTURE_2D,loader.getTextureID(BitmapID.textureNames.BACKGROUND.getValue()));
        GLES31.glDrawElements(GLES31.GL_TRIANGLES, TextureModel.DRAW_ORDER.length,GLES31.GL_UNSIGNED_SHORT,0);
        GLES31.glDisableVertexAttribArray(0);
        GLES31.glDisableVertexAttribArray(1);
        GLES31.glBindVertexArray(0);
    }
}
