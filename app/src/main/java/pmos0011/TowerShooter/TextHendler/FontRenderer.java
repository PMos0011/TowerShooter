package pmos0011.TowerShooter.TextHendler;

import android.opengl.GLES31;

import java.util.HashMap;
import java.util.Map;

import pmos0011.TowerShooter.AbstractClasses.StaticModel;
import pmos0011.TowerShooter.CommonObjects.BitmapID;
import pmos0011.TowerShooter.CommonObjects.ObjectsLoader;
import pmos0011.TowerShooter.CommonObjects.Transformations;
import pmos0011.TowerShooter.StaticTextures.StaticTexturesRenderer;


public class FontRenderer extends StaticModel {

    public static final int LETTERS_MAX_COUNT = 100;
    public static final int LETTERS_DATA_SIZE = 24;
    public static final float PIXEL_SIZE = 1.0f / 1024.0f;
    public static final float PADDING = 5 * PIXEL_SIZE;
    public static final float[] GREEN_FONT_COLOR = {1.0f, 0.5f, 0.5f, 0.8f};

    private FontShader fontShader;
    private int vboID;
    private float[] modelMatrices;
    private float[] charModelMatrix;
    private int pointer;
    private int counter;
    private int lettersCount;
    private float xTransition;

    private Map<Integer, Characters> charactersData = new HashMap<Integer, Characters>();

    public FontRenderer(int vaoID, int vboID) {
        super(vaoID);

        this.vboID = vboID;
        this.modelMatrices = new float[LETTERS_MAX_COUNT * LETTERS_DATA_SIZE];
        this.pointer = 0;
        this.lettersCount = 0;

        this.charModelMatrix = new float[16];
    }

    @Override
    protected void enableVertexArrays() {
        for (counter = 0; counter < 9; counter++)
            super.enableVertexArray(counter);
    }

    @Override
    protected void drawElements(ObjectsLoader loader) {
        fontShader.start();
        loader.updateVBOMatrix(vboID, modelMatrices);
        GLES31.glBindTexture(GLES31.GL_TEXTURE_2D, loader.getTextureID(BitmapID.textureNames.FONT_MAP.getValue()));
        drawText();
        fontShader.stop();
    }

    @Override
    protected void disableVertexArrays() {
        for (counter = 0; counter < 9; counter++)
            super.disableVertexArray(counter);
    }

    private void drawText() {
        GLES31.glEnable(GLES31.GL_BLEND);
        GLES31.glBlendFunc(GLES31.GL_SRC_ALPHA, GLES31.GL_ONE);
        GLES31.glDrawElementsInstanced(GLES31.GL_TRIANGLES, StaticTexturesRenderer.DRAW_ORDER.length, GLES31.GL_UNSIGNED_SHORT, 0, lettersCount);
        GLES31.glDisable(GLES31.GL_BLEND);
        resetCounter();
    }

    private void updateModelMatrix(float[] charModelMatrix, float transition0, float transition1, float movement0, float movement1, float[] color) {

        for (counter = 0; counter < 16; counter++) {
            modelMatrices[pointer] = charModelMatrix[counter];
            pointer++;
        }
        modelMatrices[pointer] = transition0;
        pointer++;
        modelMatrices[pointer] = transition1;
        pointer++;
        modelMatrices[pointer] = movement0;
        pointer++;
        modelMatrices[pointer] = movement1;
        pointer++;
        for (counter = 0; counter < 4; counter++) {
            modelMatrices[pointer] = color[counter];
            pointer++;
        }
    }

    private void resetCounter() {
        pointer = 0;
        lettersCount=0;
    }




    public void setFontShader(FontShader fontShader) {
        this.fontShader = fontShader;
    }

    public void addCharacterData(Characters charDat) {
        charactersData.put(charDat.getId(), charDat);
    }

    public Characters getCharacter(int id) {
        return charactersData.get(id);
    }

    public void writeText(float xPosition, float yPosition, float size, float[] color, String textToWrite) {

        xTransition = 0f;
        char[] charToWrite = textToWrite.toCharArray();

        for (char c : charToWrite) {
            if (lettersCount < LETTERS_MAX_COUNT) {
                Characters character = getCharacter(c);
                Transformations.setModelTranslation(charModelMatrix, 0, 0, xPosition + xTransition, yPosition + character.getyOffset(),
                        size * character.getAspect(), size);

                xTransition += character.getAdvance() * 10 * size;

                updateModelMatrix(charModelMatrix, character.getWith(), character.getHeight(),
                        character.getxPosition(), character.getYPosition(), color);
                lettersCount++;

            }
        }
    }
}
