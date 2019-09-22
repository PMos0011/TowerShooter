package pmos0011.TowerShooter.Enemies;

import android.graphics.PointF;

import java.util.Random;

import pmos0011.TowerShooter.AbstractClasses.ParticleEffects;
import pmos0011.TowerShooter.AbstractClasses.Weapons;
import pmos0011.TowerShooter.CommonObjects.Transformations;
import pmos0011.TowerShooter.GameLoopRenderer;
import pmos0011.TowerShooter.ParticleEffect.ParticleModelRenderer;
import pmos0011.TowerShooter.ParticleEffect.SmokeParticleEffect;
import pmos0011.TowerShooter.StaticTextures.StaticTexturesRenderer;
import pmos0011.TowerShooter.Weapons.Shells;

public class Enemy extends Weapons {

    public static final float TANK_HIT_SIZE = 0.1f;
    public static final float TANK_EXPLOSION_SPARK_SIZE = 0.01f;

    private static final float TANK_SIZE = 0.13f;
    private static final float TANK_ASPECT = 1900.0f / 800.f;
    private static final float TANK_EXHAUST_Y_OFFSET = -0.25f;
    private static final float TANK_EXHAUST_X_OFFSET = 0.018f;
    private static final float TANK_EXHAUST_SIZE = 0.04f;
    private static final float TANK_TRUCK_DUST_Y_OFFSET = -0.15f;
    private static final float TANK_TRUCK_DUST_X_OFFSET = 0.1f;
    private static final float TANK_TRUCK_DUST_SIZE = 0.15f;
    private static final float TANK_TRUCK_DUST_ANGLE_OFFSET = 10;

    private boolean isDestroyed;
    private boolean inRange;

    private float turretAngle;
    private float reloadingStatus;
    private float hp;
    private float hitStrength;
    private float opacity;
    private float currentXDeltaSpeed;
    private float currentYDeltaSpeed;
    private PointF destinationPoint;

    private StaticTexturesRenderer staticTexturesRenderer;
    private ParticleModelRenderer particleModelRenderer;

    private SmokeParticleEffect leftExhaust;
    private SmokeParticleEffect rightExhaust;
    private SmokeParticleEffect leftTruckDust;
    private SmokeParticleEffect rightTruckDust;
    private SmokeParticleEffect radarDot;
    private SmokeParticleEffect hitPoints;

