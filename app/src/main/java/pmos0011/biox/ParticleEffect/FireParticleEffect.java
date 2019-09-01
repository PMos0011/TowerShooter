package pmos0011.biox.ParticleEffect;

import pmos0011.biox.AbstractClasses.ParticleEffects;
import pmos0011.biox.CommonObjects.Transformations;

public class FireParticleEffect extends ParticleEffects {


    public FireParticleEffect(effectKind effect, float worldAngle, float objectAngle, float effectOffset, float xPos, float yPos, float travelDistance) {
        super(effect, worldAngle, objectAngle, effectOffset, xPos, yPos, travelDistance);
    }

    @Override
    protected void particleInitial(float effectOffset, float xPos, float yPos) {
        switch (effect) {
            case CANNON_FIRE:
                Transformations.setModelTranslation(modelMatrix, worldAngle, objectAngle, xPos, yPos, 0.2f, -0.25f);
                this.innerColor = LIGHT_YELLOW.clone();
                this.outerColor = LIGHT_RED.clone();
                this.options = CANNON_FIRE.clone();
                break;
        }
    }

    public void particleUpdate() {
        switch (effect) {
            case CANNON_FIRE:
                fire();
                break;
        }
    }

    private void fire() {

        addTime(0.04f);
        changeVisibility(-0.08f);
    }
}

