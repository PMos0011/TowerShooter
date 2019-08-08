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
import java.nio.ShortBuffer;

public class Texture {

    private final String vertexShaderCode =
            "attribute vec4 a_Position;" +
                    "uniform mat4 u_mModelMatrix;" +
                    "uniform mat4 u_mProjectionMatrix;" +
                    "attribute vec2 a_TexCoordinate;" +
                    "varying vec2 v_TexCoordinate;" +
                    "void main() {" +
                    "v_TexCoordinate = a_TexCoordinate;" +
                    "  gl_Position = (u_mProjectionMatrix*u_mModelMatrix)*a_Position;" +
                    "}";

    private final String fragmentShaderCode =
            "precision mediump float;" +
                    "uniform sampler2D u_Texture;" +
                    "uniform vec4 v_Color;" +
                    "varying vec2 v_TexCoordinate;" +
                    "void main() {" +
                    "  gl_FragColor = texture2D(u_Texture, v_TexCoordinate)* v_Color;" +
                    "}";


    public void setmProjectionMatrix(float[] mProjectionMatrix) {
        this.mProjectionMatrix = mProjectionMatrix;
    }

    float[] mProjectionMatrix = new float[16];

    private FloatBuffer vertexBuffer;
    private FloatBuffer textureBuffer;
    private ShortBuffer drawListBuffer;
    private final int mProgram;


    private int positionHandle;
    private int mTextureHandle;
    private int textureCoordinateHandle;
    private int mModelMatrixHandle;
    private int mProjectionMatrixHandle;
    private int mColorHandle;
    private final int COORDS_PER_VERTEX = 2;
    private final int vertexStride = COORDS_PER_VERTEX * 4;

    float squareCoords[] = {
            -1.0f, 1.0f,
            -1.0f, -1.0f,
            1.0f, -1.0f,
            1.0f, 1.0f

    };

    float textureCords[] =
            {
                    0.0f, 1.0f,
                    0.0f, 0.0f,
                    1.0f, 0.0f,
                    1.0f, 1.0f
            };

    short drawOrder[] = {0, 1, 2,
            0, 2, 3
    };

    public Texture(float size_mod) {

        for (int i=0; i<squareCoords.length; i++)
            squareCoords[i]*=size_mod;

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

        int vertexShader = GamePlayRenderer.loadShader(GLES31.GL_VERTEX_SHADER,
                vertexShaderCode);
        int fragmentShader = GamePlayRenderer.loadShader(GLES31.GL_FRAGMENT_SHADER,
                fragmentShaderCode);


        mProgram = GLES31.glCreateProgram();

        GLES31.glAttachShader(mProgram, vertexShader);
        GLES31.glAttachShader(mProgram, fragmentShader);
        GLES31.glLinkProgram(mProgram);
    }

    public void draw(float[] mModelMatrix, int texture_handle, float opacity) {

        GLES31.glUseProgram(mProgram);

        GLES31.glEnable(GLES31.GL_BLEND);
        GLES31.glBlendFunc(GLES31.GL_SRC_ALPHA, GLES31.GL_ONE_MINUS_SRC_ALPHA);

        float color[] = { 1.0f, 1.0f, 1.0f, opacity };

        mColorHandle = GLES31.glGetUniformLocation(mProgram, "v_Color");
        mModelMatrixHandle = GLES31.glGetUniformLocation(mProgram, "u_mModelMatrix");
        mProjectionMatrixHandle = GLES31.glGetUniformLocation(mProgram, "u_mProjectionMatrix");
        positionHandle = GLES31.glGetAttribLocation(mProgram, "a_Position");
        mTextureHandle = GLES31.glGetUniformLocation(mProgram, "u_Texture");
        textureCoordinateHandle = GLES31.glGetAttribLocation(mProgram, "a_TexCoordinate");

        GLES31.glUniformMatrix4fv(mModelMatrixHandle, 1, false, mModelMatrix, 0);
        GLES31.glUniformMatrix4fv(mProjectionMatrixHandle, 1, false, mProjectionMatrix, 0);

        GLES31.glVertexAttribPointer(positionHandle, COORDS_PER_VERTEX,
                GLES31.GL_FLOAT, false,
                vertexStride, vertexBuffer);
        GLES31.glEnableVertexAttribArray(positionHandle);

        GLES31.glVertexAttribPointer(textureCoordinateHandle, COORDS_PER_VERTEX,
                GLES31.GL_FLOAT, false,
                0, textureBuffer);
        GLES31.glEnableVertexAttribArray(textureCoordinateHandle);

        GLES31.glActiveTexture(GLES31.GL_TEXTURE0);
        GLES31.glBindTexture(GLES31.GL_TEXTURE_2D, texture_handle);
        GLES31.glUniform1i(mTextureHandle, 0);

        GLES31.glUniform4fv(mColorHandle, 1, color, 0);

        GLES31.glDrawElements(GLES31.GL_TRIANGLES, drawOrder.length,
                GLES31.GL_UNSIGNED_SHORT, drawListBuffer);

        GLES31.glDisableVertexAttribArray(positionHandle);
        GLES31.glDisableVertexAttribArray(textureCoordinateHandle);

    }

    public void loadTexture(Context context, int texture_id) {

        int texture_handle = texture_id;
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;

        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), texture_id, options);

        Matrix matrix = new Matrix();
        matrix.preScale(1.0f, -1.0f);
        Bitmap flippedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

        GLES31.glBindTexture(GLES31.GL_TEXTURE_2D, texture_handle);

        GLES31.glTexParameteri(GLES31.GL_TEXTURE_2D, GLES31.GL_TEXTURE_MIN_FILTER, GLES31.GL_NEAREST);
        GLES31.glTexParameteri(GLES31.GL_TEXTURE_2D, GLES31.GL_TEXTURE_MAG_FILTER, GLES31.GL_NEAREST);

        GLUtils.texImage2D(GLES31.GL_TEXTURE_2D, 0, flippedBitmap, 0);

        bitmap.recycle();
        flippedBitmap.recycle();

    }
}
