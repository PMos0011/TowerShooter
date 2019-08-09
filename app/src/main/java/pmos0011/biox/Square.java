package pmos0011.biox;

import android.opengl.GLES31;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

public class Square {

    private final String vertexShaderCode =
            "attribute vec4 a_Position;" +
                    "attribute vec4 a_Color;" +
                    "uniform mat4 u_mModelMatrix;" +
                    "uniform mat4 u_mProjectionMatrix;" +
                    "varying vec4 v_Color;" +
                    "void main() {" +
                    "v_Color=a_Color;" +
                    "  gl_Position = (u_mProjectionMatrix*u_mModelMatrix)*a_Position;" +
                    "}";

    private final String fragmentShaderCode =
            "precision mediump float;" +
                    "varying vec4 v_Color;" +
                    "void main() {" +
                    "  gl_FragColor = v_Color;" +
                    "}";


    private final int mProgram;
    private int mPositionHandle;
    private int mColorHandle;
    private int mModelMatrixHandle;
    private int mProjectionMatrixHandle;

    public void setSquare(float[] mProjectionMatrix, float ratio) {
        this.mProjectionMatrix = mProjectionMatrix;
        this.radius = ratio * 0.85f;
    }

    float[] mProjectionMatrix = new float[16];
    float radius;

    private FloatBuffer vertexBuffer;
    private ShortBuffer drawListBuffer;
    private FloatBuffer colorBuffer;

    private final int COORDS_PER_VERTEX = 2;
    private final int vertexStride = COORDS_PER_VERTEX * 4;
    float squareCoords[] = new float[8];


    short drawOrder[] = {0, 1, 2,
            0, 2, 3
    };


    private final int COORDS_PER_COLOR = 4;
    float squareColors[] = {
            1.0f, 0.0f, 0.0f, 0.0f,
            1.0f, 0.0f, 0.0f, 0.0f,
            1.0f, 0.0f, 0.0f, 0.5f,
            1.0f, 0.0f, 0.0f, 0.5f
    };

    public Square() {


        ByteBuffer dlb = ByteBuffer.allocateDirect(drawOrder.length * 2);
        dlb.order(ByteOrder.nativeOrder());
        drawListBuffer = dlb.asShortBuffer();
        drawListBuffer.put(drawOrder);
        drawListBuffer.position(0);

        ByteBuffer cb = ByteBuffer.allocateDirect(squareColors.length * 4);
        cb.order(ByteOrder.nativeOrder());
        colorBuffer = cb.asFloatBuffer();
        colorBuffer.put(squareColors);
        colorBuffer.position(0);

        int vertexShader = GamePlayRenderer.loadShader(GLES31.GL_VERTEX_SHADER,
                vertexShaderCode);
        int fragmentShader = GamePlayRenderer.loadShader(GLES31.GL_FRAGMENT_SHADER,
                fragmentShaderCode);


        mProgram = GLES31.glCreateProgram();

        GLES31.glAttachShader(mProgram, vertexShader);
        GLES31.glAttachShader(mProgram, fragmentShader);
        GLES31.glLinkProgram(mProgram);
    }

    public void draw(float[] mModelMatrix, float angle) {

        setCoords(angle);
        ByteBuffer bb = ByteBuffer.allocateDirect(squareCoords.length * 4);
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(squareCoords);
        vertexBuffer.position(0);
        GLES31.glUseProgram(mProgram);

        mPositionHandle = GLES31.glGetAttribLocation(mProgram, "a_Position");
        mColorHandle = GLES31.glGetAttribLocation(mProgram, "a_Color");
        mModelMatrixHandle = GLES31.glGetUniformLocation(mProgram, "u_mModelMatrix");
        mProjectionMatrixHandle = GLES31.glGetUniformLocation(mProgram, "u_mProjectionMatrix");

        GLES31.glUniformMatrix4fv(mModelMatrixHandle, 1, false, mModelMatrix, 0);
        GLES31.glUniformMatrix4fv(mProjectionMatrixHandle, 1, false, mProjectionMatrix, 0);

        GLES31.glVertexAttribPointer(mPositionHandle, COORDS_PER_VERTEX,
                GLES31.GL_FLOAT, false,
                vertexStride, vertexBuffer);
        GLES31.glEnableVertexAttribArray(mPositionHandle);

        GLES31.glVertexAttribPointer(mColorHandle, COORDS_PER_COLOR, GLES31.GL_FLOAT, false,
                0, colorBuffer);
        GLES31.glEnableVertexAttribArray(mColorHandle);


        GLES31.glDrawElements(GLES31.GL_TRIANGLES, drawOrder.length,
                GLES31.GL_UNSIGNED_SHORT, drawListBuffer);

        GLES31.glDisableVertexAttribArray(mPositionHandle);
        GLES31.glDisableVertexAttribArray(mColorHandle);
    }

    private void setCoords(float angle) {

        int quarter = 0;

        while (angle >= 90) {
            angle -= 90;
            quarter++;
        }
        double radians = Math.toRadians(angle);
        float delta;

        float centerXPos;
        float centerYPos;

        if (angle <= 45)
            delta = (float) (0.015f * Math.cos(radians));
        else
            delta = (float) (0.015f * Math.sin(radians));

        switch (quarter) {

            case 0:
                centerXPos = (float) (radius * Math.sin(radians))*-1.0f;
                centerYPos = (float) (radius * Math.cos(radians));
                squareCoords[0] = centerXPos + delta;
                squareCoords[2] = centerXPos - delta;
                squareCoords[1] = centerYPos + delta;
                squareCoords[3] = centerYPos - delta;
                break;

            case 1:
                centerXPos = (float) (radius * Math.cos(radians))*-1.0f;
                centerYPos = (float) (radius * Math.sin(radians))*-1.0f;
                squareCoords[0] = centerXPos - delta;
                squareCoords[2] = centerXPos + delta;
                squareCoords[1] = centerYPos + delta;
                squareCoords[3] = centerYPos - delta;
                break;

            case 2:
                centerXPos = (float) (radius * Math.sin(radians));
                centerYPos = (float) (radius * Math.cos(radians))*-1.0f;
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
    }
}
