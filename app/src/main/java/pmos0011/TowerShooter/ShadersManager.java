package pmos0011.TowerShooter;

import android.content.Context;
import android.opengl.GLES31;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;


public class ShadersManager {
    public final static int TEXTURE_PROGRAM_HANDLE = GLES31.glCreateProgram();
    public final static int SMOKE_PROGRAM_HANDLE = GLES31.glCreateProgram();
    public final static int SQUARE_PROGRAM_HANDLE = GLES31.glCreateProgram();

    public static int textureVertexShaderHandle;
    public static int textureFragmentShaderHandle;
    public static int squareVertexShaderHandle;
    public static int squareFragmentShaderHandle;
    public static int smokeFragmentShaderHandle;

    public static int projectionMatrixHandle;
    public static int modelMatrixHandle;
    public static int textureHandle;
    public static int texturePositionHandle;
    public static int textureColorHandle;
    public static int textureCoordinateHandle;
    public static int squareColorHandle;
    public static int squarePositionHandle;
    public static int smokeInnerColorHandle;
    public static int smokeOuterColorHandle;
    public static int smokeVisibilityHandle;
    public static int smokeTimeHandle;
    public static int smokeFireBoolHandle;

    public static final int COORDS_PER_VERTEX = 2;
    public static final int VERTEX_STRIDE = COORDS_PER_VERTEX * 4;
    public static final int COORDS_PER_COLOR = 4;
    public static final int COLOR_STRIDE = COORDS_PER_COLOR * 4;

    public static final float SQUARE_CORDS[] = {
            -1.0f, 1.0f,
            -1.0f, -1.0f,
            1.0f, -1.0f,
            -1.0f, 1.0f,
            1.0f, -1.0f,
            1.0f, 1.0f
    };

    public static final float TEXTURE_CORDS[] = {
            0.0f, 1.0f,
            0.0f, 0.0f,
            1.0f, 0.0f,
            0.0f, 1.0f,
            1.0f, 0.0f,
            1.0f, 1.0f
    };


    public static final float SQUARE_COLORS[] = {
            1.0f, 0.0f, 0.0f, 0.0f,
            1.0f, 0.0f, 0.0f, 0.0f,
            1.0f, 0.0f, 0.0f, 0.5f,
            1.0f, 0.0f, 0.0f, 0.0f,
            1.0f, 0.0f, 0.0f, 0.5f,
            1.0f, 0.0f, 0.0f, 0.5f
    };

    public static FloatBuffer vertexBuffer;
    public static FloatBuffer textureBuffer;
    public static FloatBuffer colorBuffer;

    public static int reader(Context context, int type, int resID) {

        final InputStream inputStream = context.getResources().openRawResource(resID);
        final InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        final BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

        String nextLine;
        final StringBuilder shaderString = new StringBuilder();

        while (true) {
            try {
                if (((nextLine = bufferedReader.readLine()) == null)) break;
                shaderString.append(nextLine);
                shaderString.append('\n');
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        int shader = GLES31.glCreateShader(type);

        GLES31.glShaderSource(shader, shaderString.toString());
        GLES31.glCompileShader(shader);

        return shader;
    }

    public static void loadShaders(Context context) {

        ByteBuffer bb = ByteBuffer.allocateDirect(SQUARE_CORDS.length * 4);
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(SQUARE_CORDS);
        vertexBuffer.position(0);

        ByteBuffer tlb = ByteBuffer.allocateDirect(TEXTURE_CORDS.length * 4);
        tlb.order(ByteOrder.nativeOrder());
        textureBuffer = tlb.asFloatBuffer();
        textureBuffer.put(TEXTURE_CORDS);
        textureBuffer.position(0);

        ByteBuffer cb = ByteBuffer.allocateDirect(SQUARE_COLORS.length * 4);
        cb.order(ByteOrder.nativeOrder());
        colorBuffer = cb.asFloatBuffer();
        colorBuffer.put(SQUARE_COLORS);
        colorBuffer.position(0);

        textureFragmentShaderHandle = reader(context, GLES31.GL_FRAGMENT_SHADER, R.raw.texture_fragment_shader);
        textureVertexShaderHandle = reader(context, GLES31.GL_VERTEX_SHADER, R.raw.texture_vertex_shader);
        smokeFragmentShaderHandle = reader(context, GLES31.GL_FRAGMENT_SHADER, R.raw.smoke_fragment_shader);
        squareFragmentShaderHandle = reader(context, GLES31.GL_FRAGMENT_SHADER, R.raw.square_fragment_shader);
        squareVertexShaderHandle = reader(context, GLES31.GL_VERTEX_SHADER, R.raw.square_vertex_shader);

        GLES31.glAttachShader(TEXTURE_PROGRAM_HANDLE, textureVertexShaderHandle);
        GLES31.glAttachShader(TEXTURE_PROGRAM_HANDLE, textureFragmentShaderHandle);
        GLES31.glAttachShader(SMOKE_PROGRAM_HANDLE, textureVertexShaderHandle);
        GLES31.glAttachShader(SMOKE_PROGRAM_HANDLE, smokeFragmentShaderHandle);
        GLES31.glAttachShader(SQUARE_PROGRAM_HANDLE, squareVertexShaderHandle);
        GLES31.glAttachShader(SQUARE_PROGRAM_HANDLE, squareFragmentShaderHandle);

        GLES31.glLinkProgram(TEXTURE_PROGRAM_HANDLE);
        GLES31.glLinkProgram(SMOKE_PROGRAM_HANDLE);
        GLES31.glLinkProgram(SQUARE_PROGRAM_HANDLE);

        projectionMatrixHandle = GLES31.glGetUniformLocation(ShadersManager.TEXTURE_PROGRAM_HANDLE, "u_mProjectionMatrix");
        modelMatrixHandle = GLES31.glGetUniformLocation(ShadersManager.TEXTURE_PROGRAM_HANDLE, "u_mModelMatrix");
        textureHandle = GLES31.glGetUniformLocation(ShadersManager.TEXTURE_PROGRAM_HANDLE, "u_Texture");
        textureColorHandle = GLES31.glGetUniformLocation(ShadersManager.TEXTURE_PROGRAM_HANDLE, "v_Color");
        texturePositionHandle = GLES31.glGetAttribLocation(ShadersManager.TEXTURE_PROGRAM_HANDLE, "a_Position");
        textureCoordinateHandle = GLES31.glGetAttribLocation(ShadersManager.TEXTURE_PROGRAM_HANDLE, "a_TexCoordinate");

        squareColorHandle = GLES31.glGetAttribLocation(ShadersManager.SQUARE_PROGRAM_HANDLE, "a_Color");
        squarePositionHandle = GLES31.glGetAttribLocation(ShadersManager.SQUARE_PROGRAM_HANDLE, "square_Position");

        smokeInnerColorHandle = GLES31.glGetUniformLocation(ShadersManager.SMOKE_PROGRAM_HANDLE, "innerColor");
        smokeOuterColorHandle = GLES31.glGetUniformLocation(ShadersManager.SMOKE_PROGRAM_HANDLE, "outerColor");
        smokeVisibilityHandle = GLES31.glGetUniformLocation(ShadersManager.SMOKE_PROGRAM_HANDLE, "visibility");
        smokeTimeHandle = GLES31.glGetUniformLocation(ShadersManager.SMOKE_PROGRAM_HANDLE, "f_Time");
        smokeFireBoolHandle = GLES31.glGetUniformLocation(ShadersManager.SMOKE_PROGRAM_HANDLE, "isFire");
    }

}
