package pmos0011.biox.StaticTextures;

import android.util.Log;

import java.util.List;

import pmos0011.biox.ParticleEffect.ParticleModel;

public class EnemyGenerator implements Runnable {
    private List<Enemy> enemyList;
    private StaticTextures staticTextures;
    private ParticleModel particleModel;

    public EnemyGenerator(List<Enemy> enemyList, StaticTextures staticTextures, ParticleModel particleModel) {

        this.enemyList = enemyList;
        this.staticTextures = staticTextures;
        this.particleModel = particleModel;
    }

    private static void generateEnemy(List<Enemy> enemyList, StaticTextures staticTextures, ParticleModel particleModel) {

        enemyList.add(new Enemy(0, -1, 0, 0.001f, staticTextures, particleModel));
    }


    @Override
    public void run() {
        generateEnemy(enemyList, staticTextures, particleModel);
    }
}
