package TextCreator;

public class Characters {

    public static int id;
    public static float yPosition;
    public static float xPosition;
    public static float aspect;
    public static float xOffset;
    public static float yOffset;

    public Characters(int id, float x, float y, float w, float h, float xOffset, float yOffset){

        this.id=id;
        this.yPosition=1.0f-(y+h)*FontRenderer.PIXEL_SIZE;
        this.xPosition=x*FontRenderer.PIXEL_SIZE;
        this.aspect=w/h;
        this.xOffset=xOffset*FontRenderer.PIXEL_SIZE;
        this.yOffset=yOffset*FontRenderer.PIXEL_SIZE;

    }

}
