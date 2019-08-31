package pmos0011.biox.ParticleEffect;

import pmos0011.biox.AbstractClasses.ParticleEffects;
import pmos0011.biox.CommonObjects.Transformations;

public class FireParticleEffect extends ParticleEffects {


    public FireParticleEffect(effectKind effect, float worldAngle, float objectAngle, float effectOffset, float xPos, float yPos) {
        super(effect, worldAngle, objectAngle, effectOffset, xPos, yPos);

        Transformations.setModelTranslation(modelMatrix, worldAngle, objectAngle, xPos, yPos, 0.2f, -0.25f);
    }

    public void particleUpdate() {
        switch (getEffect()) {
            case CANNON_FIRE:
                fire();
                break;
        }
    }

    private void fire() {

        addTime(0.04f);
        //changeVisibility(-0.08f);
    }
}

