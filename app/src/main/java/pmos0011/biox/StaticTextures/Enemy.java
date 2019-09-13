package pmos0011.biox.StaticTextures;

import java.util.Random;

import pmos0011.biox.AbstractClasses.ParticleEffects;
import pmos0011.biox.AbstractClasses.Weapons;
import pmos0011.biox.ParticleEffect.FireParticleEffect;
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
    private static final float TANK_EXPLOSION_SPARK_SIZE = 0.01f;

    private boolean isDestroyed;

    private float turretAngle;
    private float reloadingStatus;
    private float hp;
    private float hitStrength;
    private float opacity;

    private StaticTextures staticTextures;
    private ParticleModel particleModel;

    private SmokeParticleEffect leftExhaust;
    private SmokeParticleEffect rightExhaust;
    private SmokeParticleEffect leftTruckDust;
    private SmokeParticleEffect rightTruckDust;
    private SmokeParticleEffect radarDot;
    private SmokeParticleEffect hitPoints;

    public Enemy(float angle, float xPos, float yPos, float speed, StaticTextures staticTextures, ParticleModel particleModel) {
        super(angle, xPos, yPos, speed);

        setScale(TANK_SIZE, TANK_ASPECT);
        hp = 1;
        hitStrength = 10;
        isDestroyed=false;
        opacity=1.0f;

        this.staticTextures = staticTextures;
        this.particleModel = particleModel;
        getTarget();

        reloadingStatus = new Random().nextFloat();

        addExhaustsEffects();
        addTankDust();
        addRadarDot();
        addHitPointPanel();
    }

    public float getTurretAngle() {
        return turretAngle;
    }

    public float getOpacity() {
        return opacity;
    }

    public void enemyMove() {

        methodForTests();
        getTarget();

        if (reloadingStatus < 0 && !isDestroyed) {
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

        addDeltaSpeed(leftExhaust);
        addDeltaSpeed(leftTruckDust);
        addDeltaSpeed(rightTruckDust);
        addDeltaSpeed(rightExhaust);
        addDeltaSpeed(hitPoints);

        if (getPosition().y > 1)
            getPosition().y = -1;

        radarDot.getParticlePosition().x = getPosition().x;
        radarDot.getParticlePosition().y = getPosition().y;

        hitPoints.setScaleX(ParticleEffects.HIT_POINT_SIZE * hp);

        if (hp <= 0 && !isDestroyed) {
            getDeltaSpeed().x = 0;
            getDeltaSpeed().y = 0;

            removeEffect(leftTruckDust);
            removeEffect(rightTruckDust);
            removeEffect(rightExhaust);
            removeEffect(leftExhaust);
            removeEffect(hitPoints);
            removeEffect(radarDot);

            addExplosion();
            isDestroyed=true;
        }

        if (getDeltaSpeed().x<=0){
            removeEffect(leftTruckDust);
            removeEffect(rightTruckDust);
        }

        if(isDestroyed){
            opacity-=0.01f;

            if(opacity<0)
                opacity=0;
        }
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

    private void addRadarDot() {
        radarDot = new SmokeParticleEffect(ParticleEffects.effectKind.ENEMY_DOT,
                0, 0, 0, getPosition().x, getPosition().y,
                ParticleEffects.RADAR_SIZE, 0);
        particleModel.addParticleEffect(radarDot);
    }

    private void addDeltaSpeed(SmokeParticleEffect effect) {

        if (effect != null) {
            effect.getParticlePosition().x += getDeltaSpeed().x;
            effect.getParticlePosition().y += getDeltaSpeed().y;

            if (effect.getParticlePosition().y > 1)
                effect.getParticlePosition().y = -1;
        }
    }

    private void addHitPointPanel() {
        hitPoints = new SmokeParticleEffect(ParticleEffects.effectKind.HIT_POINTS,
                0, 0, 0.18f, getPosition().x, getPosition().y,
                ParticleEffects.HIT_POINT_SIZE, 0);
        particleModel.addParticleEffect(hitPoints);
    }

    public void hitCalculation(float hitPoint) {
        if (hitPoint > getPosition().y)
            hitPoint = hitPoint - getPosition().y;
        else
            hitPoint = getPosition().y - hitPoint;

        hitPoint *= hitStrength;

        if (hitPoint < 0.35)
            hitPoint = 0;
        else if (hitPoint > 0.75)
            hitPoint = 0.75f;

        hp -= 1 - hitPoint;
        if (hp < 0)
            hp = 0;
    }

    private void removeEffect(SmokeParticleEffect effect){
        effect.set0Visibility();
    }

    private void addExplosion(){
        particleModel.addParticleEffect(new FireParticleEffect(ParticleEffects.effectKind.TANK_EXPLOSION, 0, 0,
                0, getPosition().x, getPosition().y, ParticleEffects.TANK_EXPLOSION_SIZE, 0));
        particleModel.addParticleEffect(new SmokeParticleEffect(ParticleEffects.effectKind.TANK_EXPLOSION_SMOKE, 0, 0,
                0, getPosition().x, getPosition().y, ParticleEffects.TANK_EXPLOSION_SIZE, 0));

        int sparksCount = new Random().nextInt(50)+25;
        float deltaAngle = 360 / sparksCount;
        float currentAngle = 0;
        for (int i = 0; i <sparksCount; i++) {

            particleModel.addParticleEffect(new FireParticleEffect(ParticleEffects.effectKind.HIT_SPARK, currentAngle, 0, 0,
                    getPosition().x, getPosition().y,
                    TANK_EXPLOSION_SPARK_SIZE, 0));
            currentAngle += deltaAngle;
        }

    }
}