    public Enemy(float angle, float xPos, float yPos, float speed, StaticTexturesRenderer staticTexturesRenderer, ParticleModelRenderer particleModelRenderer, float xPoint, float yPoint) {
        super(angle, xPos, yPos, speed);

        setScale(TANK_SIZE, TANK_ASPECT);
        this.destinationPoint = new PointF(xPoint, yPoint);
        hp = 1;
        hitStrength = 10;
        isDestroyed = false;
        inRange = false;
        opacity = 1.0f;
        currentXDeltaSpeed = 0;
        currentYDeltaSpeed = 0;

        this.staticTexturesRenderer = staticTexturesRenderer;
        this.particleModelRenderer = particleModelRenderer;
        turretAngle = Transformations.calculateAngle(getPosition().x, getPosition().y, 0f, 0f);

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

    public PointF getDestinationPoint() {
        return destinationPoint;
    }

    public float getCurrentXDeltaSpeed() {
        return currentXDeltaSpeed;
    }

    public void changeXSpeed(float speed) {
        currentXDeltaSpeed += speed;
    }

    public void changeYSpeed(float speed) {
        currentYDeltaSpeed += speed;
    }

    public void setInRange(boolean inRange) {
        this.inRange = inRange;
    }

    public void enemyMove() {

        calculateBehavior();
        turretAngle = Transformations.calculateAngle(getPosition().x, getPosition().y, 0f, 0f);

        if (reloadingStatus < 0 && !isDestroyed && inRange) {
            staticTexturesRenderer.addShell(turretAngle, getPosition().x, getPosition().y, Shells.SHELL_SPEED);
            reloadingStatus = 1;
        }

        if (reloadingStatus >= 0)
            reloadingStatus -= 0.001f;
    }

    private void addExhaustsEffects() {

        leftExhaust = new SmokeParticleEffect(ParticleEffects.effectKind.TANK_EXHAUST,
                0, getAngle(), TANK_EXHAUST_Y_OFFSET, getPosition().x, getPosition().y,
                TANK_EXHAUST_SIZE, -TANK_EXHAUST_X_OFFSET);
        particleModelRenderer.addParticleEffect(leftExhaust);

        rightExhaust = new SmokeParticleEffect(ParticleEffects.effectKind.TANK_EXHAUST,
                0, getAngle(), TANK_EXHAUST_Y_OFFSET, getPosition().x, getPosition().y,
                TANK_EXHAUST_SIZE, TANK_EXHAUST_X_OFFSET);
        particleModelRenderer.addParticleEffect(rightExhaust);
    }

    private void calculateBehavior() {
        getPosition().x += currentXDeltaSpeed;
        getPosition().y += currentYDeltaSpeed;

        addDeltaSpeed(leftExhaust);
        addDeltaSpeed(leftTruckDust);
        addDeltaSpeed(rightTruckDust);
        addDeltaSpeed(rightExhaust);
        addDeltaSpeed(hitPoints);

        radarDot.getParticlePosition().x = getPosition().x;
        radarDot.getParticlePosition().y = getPosition().y;

        hitPoints.setScaleX(ParticleEffects.HIT_POINT_SIZE * hp);


        if (Math.abs(getCurrentXDeltaSpeed()) <= 0) {
            leftTruckDust.setVisibility(0.01f);
            rightTruckDust.setVisibility(0.01f);
            leftExhaust.getInnerColor()[3] = 0.25f;
            leftExhaust.getOuterColor()[3] = 0.25f;
            rightExhaust.getInnerColor()[3] = 0.25f;
            rightExhaust.getOuterColor()[3] = 0.25f;
        } else if (!isDestroyed) {
            leftTruckDust.setVisibility(1f);
            rightTruckDust.setVisibility(1f);
            leftExhaust.getInnerColor()[3] = 0.6f;
            leftExhaust.getOuterColor()[3] = 0.6f;
            rightExhaust.getInnerColor()[3] = 0.6f;
            rightExhaust.getOuterColor()[3] = 0.6f;
        }

        if (hp <= 0 && !isDestroyed) {
            GameLoopRenderer.addKill();
            setSpeedTo0();

            removeEffect(leftTruckDust);
            removeEffect(rightTruckDust);
            removeEffect(rightExhaust);
            removeEffect(leftExhaust);
            removeEffect(hitPoints);
            removeEffect(radarDot);

            particleModelRenderer.addExplosion(getPosition().x, getPosition().y);
            isDestroyed = true;
        }

        if (isDestroyed) {
            opacity -= 0.01f;

            if (opacity < 0)
                opacity = 0;
        }
    }

    private void addTankDust() {

        leftTruckDust = new SmokeParticleEffect(ParticleEffects.effectKind.TRACK_DUST,
                0, getAngle() - TANK_TRUCK_DUST_ANGLE_OFFSET, TANK_TRUCK_DUST_Y_OFFSET, getPosition().x, getPosition().y,
                TANK_TRUCK_DUST_SIZE, TANK_TRUCK_DUST_X_OFFSET);
        particleModelRenderer.addParticleEffect(leftTruckDust);
        rightTruckDust = new SmokeParticleEffect(ParticleEffects.effectKind.TRACK_DUST,
                0, getAngle() + TANK_TRUCK_DUST_ANGLE_OFFSET, TANK_TRUCK_DUST_Y_OFFSET, getPosition().x, getPosition().y,
                TANK_TRUCK_DUST_SIZE, -TANK_TRUCK_DUST_X_OFFSET);
        particleModelRenderer.addParticleEffect(rightTruckDust);
    }

    private void addRadarDot() {
        radarDot = new SmokeParticleEffect(ParticleEffects.effectKind.ENEMY_DOT,
                0, 0, 0, getPosition().x, getPosition().y,
                ParticleEffects.RADAR_SIZE, 0);
        particleModelRenderer.addParticleEffect(radarDot);
    }

    private void addDeltaSpeed(SmokeParticleEffect effect) {

        if (effect != null) {
            effect.getParticlePosition().x += currentXDeltaSpeed;
            effect.getParticlePosition().y += currentYDeltaSpeed;
        }
    }

    private void addHitPointPanel() {
        hitPoints = new SmokeParticleEffect(ParticleEffects.effectKind.HIT_POINTS,
                0, 0, 0.18f, getPosition().x, getPosition().y,
                ParticleEffects.HIT_POINT_SIZE, 0);
        particleModelRenderer.addParticleEffect(hitPoints);
    }

    private void removeEffect(SmokeParticleEffect effect) {
        effect.setVisibility(0f);
    }


    public void setSpeedTo0() {
        currentXDeltaSpeed = 0;
        currentYDeltaSpeed = 0;
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


}
