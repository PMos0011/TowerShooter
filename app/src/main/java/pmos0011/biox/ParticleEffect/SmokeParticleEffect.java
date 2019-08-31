package pmos0011.biox.ParticleEffect;

import pmos0011.biox.AbstractClasses.ParticleEffects;
import pmos0011.biox.CommonObjects.Transformations;
import pmos0011.biox.GameLoopRenderer;

public class SmokeParticleEffect extends ParticleEffects {

    private float scale = 0.35f;


    public SmokeParticleEffect(effectKind effect, float worldAngle, float objectAngle, float effectOffset, float xPos, float yPos) {
        super(effect, worldAngle, objectAngle, effectOffset, xPos, yPos);

        particlePosition=Transformations.calculatePoint(worldAngle,effectOffset);
        particlePosition.x+=xPos;
        particlePosition.y+=yPos;

        Transformations.setModelTranslation(modelMatrix, this.worldAngle, 0, particlePosition.x, particlePosition.y, scale, scale);
    }

    public void particleUpdate() {
        switch (getEffect()) {
            case CANNON_SMOKE:
                cannonSmoke();
                break;
        }
    }

    private void cannonSmoke() {

        addTime(0.004f);

        particlePosition.x+= GameLoopRenderer.WIND_FLOW_X;
        particlePosition.y+=GameLoopRenderer.WIND_FLOW_Y;

        if(getInnerOpacity()<0.8f && getVisibility()==3.0f){
            changeOpacity(0.05f, 0.04f);
        }else{
            changeVisibility(-0.002f);
            //changeOpacity(-0.01f,-0.12f);
            scale+=0.001f;
        }


        Transformations.setModelTranslation(modelMatrix, this.worldAngle, 0, particlePosition.x, particlePosition.y, scale, scale);

    }
}
