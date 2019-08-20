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
        openGLProgram(mModelMatrix);
    }

    private void openGLProgram(float[] mModelMatrix) {

        ByteBuffer bb = ByteBuffer.allocateDirect(squareCoords.length * 4);
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(squareCoords);
        vertexBuffer.position(0);

        GLES31.glUseProgram(ShadersManager.SQUARE_PROGRAM_HANDLE);
        int mPositionHandle = GLES31.glGetAttribLocation(ShadersManager.SQUARE_PROGRAM_HANDLE, "a_Position");
        int mColorHandle = GLES31.glGetAttribLocation(ShadersManager.SQUARE_PROGRAM_HANDLE, "a_Color");

        GLES31.glUniformMatrix4fv(GLES31.glGetUniformLocation(ShadersManager.SQUARE_PROGRAM_HANDLE, "u_mModelMatrix"), 1, false, mModelMatrix, 0);
        GLES31.glUniformMatrix4fv(GLES31.glGetUniformLocation(ShadersManager.SQUARE_PROGRAM_HANDLE, "u_mProjectionMatrix"), 1, false, GamePlayRenderer.mProjectionMatrix, 0);

        GLES31.glVertexAttribPointer(mPositionHandle, ShadersManager.COORDS_PER_VERTEX,
                GLES31.GL_FLOAT, false, ShadersManager.VERTEX_STRIDE, vertexBuffer);
        GLES31.glEnableVertexAttribArray(mPositionHandle);

        GLES31.glVertexAttribPointer(mColorHandle, ShadersManager.COORDS_PER_COLOR, GLES31.GL_FLOAT, false, ShadersManager.COLOR_STRIDE, ShadersManager.colorBuffer);
        GLES31.glEnableVertexAttribArray(mColorHandle);

        GLES31.glDrawElements(GLES31.GL_TRIANGLES, ShadersManager.DRAW_ORDER.length, GLES31.GL_UNSIGNED_SHORT, ShadersManager.drawListBuffer);

        GLES31.glDisableVertexAttribArray(mPositionHandle);
        GLES31.glDisableVertexAttribArray(mColorHandle);
    }
}
