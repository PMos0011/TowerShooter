package pmos0011.TowerShooter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.opengl.GLES31;
import android.opengl.GLUtils;

public class Texture {


    public void draw(float[] mModelMatrix, int texture_handle, float opacity) {

        float color[] = {1.0f, 1.0f, 1.0f, opacity};

        GLES31.glUseProgram(ShadersManager.TEXTURE_PROGRAM_HANDLE);
        GLES31.glUniform4fv(ShadersManager.textureColorHandle, 1, color, 0);
        GLES31.glUniform1f(ShadersManager.isFontHandle, 0);

        GLES31.glEnable(GLES31.GL_BLEND);
        GLES31.glBlendFunc(GLES31.GL_SRC_ALPHA, GLES31.GL_ONE_MINUS_SRC_ALPHA);

        loadOpenGLVariables(mModelMatrix, texture_handle);

        GLES31.glDisable(GLES31.GL_BLEND);

    }

    public void loadOpenGLVariables(float[] mModelMatrix, int texture_handle) {



        GLES31.glUniformMatrix4fv(ShadersManager.modelMatrixHandle, 1, false, mModelMatrix, 0);
        GLES31.glUniformMatrix4fv(ShadersManager.projectionMatrixHandle, 1, false, GamePlayRenderer.mProjectionMatrix, 0);

        GLES31.glVertexAttribPointer(ShadersManager.texturePositionHandle, ShadersManager.COORDS_PER_VERTEX,
                GLES31.GL_FLOAT, false, ShadersManager.VERTEX_STRIDE, ShadersManager.vertexBuffer);
        GLES31.glEnableVertexAttribArray(ShadersManager.texturePositionHandle);



        GLES31.glVertexAttribPointer(ShadersManager.textureCoordinateHandle, ShadersManager.COORDS_PER_VERTEX,
                GLES31.GL_FLOAT, false, ShadersManager.VERTEX_STRIDE, ShadersManager.textureBuffer);
        GLES31.glEnableVertexAttribArray(ShadersManager.textureCoordinateHandle);

        GLES31.glActiveTexture(GLES31.GL_TEXTURE0);
        GLES31.glBindTexture(GLES31.GL_TEXTURE_2D, texture_handle);
        GLES31.glUniform1i(ShadersManager.textureHandle, 0);

        GLES31.glDrawArrays(GLES31.GL_TRIANGLES,0,6);
        GLES31.glDisableVertexAttribArray(ShadersManager.texturePositionHandle);
        GLES31.glDisableVertexAttribArray(ShadersManager.textureCoordinateHandle);
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
