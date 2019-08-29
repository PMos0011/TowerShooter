package pmos0011.biox;

import android.opengl.GLES31;

public abstract class StaticModel {

    protected StaticShader staticShader;
    protected ParticleShader particleShader;
    protected int vao;

    public final static float SQUERE_CORDS[] = {
            -1.0f, 1.0f,
            -1.0f, -1.0f,
            1.0f, -1.0f,
            1.0f, 1.0f
    };

    public final static short DRAW_ORDER[] = {
            0, 1, 2,
            0, 2, 3
    };

    public final static float TEXTURE_COORDS[] = {
            0.0f, 1.0f,
            0.0f, 0.0f,
            1.0f, 0.0f,
            1.0f, 1.0f
    };

    public final static int COORDS_PER_VERTEX = 2;

    public StaticModel(int vaoID) {
        this.vao = vaoID;
    }

    public void setStaticShader(StaticShader staticShader) {
        this.staticShader = staticShader;
    }

    public void setParticleShader(ParticleShader particleShader) {
        this.particleShader = particleShader;
    }

    public void drawClassElements(ObjectsLoader loader, Transformations transformations) {
        GLES31.glBindVertexArray(vao);
        enableVertexArrays();
        GLES31.glActiveTexture(GLES31.GL_TEXTURE0);
        drawElements(loader, transformations);
        disableVertexArrays();
        GLES31.glBindVertexArray(0);
    }


    protected abstract void enableVertexArrays();

    protected abstract void drawElements(ObjectsLoader loader, Transformations transformations);

    protected abstract void disableVertexArrays();

    protected void enableVertexArray(int index) {
        GLES31.glEnableVertexAttribArray(index);
    }

    protected void disableVertexArray(int index) {
        GLES31.glDisableVertexAttribArray(index);
    }

}
