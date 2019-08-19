package pmos0011.biox;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.opengl.GLES31;
import android.opengl.GLUtils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class Texture {


    public void draw(float[] mModelMatrix, int texture_handle, float opacity) {

        float color[] = {1.0f, 1.0f, 1.0f, opacity};

        GLES31.glUseProgram(ShadersManager.TEXTURE_PROGRAM_HANDLE);
        GLES31.glUniform4fv(GLES31.glGetUniformLocation(ShadersManager.TEXTURE_PROGRAM_HANDLE, "v_Color"), 1, color, 0);

        loadOpenGLVariables(mModelMatrix, texture_handle);
    }

    public void loadOpenGLVariables(float[] mModelMatrix, int texture_handle) {

        int mPositionHandle = GLES31.glGetAttribLocation(ShadersManager.TEXTURE_PROGRAM_HANDLE, "a_Position");
        int textureCoordinateHandle = GLES31.glGetAttribLocation(ShadersManager.TEXTURE_PROGRAM_HANDLE, "a_TexCoordinate");

        GLES31.glUniformMatrix4fv(GLES31.glGetUniformLocation(ShadersManager.TEXTURE_PROGRAM_HANDLE, "u_mModelMatrix"), 1, false, mModelMatrix, 0);
        GLES31.glUniformMatrix4fv(GLES31.glGetUniformLocation(ShadersManager.TEXTURE_PROGRAM_HANDLE, "u_mProjectionMatrix"), 1, false, GamePlayRenderer.mProjectionMatrix, 0);

        GLES31.glVertexAttribPointer(mPositionHandle, ShadersManager.COORDS_PER_VERTEX,
                GLES31.GL_FLOAT, false, ShadersManager.VERTEX_STRIDE, ShadersManager.vertexBuffer);
        GLES31.glEnableVertexAttribArray(mPositionHandle);

        GLES31.glVertexAttribPointer(textureCoordinateHandle, ShadersManager.COORDS_PER_VERTEX,
                GLES31.GL_FLOAT, false, ShadersManager.VERTEX_STRIDE, ShadersManager.textureBuffer);
        GLES31.glEnableVertexAttribArray(textureCoordinateHandle);

        GLES31.glActiveTexture(GLES31.GL_TEXTURE0);
        GLES31.glBindTexture(GLES31.GL_TEXTURE_2D, texture_handle);
        GLES31.glUniform1i(GLES31.glGetUniformLocation(ShadersManager.TEXTURE_PROGRAM_HANDLE, "u_Texture"), 0);

        GLES31.glDrawElements(GLES31.GL_TRIANGLES, ShadersManager.DRAW_ORDER.length, GLES31.GL_UNSIGNED_SHORT, ShadersManager.drawListBuffer);

        GLES31.glDisableVertexAttribArray(mPositionHandle);
        GLES31.glDisableVertexAttribArray(textureCoordinateHandle);
    }

    public void loadTexture(Context context, int texture_id) {

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;

        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), texture_id, options);

        Matrix matrix = new Matrix();
        matrix.preScale(1.0f, -1.0f);
        Bitmap flippedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

        GLES31.glBindTexture(GLES31.GL_TEXTURE_2D, texture_id);

        GLES31.glTexParameteri(GLES31.GL_TEXTURE_2D, GLES31.GL_TEXTURE_MIN_FILTER, GLES31.GL_NEAREST);
        GLES31.glTexParameteri(GLES31.GL_TEXTURE_2D, GLES31.GL_TEXTURE_MAG_FILTER, GLES31.GL_NEAREST);

        GLUtils.texImage2D(GLES31.GL_TEXTURE_2D, 0, flippedBitmap, 0);

        bitmap.recycle();
        flippedBitmap.recycle();
    }
}
