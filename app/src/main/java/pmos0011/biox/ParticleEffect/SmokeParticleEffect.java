package pmos0011.biox.ParticleEffect;

import pmos0011.biox.AbstractClasses.ParticleEffects;
import pmos0011.biox.CommonObjects.Transformations;
import pmos0011.biox.GameLoopRenderer;
import pmos0011.biox.StaticTextures.StaticTextures;
import pmos0011.biox.Weapons.Shells;

public class SmokeParticleEffect extends ParticleEffects {

    public SmokeParticleEffect(effectKind effect, float worldAngle, float objectAngle, float effectOffset, float xPos, float yPos, float size, float travelDistance) {
        super(effect, worldAngle, objectAngle, effectOffset, xPos, yPos, size, travelDistance);
    }


    @Override
    protected void particleInitial(float effectOffset, float xPos, float yPos, float size) {
        switch (effect) {
            case CANNON_SMOKE:

                particlePosition = Transformations.calculatePoint(worldAngle, effectOffset);
                particlePosition.x += xPos;
                particlePosition.y += yPos;
                scaleX = 35*size;
                scaleY = 35*size;

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
                scaleX = size * 4;
                scaleY = size * Shells.SHELL_ASPECT * 8 * this.travelDistance;

                Transformations.setModelTranslation(modelMatrix, 0, objectAngle, particlePosition.x, particlePosition.y, scaleX, scaleY);
                break;

            case TANK_EXHAUST:
                particlePosition = Transformations.calculatePoint(objectAngle, effectOffset + travelDistance / 5);
                particlePosition.x += xPos;
                particlePosition.y += yPos;
                this.innerColor = EXHAUST_LIGHT_BLUE.clone();
                this.outerColor = EXHAUST_DARK_GRAY.clone();
                this.innerColor[3] = 0.5f;
                this.outerColor[3] = 0.5f;
                this.options = TANK_EXHAUST.clone();
                scaleX = size;
                scaleY = size*2;
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

            case TANK_EXHAUST:
                tankExhaust();
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

    private void tankExhaust(){
        addTime(0.04f);

        Transformations.setModelTranslation(getModelMatrix(),0,objectAngle,
                getParticlePosition().x, getParticlePosition().y,getScaleX(),getScaleY());
    }
}
