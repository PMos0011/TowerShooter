package pmos0011.biox;

import android.opengl.GLES31;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class Square {

    private FloatBuffer vertexBuffer;
    private float[] squareCoords;

    public Square(boolean isLaser){

        if(isLaser) {
            squareCoords = new float[8];
            squareCoords[0] = -GamePlayRenderer.LASER_SIGHT_DISPERSION;
            squareCoords[2] = GamePlayRenderer.LASER_SIGHT_DISPERSION;
        }else {
            float coords = GamePlayRenderer.GAME_CONTROL_OBJECT_SIZE;
            squareCoords=new float[]{
                    -coords, coords,
                    -coords, -coords,
                    coords, -coords,
                    coords, coords,
            };
        }
    }

    public void draw(float[] mModelMatrix, float param, boolean isLaser) {

        if (isLaser) {
            squareCoords[1] = param * 0.85f;
            squareCoords[3] = squareCoords[1];
        } else {
            float coords = GamePlayRenderer.GAME_CONTROL_OBJECT_SIZE;
            float coordMod = coords - 2 * coords * param;

            squareCoords[0]=coordMod;
            squareCoords[2]=coordMod;
        }

        GLES31.glEnable(GLES31.GL_BLEND);
        GLES31.glBlendFunc(GLES31.GL_SRC_ALPHA, GLES31.GL_ONE_MINUS_SRC_ALPHA);

        openGLProgram(mModelMatrix);

        GLES31.glDisable(GLES31.GL_BLEND);

    }

    private void openGLProgram(float[] mModelMatrix) {

        ByteBuffer bb = ByteBuffer.allocateDirect(squareCoords.length * 4);
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(squareCoords);
        vertexBuffer.position(0);

        GLES31.glUseProgram(ShadersManager.SQUARE_PROGRAM_HANDLE);

        GLES31.glUniformMatrix4fv(ShadersManager.modelMatrixHandle, 1, false, mModelMatrix, 0);
        GLES31.glUniformMatrix4fv(ShadersManager.projectionMatrixHandle, 1, false, GamePlayRenderer.mProjectionMatrix, 0);

        GLES31.glVertexAttribPointer(ShadersManager.positionHandle, ShadersManager.COORDS_PER_VERTEX,
                GLES31.GL_FLOAT, false, ShadersManager.VERTEX_STRIDE, vertexBuffer);
        GLES31.glEnableVertexAttribArray(ShadersManager.positionHandle);

        GLES31.glVertexAttribPointer(ShadersManager.squareColorHandle, ShadersManager.COORDS_PER_COLOR, GLES31.GL_FLOAT, false, ShadersManager.COLOR_STRIDE, ShadersManager.colorBuffer);
        GLES31.glEnableVertexAttribArray(ShadersManager.squareColorHandle);

        GLES31.glDrawElements(GLES31.GL_TRIANGLES, ShadersManager.DRAW_ORDER.length, GLES31.GL_UNSIGNED_SHORT, ShadersManager.drawListBuffer);

        GLES31.glDisableVertexAttribArray(ShadersManager.positionHandle);
        GLES31.glDisableVertexAttribArray(ShadersManager.squareColorHandle);
    }
}
