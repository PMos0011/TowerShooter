package pmos0011.biox.AbstractClasses;

import android.graphics.PointF;
import android.util.Log;

import pmos0011.biox.CommonObjects.Transformations;
import pmos0011.biox.Weapons.Shells;

public abstract class Weapons {

    private final static float CANNON_X_POSITION = 0.0265f;

    protected PointF position = new PointF(0f, 0f);
    protected PointF deltaSpeed;
    protected PointF scale= new PointF();
    protected PointF startPosition;
    protected float angle;
    private float offset;

    public Weapons(float angle, boolean isLeft, float speed) {

        deltaSpeed = Transformations.calculatePoint(angle, speed);
        PointF tempPoint = Transformations.calculatePoint(angle, CANNON_X_POSITION);

        if (isLeft) {
            position.x -= tempPoint.y;
            position.y -= -tempPoint.x;
        } else {
            position.x += tempPoint.y;
            position.y += -tempPoint.x;
        }
        this.angle = angle;
        this.startPosition = new PointF(position.x, position.y);
    }

    public Weapons(float angle, float xPos, float yPos, float speed) {

        deltaSpeed = Transformations.calculatePoint(angle, speed);

        position.x += xPos;
        position.y += yPos;
        this.angle = angle;
        this.startPosition = new PointF(position.x, position.y);
    }

    public PointF getPosition() {
        return position;
    }

    public PointF getDeltaSpeed() {
        return deltaSpeed;
    }

    public PointF getScale() {
        return scale;
    }

    public float getAngle() {
        return angle;
    }

    public PointF getStartPosition() {
        return startPosition;
    }

    public float getOffset() {
        return offset;
    }

    protected void setScale(float x, float aspect) {
        scale.x = x;
        scale.y = x * aspect;

        if (getScale().x < Shells.SHELL_SIZE)
            offset = 0.35f;
        else
            offset = 0.55f;

    }

    public float getTravelDisatnce() {
        float distanceX = position.x - startPosition.x;
        float distanceY = position.y - startPosition.y;
        return (float) Math.sqrt(distanceX * distanceX + distanceY * distanceY);
    }
}