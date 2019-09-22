package pmos0011.TowerShooter.ParticleEffect;

import android.graphics.PointF;

import pmos0011.TowerShooter.AbstractClasses.ParticleEffects;
import pmos0011.TowerShooter.CommonObjects.Transformations;
import pmos0011.TowerShooter.GameLoopRenderer;
import pmos0011.TowerShooter.StaticTextures.StaticTexturesRenderer;
import pmos0011.TowerShooter.Weapons.Shells;

public class SmokeParticleEffect extends ParticleEffects {
    private final static float GREEN_DOT_SIZE = 20;
    private final static float BLUE_DOT_SIZE = 15;

    private PointF deltaPosition;
    private float radarYOffset;

    public SmokeParticleEffect(effectKind effect, float worldAngle, float objectAngle, float effectOffset, float xPos, float yPos, float size, float travelDistance) {
        super(effect, worldAngle, objectAngle, effectOffset, xPos, yPos, size, travelDistance);
    }


    @Override
    protected void particleInitial(float effectOffset, float xPos, float yPos, float size) {
        switch (getEffect()) {
            case CANNON_SMOKE:

                setParticlePosition(Transformations.calculatePoint(getWorldAngle(), effectOffset));
                getParticlePosition().x += xPos;
                getParticlePosition().y += yPos;
                setScaleX(35 * size);
                setScaleY(35 * size);

                Transformations.setModelTranslation(getModelMatrix(), 0, 0, getParticlePosition().x, getParticlePosition().y, getScaleX(), getScaleY());

                setInnerColor(GRAY.clone());
                setOuterColor(GRAY.clone());
                setOptions(GRAY_SMOKE.clone());
                getInnerColor()[3] = 0.01f;
                getOuterColor()[3] = 0.0f;
                break;

            case TANK_EXPLOSION_SMOKE:
                getParticlePosition().x += xPos;
                getParticlePosition().y += yPos;
                setScaleX(size*1.3f);
                setScaleY(size*1.3f);

                Transformations.setModelTranslation(getModelMatrix(), 0, 0, getParticlePosition().x, getParticlePosition().y, getScaleX(), getScaleY());

                setInnerColor(BLACK.clone());
                setOuterColor(MEDIUM_BLACK.clone());
                setOptions(TANK_EXPLOSION_SMOKE.clone());
                getInnerColor()[3] = 0.01f;
                getOuterColor()[3] = 0.0f;
                break;

            case RELOAD_STATUS:
                getParticlePosition().x = xPos;
                getParticlePosition().y = yPos;
                setScaleX(StaticTexturesRenderer.GAME_CONTROL_OBJECT_SIZE);
                setScaleY(StaticTexturesRenderer.GAME_CONTROL_OBJECT_SIZE);

                setInnerColor(RED.clone());
                setOptions(RELOAD_STATUS.clone());

                Transformations.setModelTranslation(getModelMatrix(), getWorldAngle(), getObjectAngle(), getParticlePosition().x, getParticlePosition().y, getScaleX(), getScaleY());
                break;

            case RADAR_BACKGROUND:
                setScaleX(size * Transformations.getRatio() * 1.5f);
                setScaleY(size * 1.5f);

                getParticlePosition().x = xPos;
                getParticlePosition().y = yPos - getScaleY() - getScaleY() * 0.1f;

                setInnerColor(BLACK.clone());
                setOptions(RELOAD_STATUS.clone());

                Transformations.setModelTranslation(getModelMatrix(), getWorldAngle(), getObjectAngle(), getParticlePosition().x, getParticlePosition().y, getScaleX(), getScaleY());
                break;

            case TOWER_DOT:
                getParticlePosition().x = xPos;
                getParticlePosition().y = yPos - (size * 1.5f) - (size * 1.5f * 0.1f);

                setScaleX(size / GREEN_DOT_SIZE);
                setScaleY(size / GREEN_DOT_SIZE);

                setInnerColor(GREEN.clone());
                setOptions(RELOAD_STATUS.clone());

                Transformations.setModelTranslation(getModelMatrix(), getWorldAngle(), getObjectAngle(), getParticlePosition().x, getParticlePosition().y, getScaleX(), getScaleY());
                break;

            case ENEMY_DOT:
                getParticlePosition().x = xPos * ParticleEffects.RADAR_SIZE;
                getParticlePosition().y = (yPos + 1.5f) / 2;
                getParticlePosition().y *= ParticleEffects.RADAR_SIZE;
                radarYOffset = 1 - (size * 1.5f) - (0.5f * size * 1.5f) - (size * 0.1f);
                getParticlePosition().y += radarYOffset;

                setScaleX(size / BLUE_DOT_SIZE);
                setScaleY(size / BLUE_DOT_SIZE);

                setInnerColor(BLUE.clone());
                setOptions(RELOAD_STATUS.clone());

                Transformations.setModelTranslation(getModelMatrix(), getWorldAngle(), getObjectAngle(), getParticlePosition().x, getParticlePosition().y, getScaleX(), getScaleY());
                break;

            case SHELL_STREAK:
                setParticlePosition(Transformations.calculatePoint(getObjectAngle(), effectOffset + getDistanceParameter() / 5));
                getParticlePosition().x += xPos;
                getParticlePosition().y += yPos;
                setInnerColor(GRAY.clone());
                setOuterColor(GRAY.clone());
                getInnerColor()[3] = 0.5f;
                getOuterColor()[3] = 0.4f;
                setOptions(SHEL_STREAK.clone());
                setScaleX(size * 4);
                setScaleY(size * Shells.SHELL_ASPECT * 8 * getDistanceParameter());

                Transformations.setModelTranslation(getModelMatrix(), 0, getObjectAngle(), getParticlePosition().x, getParticlePosition().y, getScaleX(), getScaleY());
                break;

            case TANK_EXHAUST:
                setParticlePosition(Transformations.calculatePoint(getObjectAngle(), effectOffset + getDistanceParameter() / 5));
                deltaPosition = Transformations.calculatePoint(getObjectAngle(), getDistanceParameter());
                getParticlePosition().x += xPos - deltaPosition.y;
                getParticlePosition().y += yPos + deltaPosition.x;
                setInnerColor(EXHAUST_LIGHT_BLUE.clone());
                setOuterColor(EXHAUST_DARK_GRAY.clone());
                setOptions(TANK_EXHAUST.clone());
                setScaleX(size);
                setScaleY(size * 2);
                break;

            case TRACK_DUST:
                setParticlePosition(Transformations.calculatePoint(getObjectAngle(), effectOffset + getDistanceParameter() / 5));
                deltaPosition = Transformations.calculatePoint(getObjectAngle(), getDistanceParameter());
                getParticlePosition().x += xPos - deltaPosition.y;
                getParticlePosition().y += yPos + deltaPosition.x;
                setInnerColor(LIGHT_GRAY.clone());
                setOuterColor(LIGHT_DUST_GRAY.clone());
                getInnerColor()[3] = 0.4f;
                getOuterColor()[3] = 0.3f;
                setOptions(TRACK_DUST.clone());
                setScaleX(size);
                setScaleY(size * 1.5f);
                break;

                case HIT_POINTS:
                    getParticlePosition().x = xPos;
                    getParticlePosition().y = yPos+effectOffset;
                    setScaleX(size);
                    setScaleY(size/8);

                    setInnerColor(RED.clone());
                    setOptions(RELOAD_STATUS.clone());

                    Transformations.setModelTranslation(getModelMatrix(), getWorldAngle(), getObjectAngle(), getParticlePosition().x, getParticlePosition().y, getScaleX(), getScaleY());

                break;
        }


    }

