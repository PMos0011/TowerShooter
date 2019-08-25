package pmos0011.biox;

import android.content.Context;
import android.opengl.GLES31;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public abstract class Shader {

    private int programHandle;
    private int vertexShaderHandle;
    private int fragmentShaderHandle;

    public Shader(Context context, int vertexFileID, int fragmentFileID) {

        vertexShaderHandle = loadShader(context, GLES31.GL_VERTEX_SHADER, vertexFileID);
        fragmentShaderHandle = loadShader(context, GLES31.GL_FRAGMENT_SHADER, fragmentFileID);

        this.programHandle = GLES31.glCreateProgram();

        GLES31.glAttachShader(programHandle, vertexShaderHandle);
        GLES31.glAttachShader(programHandle, fragmentShaderHandle);
        bindAttributes();
        GLES31.glLinkProgram(programHandle);
        GLES31.glValidateProgram(programHandle);
        getAllUniformsHandle();
    }

    protected abstract void bindAttributes();

    protected void bindAttributes(int attribute, String variableName) {
        GLES31.glBindAttribLocation(programHandle, attribute, variableName);
    }

    protected abstract void getAllUniformsHandle();

    protected int getUniformHandle(String uniformName) {
        return GLES31.glGetUniformLocation(programHandle, uniformName);
    }

    public void start() {
        GLES31.glUseProgram(programHandle);
    }

    public void stop() {
        GLES31.glUseProgram(0);
    }

    private int loadShader(Context context, int type, int shaderID) {

        InputStream inputStream = context.getResources().openRawResource(shaderID);
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

        String nextLine;
        StringBuilder shaderString = new StringBuilder();

        while (true) {
            try {
                if (((nextLine = bufferedReader.readLine()) == null)) break;
                shaderString.append(nextLine);
                shaderString.append('\n');
            } catch (IOException e) {
            }
        }

        int shader = GLES31.glCreateShader(type);
        GLES31.glShaderSource(shader, shaderString.toString());
        GLES31.glCompileShader(shader);

        return shader;
    }

}
