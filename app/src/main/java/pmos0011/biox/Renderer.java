package pmos0011.biox;

import android.opengl.GLES31;

public class Renderer {

    public void draw(Texture texture){
        GLES31.glBindVertexArray(texture.getVao());
        GLES31.glEnableVertexAttribArray(0);
        GLES31.glDrawElements(GLES31.GL_TRIANGLES, Texture.DRAW_ORDER.length,GLES31.GL_UNSIGNED_INT,0);
        GLES31.glDisableVertexAttribArray(0);
        GLES31.glBindVertexArray(0);
    }
}
