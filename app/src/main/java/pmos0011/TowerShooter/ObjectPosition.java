package pmos0011.TowerShooter;

import android.graphics.PointF;

public abstract class ObjectPosition {
    public PointF position = new PointF(0f,0f);
    public PointF deltaSpeed;
    public PointF scale = new PointF();
    public float angle;



    public ObjectPosition(float angle, boolean isLeft, float speed) {

        deltaSpeed = Calculations.calculatePoint(angle, speed);
        PointF tempPoint = Calculations.calculatePoint(angle, GamePlayRenderer.CANNON_X_POSITION);

        if (isLeft) {
            position.x -= tempPoint.y;
            position.y -= -tempPoint.x;
        }else {
            position.x += tempPoint.y;
            position.y += -tempPoint.x;
        }
        this.angle = angle;

    }

    public ObjectPosition(float angle, float xPos, float yPos, float speed) {

        deltaSpeed = Calculations.calculatePoint(angle, speed);

        position.x+=xPos;
        position.y+=yPos;

        this.angle = angle;

    }

    protected void setScale(float x, float aspect){
        this.scale.x=x;
        this.scale.y=x*aspect;
    }

}
