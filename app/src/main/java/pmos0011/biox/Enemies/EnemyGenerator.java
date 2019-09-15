package pmos0011.biox.Enemies;

import android.util.Log;

import java.util.List;
import java.util.Random;

import pmos0011.biox.CommonObjects.Transformations;
import pmos0011.biox.GameLoopRenderer;
import pmos0011.biox.ParticleEffect.ParticleModelRenderer;
import pmos0011.biox.StaticTextures.StaticTexturesRenderer;

public class EnemyGenerator implements Runnable {
    private List<Enemy> enemyList;
    private StaticTexturesRenderer staticTexturesRenderer;
    private ParticleModelRenderer particleModelRenderer;
    private Random random;
    private float enemyAugmenter;
    private int enemiesCount1stWave;
    private int enemiesCount2ndWave;
    private int waveTimer;

    public EnemyGenerator(List<Enemy> enemyList, StaticTexturesRenderer staticTexturesRenderer, ParticleModelRenderer particleModelRenderer) {

        this.enemyList = enemyList;
        this.staticTexturesRenderer = staticTexturesRenderer;
        this.particleModelRenderer = particleModelRenderer;

        random = new Random();
        enemyAugmenter = 0;
        enemiesCount1stWave = 3;
        enemiesCount2ndWave = 0;
        waveTimer = 40;
    }

    @Override
    public void run() {
        generateEnemy();
    }

    private void generateEnemy() {

        while (GameLoopRenderer.isGamePlay()) {

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {

            }
            GameLoopRenderer.decreaseTimer();

            if (GameLoopRenderer.getWaveTimer() == 0) {

                boolean leftSide = random.nextBoolean();

                generateWave(leftSide, enemiesCount1stWave);
                generateWave(!leftSide, enemiesCount2ndWave);

                if (enemyAugmenter > 1.0f) {
                    enemyAugmenter = 0;
                    enemiesCount1stWave++;
                    waveTimer += 5;
                }

                if (enemiesCount1stWave > 5) {
                    enemiesCount1stWave = 3;
                    enemiesCount2ndWave++;
                    waveTimer += 5;
                }
                enemyAugmenter += 0.6f;
                GameLoopRenderer.setWaveTimer(waveTimer);
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
