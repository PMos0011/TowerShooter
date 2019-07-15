package pmos0011.biox;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES31;
import android.opengl.GLUtils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

public class Texture {

    private final String vertexShaderCode =
            "attribute vec4 a_Position;" +
                    "attribute vec2 a_TexCoordinate;"+
                    "varying vec2 v_TexCoordinate;"+
                    "void main() {" +
                    "v_TexCoordinate = a_TexCoordinate;"+
                    "  gl_Position = a_Position;" +
                    "}";

    private final String fragmentShaderCode =
            "precision mediump float;" +
                    "uniform sampler2D u_Texture;"+
                    "varying vec2 v_TexCoordinate;"+
                    "void main() {" +
                    "  gl_FragColor = texture2D(u_Texture, v_TexCoordinate);" +
                    "}";


    final int[] textureHandle = new int[1];
    private FloatBuffer vertexBuffer;
    private FloatBuffer textureBuffer;
    private ShortBuffer drawListBuffer;
    private final int mProgram;

    static final int COORDS_PER_VERTEX = 2;

    static float squareCoords[] = {
            -1.0f,  1.0f,
            -1.0f, -1.0f,
             1.0f, -1.0f,
             1.0f,  1.0f
    };

    final float[] textureCords =
            {
                    0.0f, 1.0f,
                    0.0f, 0.0f,
                    1.0f, 0.0f,
                    1.0f, 1.0f
            };

    short drawOrder[]={0, 1, 2,
            0, 2, 3
    };

    private int positionHandle;
    private int mTextureHandle;
    private int textureCoordinateHandle;
    private final int vertexStride = COORDS_PER_VERTEX * 4;


    public Texture(){

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

    public void draw() {

        GLES31.glUseProgram(mProgram);

        positionHandle = GLES31.glGetAttribLocation(mProgram, "a_Position");
        mTextureHandle = GLES31.glGetUniformLocation(mProgram, "u_Texture");
        textureCoordinateHandle = GLES31.glGetAttribLocation(mProgram, "a_TexCoordinate");

        GLES31.glActiveTexture(GLES31.GL_TEXTURE0);
        GLES31.glBindTexture(GLES31.GL_TEXTURE_2D, textureHandle[0]);
        GLES31.glUniform1i(mTextureHandle, 0);

        GLES31.glVertexAttribPointer(positionHandle, COORDS_PER_VERTEX,
                GLES31.GL_FLOAT, false,
                vertexStride, vertexBuffer);
        GLES31.glEnableVertexAttribArray(positionHandle);

        GLES31.glVertexAttribPointer(textureCoordinateHandle, COORDS_PER_VERTEX,
                GLES31.GL_FLOAT, false,
                0, textureBuffer);
        GLES31.glEnableVertexAttribArray(textureCoordinateHandle);

        GLES31.glDrawElements(GLES31.GL_TRIANGLES, drawOrder.length,
                GLES31.GL_UNSIGNED_SHORT, drawListBuffer);

        GLES31.glDisableVertexAttribArray(positionHandle);
        GLES31.glDisableVertexAttribArray(textureCoordinateHandle);
    }

    public void loadTexture(Context context, int texture_id){
        GLES31.glGenTextures(1, textureHandle, 0);

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;

        final Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), texture_id, options);

        GLES31.glBindTexture(GLES31.GL_TEXTURE_2D, textureHandle[0]);

        GLES31.glTexParameteri(GLES31.GL_TEXTURE_2D, GLES31.GL_TEXTURE_MIN_FILTER, GLES31.GL_NEAREST);
        GLES31.glTexParameteri(GLES31.GL_TEXTURE_2D, GLES31.GL_TEXTURE_MAG_FILTER, GLES31.GL_NEAREST);

        GLUtils.texImage2D(GLES31.GL_TEXTURE_2D, 0, bitmap, 0);

        bitmap.recycle();
    }
}
