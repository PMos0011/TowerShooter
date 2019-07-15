package pmos0011.biox;

import android.opengl.GLES31;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

public class Texture {

    private final String vertexShaderCode =
            "attribute vec4 vPosition;" +
                    "void main() {" +
                    "  gl_Position = vPosition;" +
                    "}";

    private final String fragmentShaderCode =
            "precision mediump float;" +
                    "uniform vec4 vColor;" +
                    "void main() {" +
                    "  gl_FragColor = vColor;" +
                    "}";


    private FloatBuffer vertexBuffer;
    private ShortBuffer drawListBuffer;
    private final int mProgram;

    static final int COORDS_PER_VERTEX = 2;
    static float squareCoords[] = {
            -1.0f,  1.0f,
            -1.0f, -1.0f,
             1.0f, -1.0f,
             1.0f,  1.0f
    };

    short drawOrder[]={0, 1, 2,
                       0, 2, 3
    };

    float color[] = { 0.6f, 0.8f, 0.2f, 1.0f };

    private int positionHandle;
    private int colorHandle;


    private final int vertexStride = COORDS_PER_VERTEX * 4;    private final int vertexCount = squareCoords.length / COORDS_PER_VERTEX;

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

        positionHandle = GLES31.glGetAttribLocation(mProgram, "vPosition");

        GLES31.glEnableVertexAttribArray(positionHandle);

        GLES31.glVertexAttribPointer(positionHandle, COORDS_PER_VERTEX,
                GLES31.GL_FLOAT, false,
                vertexStride, vertexBuffer);

        colorHandle = GLES31.glGetUniformLocation(mProgram, "vColor");

        GLES31.glUniform4fv(colorHandle, 1, color, 0);

        GLES31.glDrawElements(GLES31.GL_TRIANGLES, drawOrder.length,
                GLES31.GL_UNSIGNED_SHORT, drawListBuffer);

        GLES31.glDisableVertexAttribArray(positionHandle);
    }
}
