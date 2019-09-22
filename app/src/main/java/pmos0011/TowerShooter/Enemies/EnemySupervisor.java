package pmos0011.TowerShooter.Enemies;

import java.util.List;

import pmos0011.TowerShooter.CommonObjects.Transformations;

public class EnemySupervisor {
    private final static float ENEMY_DISTANCE = 0.4f;

    private List<Enemy> enemies;

    public EnemySupervisor(List<Enemy> enemies) {
        this.enemies = enemies;
    }

    public void inspectEnemy(Enemy enemy) {

        boolean accelerate = true;

        for (Enemy enemy1 : enemies)
            if (enemy1.getPosition().x != enemy.getPosition().x && enemy1.getPosition().y != enemy.getPosition().y)
                if (enemy1.getPosition().y + ENEMY_DISTANCE > enemy.getPosition().y && enemy1.getPosition().y - ENEMY_DISTANCE < enemy.getPosition().y)
                    if (enemy1.getPosition().x + ENEMY_DISTANCE > enemy.getPosition().x && enemy1.getPosition().x - ENEMY_DISTANCE < enemy.getPosition().x)
                        if (Math.abs(enemy.getPosition().x) > Math.abs(enemy1.getPosition().x)) {
                            accelerate = false;
                            decelerate(enemy);

                            if (Math.abs(enemy.getCurrentXDeltaSpeed()) < Math.abs(enemy.getDeltaSpeed().x * 0.13f))
                                enemy.setSpeedTo0();
                        }

        if (accelerate) {
            if (Math.abs(enemy.getPosition().x) > Math.abs(enemy.getDestinationPoint().x)) {
                if (Math.abs(enemy.getCurrentXDeltaSpeed()) < Math.abs(enemy.getDeltaSpeed().x))
                    accelerate(enemy);
            } else {
                if (Math.abs(enemy.getCurrentXDeltaSpeed()) > 0)
                    decelerate(enemy);
            }

            if (enemy.getPosition().x > 0) {
                if (enemy.getCurrentXDeltaSpeed() > 0)
                    enemy.setSpeedTo0();
            } else {
                if (enemy.getCurrentXDeltaSpeed() < 0)
                    enemy.setSpeedTo0();
            }
        }

        if (Math.abs(enemy.getPosition().x) < 0.95f * Transformations.getRatio())
            enemy.setInRange(true);
        else
            enemy.setInRange(false);
    }

    private void accelerate(Enemy enemy) {
        enemy.changeXSpeed(enemy.getDeltaSpeed().x * 0.05f);
        enemy.changeYSpeed(enemy.getDeltaSpeed().y * 0.05f);
    }

    private void decelerate(Enemy enemy) {
        enemy.changeXSpeed(enemy.getDeltaSpeed().x * -0.01f);
        enemy.changeYSpeed(enemy.getDeltaSpeed().y * -0.01f);
    }
}
