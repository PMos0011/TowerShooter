package pmos0011.biox.StaticTextures;

import java.util.Random;

import pmos0011.biox.AbstractClasses.ParticleEffects;
import pmos0011.biox.AbstractClasses.Weapons;
import pmos0011.biox.CommonObjects.Transformations;
import pmos0011.biox.ParticleEffect.ParticleModel;
import pmos0011.biox.ParticleEffect.SmokeParticleEffect;
import pmos0011.biox.Weapons.Shells;

public class Enemy extends Weapons {

    private static final float TANK_SIZE = 0.13f;
    private static final float TANK_ASPECT = 1900.0f / 800.f;

    private float turretAngle;
    private float reloadingStatus;

    private StaticTextures staticTextures;

    private SmokeParticleEffect leftExhaust;
    private SmokeParticleEffect rightExhaust;

    public Enemy(float angle, float xPos, float yPos, float speed, StaticTextures staticTextures, ParticleModel particleModel) {
        super(angle, xPos, yPos, speed);

        setScale(TANK_SIZE, TANK_ASPECT);

        this.staticTextures = staticTextures;
        getTarget();

        reloadingStatus = new Random().nextFloat();

        addExhaustsEffects(particleModel);
    }

    public float getTurretAngle() {
        return turretAngle;
    }

    public void enemyMove() {

        methodForTests();
        getTarget();


        if (reloadingStatus < 0) {
            staticTextures.addShell(turretAngle, getPosition().x, getPosition().y, Shells.SHELL_SPEED);
            reloadingStatus = 1;
        }

        if (reloadingStatus >= 0)
            reloadingStatus -= 0.001f;
    }

    private void getTarget() {

        if (getPosition().x < 0) {
            double mod = Math.sqrt(getPosition().x * getPosition().x + getPosition().y * getPosition().y);
            double cos = getPosition().y / mod;

            turretAngle = (float) Math.toDegrees(Math.acos(cos));

            turretAngle += 180;
        } else {
            double mod = Math.sqrt(getPosition().x * getPosition().x + getPosition().y * getPosition().y);
            double sin = getPosition().y / mod;

            turretAngle = (float) Math.toDegrees(Math.asin(sin));

            turretAngle += 90;
        }

    }

    private void addExhaustsEffects(ParticleModel particleModel) {

        leftExhaust = new SmokeParticleEffect(ParticleEffects.effectKind.TANK_EXHAUST, 0, getAngle(), -0.25f, getPosition().x - 0.018f, getPosition().y, 0.04f, 0);
        Transformations.setModelTranslation(leftExhaust.getModelMatrix(), 0, getAngle(),
                leftExhaust.getParticlePosition().x, leftExhaust.getParticlePosition().y, leftExhaust.getScaleX(), leftExhaust.getScaleY());
        particleModel.addParticleEffect(leftExhaust);

        rightExhaust = new SmokeParticleEffect(ParticleEffects.effectKind.TANK_EXHAUST, 0, getAngle(), -0.25f, getPosition().x + 0.018f, getPosition().y, 0.04f, 0);
        Transformations.setModelTranslation(rightExhaust.getModelMatrix(), 0, getAngle(),
                rightExhaust.getParticlePosition().x, rightExhaust.getParticlePosition().y, rightExhaust.getScaleX(), rightExhaust.getScaleY());
        particleModel.addParticleEffect(rightExhaust);

    }

    private void methodForTests() {
        getPosition().x += getDeltaSpeed().x;
        getPosition().y += getDeltaSpeed().y;

        leftExhaust.getParticlePosition().x += getDeltaSpeed().x;
        leftExhaust.getParticlePosition().y += getDeltaSpeed().y;

        rightExhaust.getParticlePosition().x += getDeltaSpeed().x;
        rightExhaust.getParticlePosition().y += getDeltaSpeed().y;


        if (getPosition().y > 1)
            getPosition().y = -1;

        if (leftExhaust.getParticlePosition().y > 1)
            leftExhaust.getParticlePosition().y = -1;

        if (rightExhaust.getParticlePosition().y > 1)
            rightExhaust.getParticlePosition().y = -1;

    }
}
