package pmos0011.TowerShooter;

public class Shells extends ObjectPosition {

    public Shells(float angle, boolean isLeft, float speed) {
        super(angle, isLeft, speed);

        setScale(GamePlayRenderer.SHELL_SIZE,GamePlayRenderer.SHELL_ASPECT);

    }

    public Shells(float angle, float xPos, float yPos, float speed) {
        super(angle, xPos, yPos, speed);

        setScale(GamePlayRenderer.SHELL_SIZE/2,GamePlayRenderer.SHELL_ASPECT);
    }

}
