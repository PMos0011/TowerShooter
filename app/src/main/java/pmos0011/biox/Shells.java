package pmos0011.biox;

import android.graphics.PointF;

public class Shells {

    public PointF shellPosition;
    public PointF deltaSpeed;
    public float shellAngle;

    public Shells(float angle, boolean isLeft) {
        shellPosition = new PointF();
        deltaSpeed = new PointF();

        shellPosition = Calculations.calculatePoint(angle, GamePlayRenderer.SHELL_START_POSITION);
        deltaSpeed = Calculations.calculatePoint(angle, GamePlayRenderer.SHELL_SPEED);
        PointF tempPoint = Calculations.calculatePoint(angle, GamePlayRenderer.CANNON_X_POSITION);

        if (isLeft) {
            shellPosition.x -= tempPoint.y;
            shellPosition.y -= -tempPoint.x;
        }else {
            shellPosition.x += tempPoint.y;
            shellPosition.y += -tempPoint.x;
        }
        shellAngle = angle;
    }
}
