package pmos0011.biox.StaticTextures;

import java.util.Random;

import pmos0011.biox.AbstractClasses.ParticleEffects;
import pmos0011.biox.AbstractClasses.Weapons;
import pmos0011.biox.ParticleEffect.ParticleModel;
import pmos0011.biox.ParticleEffect.SmokeParticleEffect;
import pmos0011.biox.Weapons.Shells;

public class Enemy extends Weapons {

    public static final float TANK_HIT_SIZE = 0.1f;

    private static final float TANK_SIZE = 0.13f;
    private static final float TANK_ASPECT = 1900.0f / 800.f;
    private static final float TANK_EXHAUST_Y_OFFSET = -0.25f;
    private static final float TANK_EXHAUST_X_OFFSET = 0.018f;
    private static final float TANK_EXHAUST_SIZE = 0.04f;
    private static final float TANK_TRUCK_DUST_Y_OFFSET = -0.15f;
    private static final float TANK_TRUCK_DUST_X_OFFSET = 0.1f;
    private static final float TANK_TRUCK_DUST_SIZE = 0.15f;
    private static final float TANK_TRUCK_DUST_ANGLE_OFFSET = 10;

    private float turretAngle;
    private float reloadingStatus;

    private StaticTextures staticTextures;
    private ParticleModel particleModel;

    private SmokeParticleEffect leftExhaust;
    private SmokeParticleEffect rightExhaust;
    private SmokeParticleEffect leftTruckDust;
    private SmokeParticleEffect rightTruckDust;

    public Enemy(float angle, float xPos, float yPos, float speed, StaticTextures staticTextures, ParticleModel particleModel) {
        super(angle, xPos, yPos, speed);

        setScale(TANK_SIZE, TANK_ASPECT);

        this.staticTextures = staticTextures;
        this.particleModel = particleModel;
        getTarget();

        reloadingStatus = new Random().nextFloat();

        addExhaustsEffects();
        addTankDust();
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

    private void addExhaustsEffects() {

        leftExhaust = new SmokeParticleEffect(ParticleEffects.effectKind.TANK_EXHAUST,
                0, getAngle(), TANK_EXHAUST_Y_OFFSET, getPosition().x, getPosition().y,
                TANK_EXHAUST_SIZE, -TANK_EXHAUST_X_OFFSET);
        particleModel.addParticleEffect(leftExhaust);

        rightExhaust = new SmokeParticleEffect(ParticleEffects.effectKind.TANK_EXHAUST,
                0, getAngle(), TANK_EXHAUST_Y_OFFSET, getPosition().x, getPosition().y,
                TANK_EXHAUST_SIZE, TANK_EXHAUST_X_OFFSET);
        particleModel.addParticleEffect(rightExhaust);
    }

    private void methodForTests() {
        getPosition().x += getDeltaSpeed().x;
        getPosition().y += getDeltaSpeed().y;

        leftExhaust.getParticlePosition().x += getDeltaSpeed().x;
        leftExhaust.getParticlePosition().y += getDeltaSpeed().y;

        rightExhaust.getParticlePosition().x += getDeltaSpeed().x;
        rightExhaust.getParticlePosition().y += getDeltaSpeed().y;

        leftTruckDust.getParticlePosition().x += getDeltaSpeed().x;
        leftTruckDust.getParticlePosition().y += getDeltaSpeed().y;

        rightTruckDust.getParticlePosition().x += getDeltaSpeed().x;
        rightTruckDust.getParticlePosition().y += getDeltaSpeed().y;

        if (getPosition().y > 1)
            getPosition().y = -1;

        if (leftExhaust.getParticlePosition().y > 1)
            leftExhaust.getParticlePosition().y = -1;

        if (rightExhaust.getParticlePosition().y > 1)
            rightExhaust.getParticlePosition().y = -1;

        if (leftTruckDust.getParticlePosition().y > 1)
            leftTruckDust.getParticlePosition().y = -1;

        if (rightTruckDust.getParticlePosition().y > 1)
            rightTruckDust.getParticlePosition().y = -1;
    }

    private void addTankDust() {

        leftTruckDust = new SmokeParticleEffect(ParticleEffects.effectKind.TRACK_DUST,
                0, getAngle() - TANK_TRUCK_DUST_ANGLE_OFFSET, TANK_TRUCK_DUST_Y_OFFSET, getPosition().x, getPosition().y,
                TANK_TRUCK_DUST_SIZE, TANK_TRUCK_DUST_X_OFFSET);
        particleModel.addParticleEffect(leftTruckDust);
        rightTruckDust = new SmokeParticleEffect(ParticleEffects.effectKind.TRACK_DUST,
                0, getAngle() + TANK_TRUCK_DUST_ANGLE_OFFSET, TANK_TRUCK_DUST_Y_OFFSET, getPosition().x, getPosition().y,
                TANK_TRUCK_DUST_SIZE, -TANK_TRUCK_DUST_X_OFFSET);
        particleModel.addParticleEffect(rightTruckDust);
    }
}
