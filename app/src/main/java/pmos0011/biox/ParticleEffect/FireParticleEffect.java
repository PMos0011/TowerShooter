package pmos0011.biox.ParticleEffect;

import pmos0011.biox.AbstractClasses.ParticleEffects;
import pmos0011.biox.CommonObjects.Transformations;

public class FireParticleEffect extends ParticleEffects {


    public FireParticleEffect(effectKind effect, float worldAngle, float objectAngle, float effectOffset, float xPos, float yPos,float size, float travelDistance) {
        super(effect, worldAngle, objectAngle, effectOffset, xPos, yPos, size, travelDistance);
    }

    @Override
    protected void particleInitial(float effectOffset, float xPos, float yPos, float size) {
        switch (effect) {
            case CANNON_FIRE:

                particlePosition = Transformations.calculatePoint(worldAngle, effectOffset);
                particlePosition.x += xPos;
                particlePosition.y += yPos;
                scaleX = 22*size;
                scaleY = -26*size;

                Transformations.setModelTranslation(modelMatrix, 0, worldAngle, particlePosition.x, particlePosition.y, 22*size, -26*size);
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

