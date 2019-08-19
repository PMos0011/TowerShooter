package pmos0011.biox;

import android.opengl.GLES31;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class Square {

    public void setSquare() {
        this.radius = GamePlayRenderer.ratio * 0.85f;
    }

    private float radius;
    private FloatBuffer vertexBuffer;
    private float squareCoords[] = new float[8];

    public void draw(float[] mModelMatrix, float param, boolean isLaser) {

        if (isLaser) {
            int quarter = 0;

            while (param >= 90) {
                param -= 90;
                quarter++;
            }
            double radians = Math.toRadians(param);
            float delta;

            float centerXPos;
            float centerYPos;

            if (param <= 45)
                delta = (float) (0.015f * Math.cos(radians));
            else
                delta = (float) (0.015f * Math.sin(radians));

            switch (quarter) {

                case 0:
                    centerXPos = (float) (radius * Math.sin(radians)) * -1.0f;
                    centerYPos = (float) (radius * Math.cos(radians));
                    squareCoords[0] = centerXPos + delta;
                    squareCoords[2] = centerXPos - delta;
                    squareCoords[1] = centerYPos + delta;
                    squareCoords[3] = centerYPos - delta;
                    break;

                case 1:
                    centerXPos = (float) (radius * Math.cos(radians)) * -1.0f;
                    centerYPos = (float) (radius * Math.sin(radians)) * -1.0f;
                    squareCoords[0] = centerXPos - delta;
                    squareCoords[2] = centerXPos + delta;
                    squareCoords[1] = centerYPos + delta;
                    squareCoords[3] = centerYPos - delta;
                    break;

                case 2:
                    centerXPos = (float) (radius * Math.sin(radians));
                    centerYPos = (float) (radius * Math.cos(radians)) * -1.0f;
                    squareCoords[0] = centerXPos + delta;
                    squareCoords[2] = centerXPos - delta;
                    squareCoords[1] = centerYPos + delta;
                    squareCoords[3] = centerYPos - delta;
                    break;

                case 3:
                    centerXPos = (float) (radius * Math.cos(radians));
                    centerYPos = (float) (radius * Math.sin(radians));
                    squareCoords[0] = centerXPos - delta;
                    squareCoords[2] = centerXPos + delta;
                    squareCoords[1] = centerYPos + delta;
                    squareCoords[3] = centerYPos - delta;
                    break;
            }
        } else {
            float coords = GamePlayRenderer.GAME_CONTROL_OBJECT_SIZE;

            float statusModifer = param / 100.0f;
            float coordMod = coords - 2 * coords * statusModifer;

            float tmpCoords[] = {
                    coordMod, coords,
                    coordMod, -coords,
                    coords, -coords,
                    coords, coords,
            };
            squareCoords = tmpCoords;
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
