package pmos0011.biox;

import android.graphics.PointF;
import android.opengl.GLES31;
import android.opengl.Matrix;

import java.util.Random;

public class SmokeEffect extends Texture {

    private final int SMOKE_EFFECT = 0;
    private final int ROCKET_FIRE = 1;
    private effectsNames effect;

    private float timeVal;
    private float[] innerColor = {0.85f, 0.60f, 0.10f, 0.95f};
    private float[] outerColor = {0.85f, 0.20f, 0.10f, 0.95f};

    private float scale = 1.0f;
    private PointF smokeFlow= new PointF();
    public float visibility = 4.0f;

    private boolean notGrayDestroyEffect = true;
    private boolean initValues = true;

    private float[] mModelMatrix = new float[16];

    public SmokeEffect(effectsNames effect) {

        Random r = new Random();
        timeVal = r.nextInt((25 - 20) + 1) + 20;
        timeVal = -timeVal;
        this.effect = effect;
    }

    public void draw(int texture_handle, float angle) {
        boolean isFire = false;

        switch (effect) {
            case DESTROY_EFFECT:
                destroyEffect(angle);
                break;
            case LEFT_CANNON_FIRE:
                cannonFire(angle, true);
                break;
            case CANNON_SMOKE:
                cannonSmoke(angle);
                break;
            case RIGHT_CANNON_FIRE:
                cannonFire(angle, false);
                break;

        }

        GLES31.glUseProgram(ShadersManager.SMOKE_PROGRAM_HANDLE);

        GLES31.glUniform4fv(GLES31.glGetUniformLocation(ShadersManager.SMOKE_PROGRAM_HANDLE, "innerColor"), 1, innerColor, 0);
        GLES31.glUniform4fv(GLES31.glGetUniformLocation(ShadersManager.SMOKE_PROGRAM_HANDLE, "outerColor"), 1, outerColor, 0);
        GLES31.glUniform1f(GLES31.glGetUniformLocation(ShadersManager.SMOKE_PROGRAM_HANDLE, "visibility"), visibility);
        GLES31.glUniform1f(GLES31.glGetUniformLocation(ShadersManager.SMOKE_PROGRAM_HANDLE, "f_Time"), timeVal);

        if (isFire)
            GLES31.glUniform1f(GLES31.glGetUniformLocation(ShadersManager.SMOKE_PROGRAM_HANDLE, "isFire"), ROCKET_FIRE);
        else
            GLES31.glUniform1f(GLES31.glGetUniformLocation(ShadersManager.SMOKE_PROGRAM_HANDLE, "isFire"), SMOKE_EFFECT);
        loadOpenGLVariables(mModelMatrix, texture_handle);
    }

    private void destroyEffect(float angle) {

        if (initValues) {

            innerColor[0] = 0.85f;
            innerColor[1] = 0.60f;
            innerColor[2] = 0.10f;

            outerColor[0] = 0.85f;
            outerColor[1] = 0.20f;
            outerColor[2] = 0.10f;

            scale = GamePlayRenderer.SMOKE_EFFECT_SIZE * 0.1f;
            visibility = 4.0f;

            smokeFlow=Calculations.calculatePoint(angle,GamePlayRenderer.SMOKE_CANNON_INITIAL);
            initValues = false;
        }

        Matrix.setIdentityM(mModelMatrix, 0);
        Matrix.translateM(mModelMatrix, 0, smokeFlow.x,smokeFlow.y, GamePlayRenderer.Z_DIMENSION);
        Matrix.scaleM(mModelMatrix, 0, scale, scale, 1);


        if (scale < 1.0f) {
            scale += 0.02f;
            timeVal += 0.1 - scale / 10;
        } else {
            scale += 0.0001f;
            if (innerColor[1] > 0.2) {
                innerColor[1] -= 0.008;
                timeVal += 0.004;
            } else {
                if (outerColor[0] > 0.45 && notGrayDestroyEffect) {
                    outerColor[0] -= 0.005;
                    timeVal += 0.003;
                } else if (outerColor[0] > 0.2 && notGrayDestroyEffect) {
                    outerColor[0] -= 0.005;
                    innerColor[0] -= 0.005;
                    timeVal += 0.003;
                } else {
                    notGrayDestroyEffect = false;
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
        smokeFlow.x+=GamePlayRenderer.WIND_FLOW_X;
        smokeFlow.y+=GamePlayRenderer.WIND_FLOW_Y;
    }

    private void cannonFire(float angle, boolean isLeft) {
        if (initValues) {

            innerColor[0] = 0.85f;
            innerColor[1] = 0.60f;
            innerColor[2] = 0.10f;

            outerColor[0] = 0.85f;
            outerColor[1] = 0.20f;
            outerColor[2] = 0.10f;

            scale = GamePlayRenderer.SMOKE_EFFECT_SIZE;
            visibility = 4.0f;

            initValues = false;
        }

        float xPosition = GamePlayRenderer.CANNON_X_POSITION;
        if (isLeft)
            xPosition = -xPosition;

        Matrix.setIdentityM(mModelMatrix, 0);
        Matrix.rotateM(mModelMatrix, 0, angle, 0, 0, 1.0f);
        Matrix.translateM(mModelMatrix, 0, xPosition, +0.5f, GamePlayRenderer.Z_DIMENSION);
        Matrix.rotateM(mModelMatrix, 0, 180, 0, 0, 1.0f);
        Matrix.scaleM(mModelMatrix, 0, scale, scale * 1.2f, 1);

        timeVal += 0.03;
        visibility -= 0.2;
    }

    private void cannonSmoke(float angle) {
        if (initValues) {

            innerColor[0] = 0.8f;
            innerColor[1] = 0.8f;
            innerColor[2] = 0.8f;

            outerColor[0] = 0.8f;
            outerColor[1] = 0.8f;
            outerColor[2] = 0.8f;

            scale = GamePlayRenderer.SMOKE_EFFECT_SIZE * 1.3f;
            visibility = 4.0f;

            smokeFlow=Calculations.calculatePoint(angle,GamePlayRenderer.SMOKE_CANNON_INITIAL);
            initValues = false;
        }

        Matrix.setIdentityM(mModelMatrix, 0);
        Matrix.translateM(mModelMatrix, 0, smokeFlow.x, smokeFlow.y , GamePlayRenderer.Z_DIMENSION);
        Matrix.scaleM(mModelMatrix, 0, scale, scale, 1);

        timeVal += 0.003;
        scale += 0.0005;
        visibility -= 0.01;
        innerColor[3] -= 0.001;
        outerColor[3] -= 0.0035;
        smokeFlow.x+=GamePlayRenderer.WIND_FLOW_X;
        smokeFlow.y+=GamePlayRenderer.WIND_FLOW_Y;
    }


    public enum effectsNames {

        DESTROY_EFFECT,
        LEFT_CANNON_FIRE,
        RIGHT_CANNON_FIRE,
        CANNON_SMOKE

    }
}
