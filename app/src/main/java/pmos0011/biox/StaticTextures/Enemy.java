package pmos0011.biox.StaticTextures;

import java.util.Random;

import pmos0011.biox.AbstractClasses.Weapons;
import pmos0011.biox.Weapons.Shells;

public class Enemy extends Weapons {

    private static final float TANK_SIZE = 0.13f;
    private static final float TANK_ASPECT = 1900.0f / 800.f;

    private float turretAngle;
    private float reloadingStatus;

    private StaticTextures staticTextures;

    public Enemy(float angle, float xPos, float yPos, float speed, StaticTextures staticTextures) {
        super(angle, xPos, yPos, speed);

        setScale(TANK_SIZE, TANK_ASPECT);

        this.staticTextures = staticTextures;
        getTarget();

        reloadingStatus = new Random().nextFloat();

    }

    public float getTurretAngle() {
        return turretAngle;
    }

    public void enemyMove() {
        getPosition().y += 0.0008f;

        if (getPosition().y > 1)
            getPosition().y = -1;

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
}
