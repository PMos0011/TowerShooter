package pmos0011.biox.Enemies;

import java.util.List;
import java.util.Random;

import pmos0011.biox.CommonObjects.Transformations;
import pmos0011.biox.ParticleEffect.ParticleModelRenderer;
import pmos0011.biox.StaticTextures.StaticTexturesRenderer;

public class EnemyGenerator implements Runnable {
    private List<Enemy> enemyList;
    private StaticTexturesRenderer staticTexturesRenderer;
    private ParticleModelRenderer particleModelRenderer;
    private Random random;
    private float enemiesCount1stWave;
    private float enemiesCount2ndWave;

    public EnemyGenerator(List<Enemy> enemyList, StaticTexturesRenderer staticTexturesRenderer, ParticleModelRenderer particleModelRenderer) {

        this.enemyList = enemyList;
        this.staticTexturesRenderer = staticTexturesRenderer;
        this.particleModelRenderer = particleModelRenderer;

        random = new Random();

        enemiesCount1stWave = 3.34f;
        enemiesCount2ndWave = 0f;
    }

    @Override
    public void run() {
        generateEnemy();
    }

    private void generateEnemy() {

        while (staticTexturesRenderer.getTowerHP() > 0) {

            if (enemyList.size() == 0) {

                boolean leftSide = random.nextBoolean();

                generateWave(leftSide, (int) enemiesCount1stWave);
                generateWave(!leftSide, (int) enemiesCount2ndWave);
                enemiesCount1stWave += 0.34f;

                if(enemiesCount1stWave>5.3){
                    enemiesCount1stWave=3;
                    enemiesCount2ndWave++;
                }
            }
        }
    }

    private float generatePoint(float min, float max) {

        boolean isNegative = false;
        if (min < 0) {
            min = 0f;
            isNegative = true;
        }

        float number = min + random.nextFloat() * (max - min);

        if (isNegative) {
            if (random.nextBoolean())
                number = -number;
        }

        return number;
    }

    private void generateWave(boolean leftSide, int enemyCount) {

        for (int i = 0; i < enemyCount; i++) {
            float startXPoint = generatePoint(1.1f * Transformations.getRatio(), 1.4f * Transformations.getRatio());
            float startYPoint = generatePoint(-1.4f, 1.4f);
            float speed = generatePoint(0.0006f, 0.0012f);
            float destinationXPoint = generatePoint(0.4f * Transformations.getRatio(), 0.9f * Transformations.getRatio());
            float destinationYPoint = generatePoint(-0.8f, 0.8f);

            if (leftSide) {
                startXPoint = -startXPoint;
                destinationXPoint = -destinationXPoint;
            }

            float angle = Transformations.calculateAngle(startXPoint, startYPoint, destinationXPoint, destinationYPoint);

            enemyList.add(new Enemy(angle, startXPoint, startYPoint, speed, staticTexturesRenderer, particleModelRenderer, destinationXPoint, destinationYPoint));
        }
    }
}
