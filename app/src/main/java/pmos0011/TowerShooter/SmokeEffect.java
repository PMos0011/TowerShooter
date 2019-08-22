package pmos0011.TowerShooter;

import android.graphics.PointF;
import android.opengl.GLES31;
import android.opengl.Matrix;

import java.util.Random;

public class SmokeEffect extends Texture {

    private final int DISABLE = 0;
    private final int ENABLE = 1;
    private effectsNames effect;

    private float timeVal;
    private float[] innerColor = {0.85f, 0.60f, 0.10f, 1.0f};
    private float[] outerColor = {0.85f, 0.20f, 0.10f, 1.0f};

    private float scale;
    private PointF initialPosition;
    public float visibility = 4.0f;
    private float turretAngle;

    private boolean notGrayDestroyEffect = true;
    private boolean initValues = true;

    private float[] mModelMatrix = new float[16];

    float shapeMode;

    public SmokeEffect(effectsNames effect, float turretAngle, float lengthDifference, float posX, float posY, float size) {

        timeVal = new Random().nextInt(100);
        this.effect = effect;
        this.turretAngle =turretAngle;
        this.scale=size;

        initialPosition = Calculations.calculatePoint(turretAngle, lengthDifference);
        initialPosition.x+=posX;
        initialPosition.y+=posY;
    }

    public void draw(int texture_handle) {
        boolean shapeModifEnable = false;

        switch (effect) {
            case DESTROY_EFFECT:
                GLES31.glEnable(GLES31.GL_BLEND);
                GLES31.glBlendFunc(GLES31.GL_SRC_ALPHA, GLES31.GL_ONE_MINUS_SRC_ALPHA);
                destroyEffect();
                break;
            case CANNON_FIRE:
                GLES31.glEnable(GLES31.GL_BLEND);
                GLES31.glBlendFunc(GLES31.GL_SRC_ALPHA, GLES31.GL_ONE);
                shapeModifEnable=true;
                shapeMode=1.5f;
                cannonFire();
                break;
            case CANNON_SMOKE:
                GLES31.glEnable(GLES31.GL_BLEND);
                GLES31.glBlendFunc(GLES31.GL_SRC_ALPHA, GLES31.GL_ONE_MINUS_SRC_ALPHA);
                cannonSmoke();
                break;
            case EXHAUST:
                GLES31.glEnable(GLES31.GL_BLEND);
                GLES31.glBlendFunc(GLES31.GL_SRC_ALPHA, GLES31.GL_ONE_MINUS_SRC_ALPHA);
                shapeModifEnable=true;
                shapeMode=4.0f;
                exhaust();
                break;
        }

        GLES31.glUseProgram(ShadersManager.SMOKE_PROGRAM_HANDLE);

        GLES31.glUniform4fv(ShadersManager.smokeInnerColorHandle, 1, innerColor, 0);
        GLES31.glUniform4fv(ShadersManager.smokeOuterColorHandle, 1, outerColor, 0);
        GLES31.glUniform1f(ShadersManager.smokeVisibilityHandle, visibility);
        GLES31.glUniform1f(ShadersManager.smokeTimeHandle, timeVal);

        if (shapeModifEnable){
            GLES31.glUniform1f(ShadersManager.smokeShapeModEnableHandle, ENABLE);
            GLES31.glUniform1f(ShadersManager.smokeShapeModHandle, shapeMode);
            }
        else
            GLES31.glUniform1f(ShadersManager.smokeShapeModEnableHandle, DISABLE);

        GLES31.glUniform1f(GLES31.glGetUniformLocation(ShadersManager.TEXTURE_PROGRAM_HANDLE, "isFont"), DISABLE);

        loadOpenGLVariables(mModelMatrix, texture_handle);

        GLES31.glDisable(GLES31.GL_BLEND);
    }

    private void destroyEffect() {
        if (initValues) {

            innerColor[0] = 1.f;
            innerColor[1] = 0.60f;
            innerColor[2] = 0.10f;

            outerColor[0] = 1.f;
            outerColor[1] = 0.20f;
            outerColor[2] = 0.10f;

            scale *= 0.1f;
            visibility = 4.0f;

            initValues = false;
        }

        Matrix.setIdentityM(mModelMatrix, 0);
        Matrix.translateM(mModelMatrix, 0, initialPosition.x, initialPosition.y, GamePlayRenderer.Z_DIMENSION);
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
        initialPosition.x += GamePlayRenderer.WIND_FLOW_X;
        initialPosition.y += GamePlayRenderer.WIND_FLOW_Y;
    }

    private void cannonFire() {

        if (initValues) {

            innerColor[0] = 1.00f;
            innerColor[1] = 0.70f;
            innerColor[2] = 0.20f;

            outerColor[0] = 1.00f;
            outerColor[1] = 0.30f;
            outerColor[2] = 0.20f;

            visibility = 4.0f;

            initValues = false;
        }

        Matrix.setIdentityM(mModelMatrix, 0);
        Matrix.translateM(mModelMatrix, 0, initialPosition.x, initialPosition.y, GamePlayRenderer.Z_DIMENSION);
        Matrix.rotateM(mModelMatrix, 0, turretAngle, 0, 0, 1.0f);
        Matrix.scaleM(mModelMatrix, 0, scale,-scale, 1);

        timeVal += 0.03;
        visibility -= 0.2;
    }

    private void cannonSmoke() {

        if (initValues) {

            innerColor[0] = 0.85f;
            innerColor[1] = 0.85f;
            innerColor[2] = 0.85f;
            innerColor[3] = 0.0f;

            outerColor[0] = 0.85f;
            outerColor[1] = 0.85f;
            outerColor[2] = 0.85f;
            outerColor[3] = 0.0f;

            scale *= 1.4f;
            visibility = 4.0f;

            initValues = false;
        }

        Matrix.setIdentityM(mModelMatrix, 0);
        Matrix.translateM(mModelMatrix, 0, initialPosition.x, initialPosition.y, GamePlayRenderer.Z_DIMENSION);
        Matrix.scaleM(mModelMatrix, 0, scale, scale, 1);

        if (innerColor[3] < 0.9 && visibility == 4.0) {
            innerColor[3] += 0.07;
            outerColor[3] += 0.07;
        } else {
            scale += 0.0005;
            visibility -= 0.015;
            innerColor[3] -= 0.0015;
            outerColor[3] -= 0.0045;
            initialPosition.x += GamePlayRenderer.WIND_FLOW_X;
            initialPosition.y += GamePlayRenderer.WIND_FLOW_Y;
        }
        timeVal += 0.003;
    }
    private void exhaust(){
        if (initValues) {

            innerColor[0] = 0.25f;
            innerColor[1] = 0.25f;
            innerColor[2] = 0.25f;
            innerColor[3] = 0.3f;

            outerColor[0] = 0.35f;
            outerColor[1] = 0.35f;
            outerColor[2] = 0.35f;
            outerColor[3] = 0.2f;

            visibility = 1.5f;

            initValues = false;
        }

        Matrix.setIdentityM(mModelMatrix, 0);
        Matrix.translateM(mModelMatrix, 0, initialPosition.x, initialPosition.y, GamePlayRenderer.Z_DIMENSION);
        Matrix.rotateM(mModelMatrix, 0, turretAngle, 0, 0, 1.0f);
        Matrix.scaleM(mModelMatrix, 0, scale, scale, 1);

        timeVal += 0.01;
    }


    public enum effectsNames {

        DESTROY_EFFECT,
        CANNON_FIRE,
        CANNON_SMOKE,
        EXHAUST

    }
}
