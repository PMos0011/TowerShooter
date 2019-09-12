package pmos0011.biox.ParticleEffect;

import android.opengl.GLES31;
import android.util.Log;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import pmos0011.biox.AbstractClasses.ParticleEffects;
import pmos0011.biox.CommonObjects.BitmapID;
import pmos0011.biox.CommonObjects.ObjectsLoader;
import pmos0011.biox.AbstractClasses.StaticModel;
import pmos0011.biox.StaticTextures.StaticTextures;

public class ParticleModel extends StaticModel {

    private List<FireParticleEffect> fireParticleEffects = new ArrayList<>();
    private List<SmokeParticleEffect> smokeParticleEffects = new ArrayList<>();

    private float[] modelMatrices;
    private final int VBO;
    private int pointer;
    private int counter;

    public ParticleModel(int vaoID, int vboID) {
        super(vaoID);

        this.modelMatrices = new float[ParticleEffects.PARTICLE_MAX_COUNT * ParticleEffects.PARTICLE_DATA_LENGTH];
        this.VBO = vboID;
        this.pointer = 0;
    }

    @Override
    protected void enableVertexArrays() {
        for (counter = 0; counter < 9; counter++)
            super.enableVertexArray(counter);
    }

    @Override
    protected void drawElements(ObjectsLoader loader) {
        particleShader.start();
        GLES31.glBindTexture(GLES31.GL_TEXTURE_2D, loader.getTextureID(BitmapID.textureNames.LEFT_ARROW.getValue()));

        updateSmokeParticleEffects(loader);
        drawSmokeParticleEffects();

        updateFireParticleEffects(loader);
        drawFireParticleEffects();

        particleShader.stop();
    }

    @Override
    protected void disableVertexArrays() {
        for (int counter = 0; counter < 9; counter++)
            super.disableVertexArray(counter);
    }

    public void resetCounter() {
        pointer = 0;
    }

    private void updateParticleMatrix(FireParticleEffect effect) {

        for (counter = 0; counter < 16; counter++) {
            modelMatrices[pointer] = effect.getModelMatrix()[counter];
            pointer++;
        }
        for (counter = 0; counter < 4; counter++) {
            modelMatrices[pointer] = effect.getInnerColor()[counter];
            pointer++;
        }
        for (counter = 0; counter < 4; counter++) {
            modelMatrices[pointer] = effect.getOuterColor()[counter];
            pointer++;
        }
        for (counter = 0; counter < 4; counter++) {
            modelMatrices[pointer] = effect.getOptions()[counter];
            pointer++;
        }
    }

    private void updateParticleMatrix(SmokeParticleEffect effect) {

        for (counter = 0; counter < 16; counter++) {
            modelMatrices[pointer] = effect.getModelMatrix()[counter];
            pointer++;
        }
        for (counter = 0; counter < 4; counter++) {
            modelMatrices[pointer] = effect.getInnerColor()[counter];
            pointer++;
        }
        for (counter = 0; counter < 4; counter++) {
            modelMatrices[pointer] = effect.getOuterColor()[counter];
            pointer++;
        }
        for (counter = 0; counter < 4; counter++) {
            modelMatrices[pointer] = effect.getOptions()[counter];
            pointer++;
        }
    }

    public void addParticleEffect(FireParticleEffect effect) {

        if (fireParticleEffects.size() < ParticleEffects.PARTICLE_MAX_COUNT)
            fireParticleEffects.add(effect);
    }

    public void addParticleEffect(SmokeParticleEffect effect) {
        if (smokeParticleEffects.size() < ParticleEffects.PARTICLE_MAX_COUNT)
            smokeParticleEffects.add(effect);
    }

    private void updateFireParticleEffects(ObjectsLoader loader) {

        resetCounter();
        try {
            Iterator<FireParticleEffect> particleEffectIterator = fireParticleEffects.iterator();
            while (particleEffectIterator.hasNext()) {
                FireParticleEffect effect = particleEffectIterator.next();
                updateParticleMatrix(effect);
                effect.particleUpdate();

                if (effect.getVisibility() <= 0.0f)
                    particleEffectIterator.remove();
            }
        } catch (Exception e) {

        }

        loader.updateVBOMatrix(VBO, modelMatrices);
    }

    private void drawFireParticleEffects() {

        GLES31.glEnable(GLES31.GL_BLEND);
        GLES31.glBlendFunc(GLES31.GL_SRC_ALPHA, GLES31.GL_ONE);
        GLES31.glDrawElementsInstanced(GLES31.GL_TRIANGLES, StaticTextures.DRAW_ORDER.length, GLES31.GL_UNSIGNED_SHORT, 0, fireParticleEffects.size());
        GLES31.glDisable(GLES31.GL_BLEND);
    }

    private void updateSmokeParticleEffects(ObjectsLoader loader) {

        resetCounter();
        try {
            Iterator<SmokeParticleEffect> particleEffectIterator = smokeParticleEffects.iterator();
            while (particleEffectIterator.hasNext()) {
                SmokeParticleEffect effect = particleEffectIterator.next();
                updateParticleMatrix(effect);
                effect.particleUpdate();

                if (effect.getVisibility() <= 0.0f || effect.getInnerOpacity() <= 0.0f)
                    particleEffectIterator.remove();
            }
        } catch (Exception e) {

        }
        loader.updateVBOMatrix(VBO, modelMatrices);
    }

    private void drawSmokeParticleEffects() {

        GLES31.glEnable(GLES31.GL_BLEND);
        GLES31.glBlendFunc(GLES31.GL_SRC_ALPHA, GLES31.GL_ONE_MINUS_SRC_ALPHA);
        GLES31.glDrawElementsInstanced(GLES31.GL_TRIANGLES, StaticTextures.DRAW_ORDER.length, GLES31.GL_UNSIGNED_SHORT, 0, smokeParticleEffects.size());
        GLES31.glDisable(GLES31.GL_BLEND);
    }

    public void drawRadar() {
        addParticleEffect(new SmokeParticleEffect(ParticleEffects.effectKind.RADAR_BACKGROUND, 0, 0, 0,
                0, 1, ParticleEffects.RADAR_SIZE, 0));
        addParticleEffect(new SmokeParticleEffect(ParticleEffects.effectKind.TOWER_DOT, 0, 0, 0,
                0, 1, ParticleEffects.RADAR_SIZE, 0));
    }
}
