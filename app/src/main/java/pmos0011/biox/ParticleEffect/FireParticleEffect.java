package pmos0011.biox.ParticleEffect;

import pmos0011.biox.AbstractClasses.ParticleEffects;

public class FireParticleEffect extends ParticleEffects {


    public FireParticleEffect(effectKind effect, float wordAngle, float objectAngle, float xPos, float yPos) {
        super(effect, wordAngle, objectAngle, xPos, yPos);
    }

    public void particleUpdate() {
        switch (getEffect()) {
            case FIRE:
                fire();
                break;
        }
    }

    private void fire() {
        addTime(-0.005f);
    }
}

