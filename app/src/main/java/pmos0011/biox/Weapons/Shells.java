package pmos0011.biox.Weapons;

import android.graphics.PointF;
import android.util.Log;

import pmos0011.biox.AbstractClasses.Weapons;

public class Shells extends Weapons {

    public Shells(float angle, boolean isLeft, float speed) {
        super(angle, isLeft, speed);

        setScale(SHELL_SIZE, SHELL_ASPECT);

    }

    public Shells(float angle, float xPos, float yPos, float speed) {
        super(angle, xPos, yPos, speed);

        setScale(SHELL_SIZE/2,SHELL_ASPECT);
    }

}