    public void particleUpdate() {
        switch (getEffect()) {
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

            case TRACK_DUST:
                truckDust();
                break;

            case ENEMY_DOT:
                enemyDot();
                break;

            case HIT_POINTS:
                hitPoints();
                break;

            case TANK_EXPLOSION_SMOKE:
                tankSmoke();
                break;
        }
    }

    private void cannonSmoke() {

        addTime(0.004f);

        getParticlePosition().x += GameLoopRenderer.WIND_FLOW_X;
        getParticlePosition().y += GameLoopRenderer.WIND_FLOW_Y;

        if (getInnerOpacity() < 0.8f && getVisibility() == 3.0f) {
            changeOpacity(0.03f, 0.028f);
        } else {
            changeVisibility(-0.002f);
            changeOpacity(-0.003f, -0.004f);
            changeScaleX(0.0005f);
            changeScaleY(0.0005f);
        }

        Transformations.setModelTranslation(getModelMatrix(), 0, 0, getParticlePosition().x, getParticlePosition().y, getScaleX(), getScaleY());
    }

    private void reloadStatus() {
        changeVisibility(-0.002f);
        setScaleX(StaticTexturesRenderer.GAME_CONTROL_OBJECT_SIZE * getVisibility());

        getParticlePosition().x += StaticTexturesRenderer.GAME_CONTROL_OBJECT_SIZE * 0.002f;

        Transformations.setModelTranslation(getModelMatrix(), getWorldAngle(), getObjectAngle(), getParticlePosition().x, getParticlePosition().y, getScaleX(), getScaleY());
    }

    private void shellStreak() {
        changeOpacity(-0.01f, -0.01f);
    }

    private void tankExhaust() {
        addTime(0.04f);
        Transformations.setModelTranslation(getModelMatrix(), 0, getObjectAngle(),
                getParticlePosition().x, getParticlePosition().y, getScaleX(), getScaleY());
    }

    private void truckDust() {
        addTime(0.01f);
        Transformations.setModelTranslation(getModelMatrix(), 0, getObjectAngle(),
                getParticlePosition().x, getParticlePosition().y, getScaleX(), getScaleY());
    }

    private void enemyDot() {
        getParticlePosition().x *= ParticleEffects.RADAR_SIZE;
        getParticlePosition().y = (getParticlePosition().y + 1.5f) / 2;
        getParticlePosition().y *= ParticleEffects.RADAR_SIZE;
        getParticlePosition().y += radarYOffset;

        Transformations.setModelTranslation(getModelMatrix(), 0, 0,
                getParticlePosition().x, getParticlePosition().y, getScaleX(), getScaleY());
    }

    private void hitPoints(){

        Transformations.setModelTranslation(getModelMatrix(), 0, getObjectAngle(),
                getParticlePosition().x, getParticlePosition().y, getScaleX(), getScaleY());
    }

    private void tankSmoke(){
        addTime(0.004f);

        getParticlePosition().x += GameLoopRenderer.WIND_FLOW_X;
        getParticlePosition().y += GameLoopRenderer.WIND_FLOW_Y;

        if (getInnerOpacity() < 0.9f && getVisibility() == 4.0f) {
            changeOpacity(0.03f, 0.028f);
        } else {
            changeVisibility(-0.0015f);
            changeOpacity(-0.002f, -0.003f);
            changeScaleX(0.0005f);
            changeScaleY(0.0005f);
        }

        Transformations.setModelTranslation(getModelMatrix(), 0, 0, getParticlePosition().x, getParticlePosition().y, getScaleX(), getScaleY());
    }
}
