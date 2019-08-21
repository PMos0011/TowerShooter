package TextCreator;

import android.graphics.PointF;
import android.opengl.GLES31;
import android.opengl.Matrix;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

import pmos0011.TowerShooter.GamePlayRenderer;
import pmos0011.TowerShooter.ShadersManager;
import pmos0011.TowerShooter.Texture;

public class FontRenderer extends Texture {

    public static final float PIXEL_SIZE = 1.0f / 1024.0f;
    public static final float PADDING = 5 * PIXEL_SIZE;

    private Map<Integer, Characters> charactersData = new HashMap<Integer, Characters>();

    public void addCharacterData(Characters charDat) {
        charactersData.put(charDat.getId(), charDat);
    }

    public Characters getCharacter(int id) {
        return charactersData.get(id);
    }


    public void draw(float[] mModelMatrix, Characters c, int textureHandle) {


        GLES31.glUseProgram(ShadersManager.TEXTURE_PROGRAM_HANDLE);

        float colour[] = {1.0f, 0.5f, 0.5f, 0.8f};
        GLES31.glUniform4fv(ShadersManager.textureColorHandle, 1, colour, 0);

        GLES31.glUniform1f(GLES31.glGetUniformLocation(ShadersManager.TEXTURE_PROGRAM_HANDLE, "isFont"), 1);
        GLES31.glUniform2f(GLES31.glGetUniformLocation(ShadersManager.TEXTURE_PROGRAM_HANDLE, "transition"), c.getWith(), c.getHeight());
        GLES31.glUniform2f(GLES31.glGetUniformLocation(ShadersManager.TEXTURE_PROGRAM_HANDLE, "movement"), c.getxPosition(), c.getYPosition());

        GLES31.glEnable(GLES31.GL_BLEND);
        GLES31.glBlendFunc(GLES31.GL_SRC_ALPHA, GLES31.GL_ONE);

        loadOpenGLVariables(mModelMatrix, textureHandle);

        GLES31.glDisable(GLES31.GL_BLEND);

    }

    public void writeText(float[] modelMatrix, float xPosition, float yPosition, String textToWrite, float size, int textreHandle) {

        float xTransition = 0.0f;

        char[] charToWrite = textToWrite.toCharArray();
        for (char c : charToWrite) {
            Characters character = getCharacter(c);
            Matrix.setIdentityM(modelMatrix, 0);
            Matrix.translateM(modelMatrix, 0, xPosition + xTransition, yPosition + character.getyOffset(), GamePlayRenderer.Z_DIMENSION);
            Matrix.scaleM(modelMatrix, 0, size * character.getAspect(), size, 1);
            draw(modelMatrix, character, textreHandle);
            xTransition += character.getAdvance()*10*size;
        }
    }

}
