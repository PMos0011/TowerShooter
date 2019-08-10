package pmos0011.biox;

import android.content.Context;
import android.opengl.GLES31;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


public class FileReader {

    public static int reader(Context context, int type, int resID) {

        final InputStream inputStream = context.getResources().openRawResource(
                resID);
        final InputStreamReader inputStreamReader = new InputStreamReader(
                inputStream);
        final BufferedReader bufferedReader = new BufferedReader(
                inputStreamReader);

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
}
