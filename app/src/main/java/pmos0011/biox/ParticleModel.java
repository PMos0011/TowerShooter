package pmos0011.biox;

import android.opengl.GLES31;
import android.util.Log;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ParticleModel extends StaticModel {

    public final static int PARTICLE_DATA_LENGHT = 28;

    private List<ParticleEffect> particleEffects;

    private float[] modelMatrices;
    private final int VBO;
    private int counter;

    public ParticleModel(int vaoID, int particlesMaxCount, int vboID) {
        super(vaoID);

        this.modelMatrices = new float[particlesMaxCount * PARTICLE_DATA_LENGHT];
        this.VBO = vboID;
        this.counter = 0;
        this.particleEffects = new ArrayList<>();

    }

    @Override
    protected void enableVertexArrays() {
        for (int i = 0; i < 9; i++)
            super.enableVertexArray(i);
    }

    @Override
    protected void drawElements(ObjectsLoader loader) {
        particleShader.start();
        resetCounter();

        Iterator<ParticleEffect> particleEffectIterator = particleEffects.iterator();
        while (particleEffectIterator.hasNext()) {
            ParticleEffect effect = particleEffectIterator.next();
            updateParticleMatrix(effect);
        }

        loader.updateVBOMatrix(VBO, modelMatrices);

        GLES31.glEnable(GLES31.GL_BLEND);
        GLES31.glBlendFunc(GLES31.GL_SRC_ALPHA, GLES31.GL_ONE_MINUS_SRC_ALPHA);
        GLES31.glBindTexture(GLES31.GL_TEXTURE_2D, loader.getTextureID(BitmapID.textureNames.LEFT_ARROW.getValue()));
        GLES31.glDrawElementsInstanced(GLES31.GL_TRIANGLES, StaticTextures.DRAW_ORDER.length, GLES31.GL_UNSIGNED_SHORT, 0, particleEffects.size());

        GLES31.glDisable(GLES31.GL_BLEND);
        particleShader.stop();

    }

    @Override
    protected void disableVertexArrays() {
        for (int i = 0; i < 9; i++)
            super.disableVertexArray(i);

    }

    public void resetCounter() {
        counter = 0;
    }

    public void updateParticleMatrix(ParticleEffect effect) {

        for (int i = 0; i < 16; i++) {
            modelMatrices[counter] = effect.getModelMatrix()[i];
            counter++;
        }
        for (int i = 0; i < 4; i++) {
            modelMatrices[counter] = effect.getInnerColor()[i];
            counter++;
        }
        for (int i = 0; i < 4; i++) {
            modelMatrices[counter] = effect.getOuterColor()[i];
            counter++;
        }
        for (int i = 0; i < 4; i++) {
            modelMatrices[counter] = effect.getOptions()[i];
            counter++;
        }
    }

    public void addPArticleEffect(ParticleEffect effect) {
        particleEffects.add(effect);
    }

}
