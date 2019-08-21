package TextCreator;

public class Characters {

    private int id;
    private float yPosition;
    private float xPosition;
    private float aspect;
    private float xOffset;
    private float yOffset;
    private float with;
    private float height;
    private float advance;


    public Characters(int id, float x, float y, float w, float h, float xOffset, float yOffset, float advance) {

        this.id = id;
        this.yPosition = 1.0f - (y + h) * FontRenderer.PIXEL_SIZE;
        this.xPosition = x * FontRenderer.PIXEL_SIZE;
        this.aspect = w / h;
        this.xOffset = xOffset * FontRenderer.PIXEL_SIZE;
        this.yOffset = yOffset * FontRenderer.PIXEL_SIZE;
        this.with = w * FontRenderer.PIXEL_SIZE;
        this.height = h * FontRenderer.PIXEL_SIZE;
        this.advance = advance * FontRenderer.PIXEL_SIZE + 2 * FontRenderer.PADDING;
    }

    public int getId() {
        return id;
    }

    public float getYPosition() {
        return yPosition;
    }

    public float getxPosition() {
        return xPosition;
    }

    public float getAspect() {
        return aspect;
    }

    public float getxOffset() {
        return xOffset;
    }

    public float getyOffset() {
        return yOffset;
    }

    public float getWith() {
        return with;
    }

    public float getHeight() {
        return height;
    }

    public float getAdvance() {
        return advance;
    }

}
