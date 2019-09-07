package pmos0011.biox.ParticleEffect;

import android.graphics.PointF;

import java.util.Random;

import pmos0011.biox.AbstractClasses.ParticleEffects;
import pmos0011.biox.CommonObjects.Transformations;

public class FireParticleEffect extends ParticleEffects {

    private float sparkSize;

    public FireParticleEffect(effectKind effect, float worldAngle, float objectAngle, float effectOffset, float xPos, float yPos, float size, float travelDistance) {
        super(effect, worldAngle, objectAngle, effectOffset, xPos, yPos, size, travelDistance);
    }

    @Override
    protected void particleInitial(float effectOffset, float xPos, float yPos, float size) {
        switch (getEffect()) {
            case CANNON_FIRE:

                setParticlePosition(Transformations.calculatePoint(getWorldAngle(), effectOffset));
                getParticlePosition().x += xPos;
                getParticlePosition().y += yPos;
                setScaleX(22 * size);
                setScaleY(-26 * size);

                Transformations.setModelTranslation(getModelMatrix(), 0, getWorldAngle(), getParticlePosition().x, getParticlePosition().y, getScaleX(), getScaleY());
                setInnerColor(LIGHT_YELLOW.clone());
                setOuterColor(LIGHT_RED.clone());
                setOptions(CANNON_FIRE.clone());
                break;

            case HIT_SPARK:
                Random random = new Random();
                sparkSize=random.nextFloat() + random.nextInt(4)+2;
                addTime(random.nextInt(100));
                setParticlePosition(Transformations.calculatePoint(getWorldAngle(), effectOffset));
                PointF deltaPosition = Transformations.calculatePoint(getWorldAngle(), getDistanceParameter());
                getParticlePosition().x += xPos+deltaPosition.x;
                getParticlePosition().y += yPos+deltaPosition.y;
                setHitSize(size);
                setScaleX(0);
                setScaleY(0);
                setDeltaSpeed(Transformations.calculatePoint(getWorldAngle(), 1.5f * size));
                Transformations.setModelTranslation(getModelMatrix(), 0, getWorldAngle(), getParticlePosition().x, getParticlePosition().y, getScaleX(), getScaleY());
                setInnerColor(WHITE.clone());
                setOuterColor(LIGHT_RED.clone());
                getInnerColor()[3] = 0.4f;
                getOuterColor()[3] = 0.6f;
                setOptions(HIT_SPARK.clone());
                break;
        }
    }

    public void particleUpdate() {
        switch (getEffect()) {
            case CANNON_FIRE:
                fire();
                break;
            case HIT_SPARK:
                hitSpark();
                break;
        }
    }

    private void fire() {

        addTime(0.04f);
        changeVisibility(-0.08f);
    }

    private void hitSpark() {

        if (getScaleX() < sparkSize * getHitSize() && getVisibility() == 2.0f) {
            changeScaleX(0.5f * getHitSize());
            changeScaleY(-1.0f * getHitSize());
            getParticlePosition().x -= getDeltaSpeed().x;
            getParticlePosition().y -= getDeltaSpeed().y;
        } else {
            changeScaleX(-0.1f * getHitSize());
            changeScaleY(0.2f * getHitSize());
            getParticlePosition().x -= getDeltaSpeed().x / 4;
            getParticlePosition().y -= getDeltaSpeed().y / 4;
            changeVisibility(-0.04f);
        }
        Transformations.setModelTranslation(getModelMatrix(), 0, getWorldAngle(), getParticlePosition().x, getParticlePosition().y, getScaleX(), getScaleY());
    }
}

