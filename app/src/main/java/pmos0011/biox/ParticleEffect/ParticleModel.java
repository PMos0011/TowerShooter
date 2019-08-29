package pmos0011.biox.ParticleEffect;

import android.opengl.GLES31;
import android.util.Log;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import pmos0011.biox.CommonObjects.BitmapID;
import pmos0011.biox.CommonObjects.ObjectsLoader;
import pmos0011.biox.AbstractClasses.StaticModel;
import pmos0011.biox.StaticTextures.StaticTextures;

public class ParticleModel extends StaticModel {

    public final static int PARTICLE_DATA_LENGTH = 28;

    private List<FireParticleEffect> fireParticleEffects = new ArrayList<>();

    private float[] modelMatrices;
    private final int VBO;
    private int counter;

    public ParticleModel(int vaoID, int particlesMaxCount, int vboID) {
        super(vaoID);

        this.modelMatrices = new float[particlesMaxCount * PARTICLE_DATA_LENGTH];
        this.VBO = vboID;
        this.counter = 0;
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

        Iterator<FireParticleEffect> particleEffectIterator = fireParticleEffects.iterator();
        while (particleEffectIterator.hasNext()) {
            FireParticleEffect effect = particleEffectIterator.next();
            updateParticleMatrix(effect);
            effect.particleUpdate();
        }

        loader.updateVBOMatrix(VBO, modelMatrices);

        GLES31.glEnable(GLES31.GL_BLEND);
        GLES31.glBlendFunc(GLES31.GL_SRC_ALPHA, GLES31.GL_ONE);
        GLES31.glBindTexture(GLES31.GL_TEXTURE_2D, loader.getTextureID(BitmapID.textureNames.LEFT_ARROW.getValue()));
        GLES31.glDrawElementsInstanced(GLES31.GL_TRIANGLES, StaticTextures.DRAW_ORDER.length, GLES31.GL_UNSIGNED_SHORT, 0, 2);

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

    public void updateParticleMatrix(FireParticleEffect effect) {

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

    public void addPArticleEffect(FireParticleEffect effect) {
        fireParticleEffects.add(effect);
    }

}
