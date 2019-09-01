package pmos0011.biox.ParticleEffect;

import android.util.Log;

import pmos0011.biox.AbstractClasses.ParticleEffects;
import pmos0011.biox.CommonObjects.Transformations;
import pmos0011.biox.GameControlObjects;
import pmos0011.biox.GameLoopRenderer;
import pmos0011.biox.StaticTextures.StaticTextures;
import pmos0011.biox.Weapons.Shells;

public class SmokeParticleEffect extends ParticleEffects {

    private float scaleX;
    private float scaleY;

    public SmokeParticleEffect(effectKind effect, float worldAngle, float objectAngle, float effectOffset, float xPos, float yPos, float travelDistance) {
        super(effect, worldAngle, objectAngle, effectOffset, xPos, yPos, travelDistance);
    }


    @Override
    protected void particleInitial(float effectOffset, float xPos, float yPos) {
        switch (effect) {
            case CANNON_SMOKE:

                particlePosition = Transformations.calculatePoint(worldAngle, effectOffset);
                particlePosition.x += xPos;
                particlePosition.y += yPos;
                scaleX = 0.35f;
                scaleY = 0.35f;

                Transformations.setModelTranslation(modelMatrix, 0, 0, particlePosition.x, particlePosition.y, scaleX, scaleY);

                this.innerColor = GRAY.clone();
                this.outerColor = GRAY.clone();
                this.options = GRAY_SMOKE.clone();
                this.innerColor[3] = 0.01f;
                this.outerColor[3] = 0.0f;
                break;

            case RELOAD_STATUS:
                particlePosition.x = xPos;
                particlePosition.y = yPos;
                scaleX = StaticTextures.GAME_CONTROL_OBJECT_SIZE;
                scaleY = StaticTextures.GAME_CONTROL_OBJECT_SIZE;

                this.innerColor = RED.clone();
                this.options = RELOAD_STATUS.clone();

                Transformations.setModelTranslation(modelMatrix, worldAngle, objectAngle, particlePosition.x, particlePosition.y, scaleX, scaleY);
                break;

            case SHELL_STREAK:
                particlePosition = Transformations.calculatePoint(objectAngle, effectOffset + travelDistance / 5);
                particlePosition.x += xPos;
                particlePosition.y += yPos;
                this.innerColor = GRAY.clone();
                this.outerColor = GRAY.clone();
                this.innerColor[3] = 0.5f;
                this.outerColor[3] = 0.4f;
                this.options = SHEL_STREAK.clone();
                scaleX = Shells.getScale().x * 4;
                scaleY = Shells.getScale().y * 8 * this.travelDistance;

                Transformations.setModelTranslation(modelMatrix, 0, objectAngle, particlePosition.x, particlePosition.y, scaleX, scaleY);
                break;
        }
    }

    public void particleUpdate() {
        switch (effect) {
            case CANNON_SMOKE:
                cannonSmoke();
                break;

            case RELOAD_STATUS:
                reloadStatus();
                break;

            case SHELL_STREAK:
                shellStreak();
                break;
        }
    }

    private void cannonSmoke() {

        addTime(0.004f);

        particlePosition.x += GameLoopRenderer.WIND_FLOW_X;
        particlePosition.y += GameLoopRenderer.WIND_FLOW_Y;

        if (getInnerOpacity() < 0.8f && getVisibility() == 3.0f) {
            changeOpacity(0.03f, 0.028f);
        } else {
            changeVisibility(-0.002f);
            changeOpacity(-0.003f, -0.004f);
            scaleX += 0.0005f;
            scaleY += 0.0005f;
        }

        Transformations.setModelTranslation(modelMatrix, 0, 0, particlePosition.x, particlePosition.y, scaleX, scaleY);
    }

    private void reloadStatus() {
        changeVisibility(-0.002f);
        scaleX = StaticTextures.GAME_CONTROL_OBJECT_SIZE * getVisibility();

        particlePosition.x += StaticTextures.GAME_CONTROL_OBJECT_SIZE * 0.002f;

        Transformations.setModelTranslation(modelMatrix, worldAngle, objectAngle, particlePosition.x, particlePosition.y, scaleX, scaleY);
    }

    private void shellStreak() {
        changeOpacity(-0.01f, -0.01f);
    }
}
