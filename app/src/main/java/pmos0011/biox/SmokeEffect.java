package pmos0011.biox;

import android.content.Context;
import android.opengl.GLES31;

import java.util.Random;

public class SmokeEffect extends Texture {

    private float timeVal;
    private float[] innerColor = {0.85f, 0.60f, 0.10f, 0.8f};
    private float[] outerColor = {0.85f, 0.20f, 0.10f, 0.8f};

    public float scale = 0.1f;
    public float smokeFlow = 0.05f;
    private float visibility = 4.0f;
    private boolean notGray = true;

    private int mTimeHandle;
    private int mInnerColorHandle;
    private int mOuterColorHandle;
    private int mVisibilityHandle;

    public SmokeEffect(Context context, float size_mod, boolean isSmoke) {
        super(context, size_mod, isSmoke);

        Random r = new Random();
        timeVal = r.nextInt((25 - 20) + 1) + 20;
        timeVal = -timeVal;
    }

    public void draw(float[] mModelMatrix, int texture_handle) {

        DestroyEffect();

        GLES31.glUseProgram(mProgram);

        mInnerColorHandle = GLES31.glGetUniformLocation(mProgram, "innerColor");
        GLES31.glUniform4fv(mInnerColorHandle, 1, innerColor, 0);

        mOuterColorHandle = GLES31.glGetUniformLocation(mProgram, "outerColor");
        GLES31.glUniform4fv(mOuterColorHandle, 1, outerColor, 0);

        mVisibilityHandle = GLES31.glGetUniformLocation(mProgram, "visibility");
        GLES31.glUniform1f(mVisibilityHandle, visibility);

        mTimeHandle = GLES31.glGetUniformLocation(mProgram, "f_Time");
        GLES31.glUniform1f(mTimeHandle, timeVal);

        loadOpenGLVariables(mModelMatrix, texture_handle);
    }

    private void DestroyEffect(){
        if (scale < 1.0f) {
            scale += 0.02f;
            timeVal += 0.1 - scale / 10;
            smokeFlow += 0.0002f;
        } else {
            smokeFlow += 0.0005f;
            scale += 0.001f;

            if (innerColor[1] > 0.2) {
                innerColor[1] -= 0.008;
                timeVal += 0.004;
            } else {
                if (outerColor[0] > 0.45 && notGray) {
                    outerColor[0] -= 0.005;
                    timeVal += 0.003;
                } else if (outerColor[0] > 0.2 && notGray) {
                    outerColor[0] -= 0.005;
                    innerColor[0] -= 0.005;
                    timeVal += 0.003;
                } else {
                    notGray = false;
                    if (innerColor[0] > 0.2) {
                        innerColor[0] -= 0.005;
                        timeVal += 0.0025;
                    } else {
                        if (innerColor[3] > 0.0) {
                            innerColor[3] -= 0.01;
                            timeVal += 0.002;
                        }
                        if (outerColor[0] < 0.8) {
                            outerColor[0] += 0.002;
                            outerColor[1] = outerColor[2] = outerColor[0];
                            if (innerColor[3] <= 0.0) {
                                timeVal += 0.002;
                                visibility -= 0.005;
                            }
                        } else {
                            if (visibility > 0) {
                                visibility -= 0.01;
                                if (outerColor[3] > 0.5) outerColor[3] -= 0.005;
                                timeVal += 0.002;
                            }
                        }
                    }
                }
            }
        }
    }
}
