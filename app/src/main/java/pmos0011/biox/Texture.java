package pmos0011.biox;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.opengl.GLES31;
import android.opengl.GLUtils;
import android.os.SystemClock;
import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Texture {

    Context context;
    private FloatBuffer vertexBuffer;
    private FloatBuffer textureBuffer;
    private ShortBuffer drawListBuffer;
    private final int mProgram;

    float someVal;

    private int mPositionHandle;
    private int mTextureHandle;
    private int textureCoordinateHandle;
    private int mModelMatrixHandle;
    private int mProjectionMatrixHandle;
    private int mColorHandle;
    private final int COORDS_PER_VERTEX = 2;
    private final int VERTEX_STRIDE = COORDS_PER_VERTEX * 4;

    float squareCoords[] = {
            -1.0f, 1.0f,
            -1.0f, -1.0f,
            1.0f, -1.0f,
            1.0f, 1.0f
    };

    float textureCords[] = {
            0.0f, 1.0f,
            0.0f, 0.0f,
            1.0f, 0.0f,
            1.0f, 1.0f
    };

    short drawOrder[] = {
            0, 1, 2,
            0, 2, 3
    };

    public Texture(Context context, float size_mod, boolean isSmoke) {

        this.context = context;

        Random r = new Random();
        someVal=r.nextInt((25 - 20)+1)+20;
        someVal=-someVal;

        for (int i = 0; i < squareCoords.length; i++)
            squareCoords[i] *= size_mod;

        ByteBuffer bb = ByteBuffer.allocateDirect(squareCoords.length * 4);
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(squareCoords);
        vertexBuffer.position(0);

        ByteBuffer dlb = ByteBuffer.allocateDirect(drawOrder.length * 2);
        dlb.order(ByteOrder.nativeOrder());
        drawListBuffer = dlb.asShortBuffer();
        drawListBuffer.put(drawOrder);
        drawListBuffer.position(0);

        ByteBuffer tlb = ByteBuffer.allocateDirect(textureCords.length * 4);
        tlb.order(ByteOrder.nativeOrder());
        textureBuffer = tlb.asFloatBuffer();
        textureBuffer.put(textureCords);
        textureBuffer.position(0);

        int vertexShader = FileReader.reader(context, GLES31.GL_VERTEX_SHADER, R.raw.texture_vertex_shader);

        int fragmentShader;
        if(!isSmoke)
            fragmentShader = FileReader.reader(context, GLES31.GL_FRAGMENT_SHADER, R.raw.texture_fragment_shader);
        else
            fragmentShader = FileReader.reader(context, GLES31.GL_FRAGMENT_SHADER, R.raw.smoke_fragment_shader);

        mProgram = GLES31.glCreateProgram();

        GLES31.glAttachShader(mProgram, vertexShader);
        GLES31.glAttachShader(mProgram, fragmentShader);
        GLES31.glLinkProgram(mProgram);
    }

    public void draw(float[] mModelMatrix, int texture_handle, float opacity) {

        GLES31.glUseProgram(mProgram);

        GLES31.glEnable(GLES31.GL_BLEND);
        GLES31.glBlendFunc(GLES31.GL_SRC_ALPHA, GLES31.GL_ONE_MINUS_SRC_ALPHA);

        float color[] = {1.0f, 1.0f, 1.0f, opacity};

        mColorHandle = GLES31.glGetUniformLocation(mProgram, "v_Color");
        mModelMatrixHandle = GLES31.glGetUniformLocation(mProgram, "u_mModelMatrix");
        mProjectionMatrixHandle = GLES31.glGetUniformLocation(mProgram, "u_mProjectionMatrix");
        mPositionHandle = GLES31.glGetAttribLocation(mProgram, "a_Position");
        mTextureHandle = GLES31.glGetUniformLocation(mProgram, "u_Texture");
        textureCoordinateHandle = GLES31.glGetAttribLocation(mProgram, "a_TexCoordinate");

        GLES31.glUniformMatrix4fv(mModelMatrixHandle, 1, false, mModelMatrix, 0);
        GLES31.glUniformMatrix4fv(mProjectionMatrixHandle, 1, false, GamePlayRenderer.mProjectionMatrix, 0);

        GLES31.glVertexAttribPointer(mPositionHandle, COORDS_PER_VERTEX,
                GLES31.GL_FLOAT, false, VERTEX_STRIDE, vertexBuffer);
        GLES31.glEnableVertexAttribArray(mPositionHandle);

        GLES31.glVertexAttribPointer(textureCoordinateHandle, COORDS_PER_VERTEX,
                GLES31.GL_FLOAT, false, VERTEX_STRIDE, textureBuffer);
        GLES31.glEnableVertexAttribArray(textureCoordinateHandle);

        GLES31.glActiveTexture(GLES31.GL_TEXTURE0);
        GLES31.glBindTexture(GLES31.GL_TEXTURE_2D, texture_handle);
        GLES31.glUniform1i(mTextureHandle, 0);

        GLES31.glUniform4fv(mColorHandle, 1, color, 0);

        GLES31.glDrawElements(GLES31.GL_TRIANGLES, drawOrder.length, GLES31.GL_UNSIGNED_SHORT, drawListBuffer);

        GLES31.glDisableVertexAttribArray(mPositionHandle);
        GLES31.glDisableVertexAttribArray(textureCoordinateHandle);
    }

    public void draw(float[] mModelMatrix, int texture_handle) {

        GLES31.glUseProgram(mProgram);

        GLES31.glEnable(GLES31.GL_BLEND);
        GLES31.glBlendFunc(GLES31.GL_SRC_ALPHA, GLES31.GL_ONE_MINUS_SRC_ALPHA);

        someVal+=0.005;

        mColorHandle = GLES31.glGetUniformLocation(mProgram, "time");
        mModelMatrixHandle = GLES31.glGetUniformLocation(mProgram, "u_mModelMatrix");
        mProjectionMatrixHandle = GLES31.glGetUniformLocation(mProgram, "u_mProjectionMatrix");
        mPositionHandle = GLES31.glGetAttribLocation(mProgram, "a_Position");
        mTextureHandle = GLES31.glGetUniformLocation(mProgram, "u_Texture");
        textureCoordinateHandle = GLES31.glGetAttribLocation(mProgram, "a_TexCoordinate");

        GLES31.glUniformMatrix4fv(mModelMatrixHandle, 1, false, mModelMatrix, 0);
        GLES31.glUniformMatrix4fv(mProjectionMatrixHandle, 1, false, GamePlayRenderer.mProjectionMatrix, 0);

        GLES31.glVertexAttribPointer(mPositionHandle, COORDS_PER_VERTEX,
                GLES31.GL_FLOAT, false, VERTEX_STRIDE, vertexBuffer);
        GLES31.glEnableVertexAttribArray(mPositionHandle);

        GLES31.glVertexAttribPointer(textureCoordinateHandle, COORDS_PER_VERTEX,
                GLES31.GL_FLOAT, false, VERTEX_STRIDE, textureBuffer);
        GLES31.glEnableVertexAttribArray(textureCoordinateHandle);

        GLES31.glActiveTexture(GLES31.GL_TEXTURE0);
        GLES31.glBindTexture(GLES31.GL_TEXTURE_2D, texture_handle);
        GLES31.glUniform1i(mTextureHandle, 0);

        GLES31.glUniform1f(mColorHandle, someVal);

        GLES31.glDrawElements(GLES31.GL_TRIANGLES, drawOrder.length, GLES31.GL_UNSIGNED_SHORT, drawListBuffer);

        GLES31.glDisableVertexAttribArray(mPositionHandle);
        GLES31.glDisableVertexAttribArray(textureCoordinateHandle);
    }

    public void loadTexture(int texture_id) {

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
