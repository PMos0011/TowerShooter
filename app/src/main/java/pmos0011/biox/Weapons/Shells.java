package pmos0011.biox.Weapons;

import pmos0011.biox.AbstractClasses.Weapons;

public class Shells extends Weapons {

    public static final float SHELL_SIZE = 0.009f;
    public static final float SHELL_ASPECT = 300.0f / 76.0f;
    public static final float SHELL_SPEED = 0.1f;

    private boolean isEnemy;

    public Shells(float angle, boolean isLeft, float speed) {
        super(angle, isLeft, speed);

        setScale(SHELL_SIZE, SHELL_ASPECT);
        isEnemy=false;

    }

    public Shells(float angle, float xPos, float yPos, float speed) {
        super(angle, xPos, yPos, speed);

        setScale(SHELL_SIZE/2,SHELL_ASPECT);
        isEnemy=true;
    }

    public boolean isEnemy() {
        return isEnemy;
    }
}
