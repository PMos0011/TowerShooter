package pmos0011.TowerShooter;

public class Enemy extends ObjectPosition {

    public float turretAngle;

    public Enemy(float angle, float xPos, float yPos, float speed) {
        super(angle, xPos, yPos, speed);

        setScale(GamePlayRenderer.TANK_SIZE,GamePlayRenderer.TANK_ASPECT);
        this.turretAngle=270.0f;
    }
}
