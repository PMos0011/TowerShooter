package pmos0011.biox;

import android.opengl.GLES31;

public class StaticTextures extends StaticModel {

    public static final float GAME_CONTROL_OBJECT_SIZE = 0.18f;

    private final float TOWER_SIZE = 0.4f;
    private final float RADAR_SIZE = 0.065f;

    private GameControlObjects rightArrow = new GameControlObjects();
    private GameControlObjects leftArrow = new GameControlObjects();
    private GameControlObjects leftCannonButton = new GameControlObjects();
    private GameControlObjects rightCannonButton = new GameControlObjects();

    private boolean rotateLeft = false;
    private boolean rotateRight = false;
    private boolean isLeftCannonLoaded = true;
    private boolean isRightCannonLoaded = true;

    private float turretAngle = 0;
    private float rotateLeftSpeedDelta = 0;
    private float rotateRightSpeedDelta = 0;

    private float radarAngle = 0;

    public StaticTextures(int vaoID) {
        super(vaoID);
    }

    @Override
    protected void enableVertexArrays() {
        super.enableVertexArray(0);
        super.enableVertexArray(1);
    }

    @Override
    protected void drawElements(ObjectsLoader loader, Transformations transformations) {

        GLES31.glEnable(GLES31.GL_BLEND);
        GLES31.glBlendFunc(GLES31.GL_SRC_ALPHA, GLES31.GL_ONE_MINUS_SRC_ALPHA);

        drawBackground(loader, transformations);
        drawTowerBase(loader, transformations);
        drawLeftCannon(loader, transformations);
        drawRightCannon(loader, transformations);
        drawTurret(loader, transformations);
        drawRadar(loader, transformations);
        drawLeftRotateButton(loader, transformations);
        drawRightRotateButton(loader, transformations);
        drawLeftCannonButton(loader, transformations);
        drawRightCannonButton(loader, transformations);

        GLES31.glDisable(GLES31.GL_BLEND);

    }

    @Override
    protected void disableVertexArrays() {
        super.disableVertexArray(0);
        super.disableVertexArray(1);
    }

    private void drawBackground(ObjectsLoader loader, Transformations transformations) {
        transformations.setModelTranslation(0, 0, 0, 0, transformations.getRatio(), transformations.getRatio());
        loader.loadUniformMatrix4fv(shader.getModelMatrixHandle(), transformations.getModelMatrix());
        GLES31.glBindTexture(GLES31.GL_TEXTURE_2D, loader.getTextureID(BitmapID.textureNames.BACKGROUND.getValue()));
        GLES31.glDrawElements(GLES31.GL_TRIANGLES, StaticTextures.DRAW_ORDER.length, GLES31.GL_UNSIGNED_SHORT, 0);
    }

    private void drawTowerBase(ObjectsLoader loader, Transformations transformations) {
        transformations.setModelTranslation(0, 0, 0, 0, TOWER_SIZE, TOWER_SIZE);
        loader.loadUniformMatrix4fv(shader.getModelMatrixHandle(), transformations.getModelMatrix());
        GLES31.glBindTexture(GLES31.GL_TEXTURE_2D, loader.getTextureID(BitmapID.textureNames.TOWER_BASE.getValue()));
        GLES31.glDrawElements(GLES31.GL_TRIANGLES, StaticTextures.DRAW_ORDER.length, GLES31.GL_UNSIGNED_SHORT, 0);
    }

    private void drawLeftCannon(ObjectsLoader loader, Transformations transformations) {
        transformations.setModelTranslation(turretAngle, 0, 0, 0, TOWER_SIZE, TOWER_SIZE);
        loader.loadUniformMatrix4fv(shader.getModelMatrixHandle(), transformations.getModelMatrix());
        GLES31.glBindTexture(GLES31.GL_TEXTURE_2D, loader.getTextureID(BitmapID.textureNames.TOWER_LEFT_CANNON.getValue()));
        GLES31.glDrawElements(GLES31.GL_TRIANGLES, StaticTextures.DRAW_ORDER.length, GLES31.GL_UNSIGNED_SHORT, 0);
    }

    private void drawRightCannon(ObjectsLoader loader, Transformations transformations) {
        transformations.setModelTranslation(turretAngle, 0, 0, 0, TOWER_SIZE, TOWER_SIZE);
        loader.loadUniformMatrix4fv(shader.getModelMatrixHandle(), transformations.getModelMatrix());
        GLES31.glBindTexture(GLES31.GL_TEXTURE_2D, loader.getTextureID(BitmapID.textureNames.TOWER_RIGHT_CANNON.getValue()));
        GLES31.glDrawElements(GLES31.GL_TRIANGLES, StaticTextures.DRAW_ORDER.length, GLES31.GL_UNSIGNED_SHORT, 0);
    }

    private void drawTurret(ObjectsLoader loader, Transformations transformations) {
        transformations.setModelTranslation(turretAngle, 0, 0, 0, TOWER_SIZE, TOWER_SIZE);
        loader.loadUniformMatrix4fv(shader.getModelMatrixHandle(), transformations.getModelMatrix());
        GLES31.glBindTexture(GLES31.GL_TEXTURE_2D, loader.getTextureID(BitmapID.textureNames.TURRET.getValue()));
        GLES31.glDrawElements(GLES31.GL_TRIANGLES, StaticTextures.DRAW_ORDER.length, GLES31.GL_UNSIGNED_SHORT, 0);
    }

    private void drawRadar(ObjectsLoader loader, Transformations transformations) {
        transformations.setModelTranslation(turretAngle, radarAngle, 0.065f, -0.1f, RADAR_SIZE, RADAR_SIZE);
        loader.loadUniformMatrix4fv(shader.getModelMatrixHandle(), transformations.getModelMatrix());
        GLES31.glBindTexture(GLES31.GL_TEXTURE_2D, loader.getTextureID(BitmapID.textureNames.RADAR.getValue()));
        GLES31.glDrawElements(GLES31.GL_TRIANGLES, StaticTextures.DRAW_ORDER.length, GLES31.GL_UNSIGNED_SHORT, 0);
    }

    private void drawLeftRotateButton(ObjectsLoader loader, Transformations transformations) {
        transformations.setModelTranslation(0, turretAngle, leftArrow.getOpenGLPosition().x, leftArrow.getOpenGLPosition().y,
                GAME_CONTROL_OBJECT_SIZE, GAME_CONTROL_OBJECT_SIZE);
        loader.loadUniformMatrix4fv(shader.getModelMatrixHandle(), transformations.getModelMatrix());
        GLES31.glBindTexture(GLES31.GL_TEXTURE_2D, loader.getTextureID(BitmapID.textureNames.LEFT_ARROW.getValue()));
        GLES31.glDrawElements(GLES31.GL_TRIANGLES, StaticTextures.DRAW_ORDER.length, GLES31.GL_UNSIGNED_SHORT, 0);
    }

    private void drawRightRotateButton(ObjectsLoader loader, Transformations transformations) {
        transformations.setModelTranslation(0, turretAngle, rightArrow.getOpenGLPosition().x, rightArrow.getOpenGLPosition().y,
                GAME_CONTROL_OBJECT_SIZE, GAME_CONTROL_OBJECT_SIZE);
        loader.loadUniformMatrix4fv(shader.getModelMatrixHandle(), transformations.getModelMatrix());
        GLES31.glBindTexture(GLES31.GL_TEXTURE_2D, loader.getTextureID(BitmapID.textureNames.RIGHT_ARROW.getValue()));
        GLES31.glDrawElements(GLES31.GL_TRIANGLES, StaticTextures.DRAW_ORDER.length, GLES31.GL_UNSIGNED_SHORT, 0);
    }

    private void drawLeftCannonButton(ObjectsLoader loader, Transformations transformations) {
        transformations.setModelTranslation(0, 0, leftCannonButton.getOpenGLPosition().x, leftCannonButton.getOpenGLPosition().y,
                GAME_CONTROL_OBJECT_SIZE, GAME_CONTROL_OBJECT_SIZE);
        loader.loadUniformMatrix4fv(shader.getModelMatrixHandle(), transformations.getModelMatrix());
        GLES31.glBindTexture(GLES31.GL_TEXTURE_2D, loader.getTextureID(BitmapID.textureNames.LEFT_CANNON_BUTTON.getValue()));
        GLES31.glDrawElements(GLES31.GL_TRIANGLES, StaticTextures.DRAW_ORDER.length, GLES31.GL_UNSIGNED_SHORT, 0);
    }

    private void drawRightCannonButton(ObjectsLoader loader, Transformations transformations) {
        transformations.setModelTranslation(0, 0, rightCannonButton.getOpenGLPosition().x, rightArrow.getOpenGLPosition().y,
                GAME_CONTROL_OBJECT_SIZE, GAME_CONTROL_OBJECT_SIZE);
        loader.loadUniformMatrix4fv(shader.getModelMatrixHandle(), transformations.getModelMatrix());
        GLES31.glBindTexture(GLES31.GL_TEXTURE_2D, loader.getTextureID(BitmapID.textureNames.RIGHT_CANNON_BUTTON.getValue()));
        GLES31.glDrawElements(GLES31.GL_TRIANGLES, StaticTextures.DRAW_ORDER.length, GLES31.GL_UNSIGNED_SHORT, 0);
    }

    public void setGameButtons(int width, int height, float ratio, float[] projectionMatrix) {
        leftArrow.setObject(width, height, GAME_CONTROL_OBJECT_SIZE, -ratio + 1.2f * GAME_CONTROL_OBJECT_SIZE,
                -1 + GAME_CONTROL_OBJECT_SIZE, projectionMatrix);

        rightArrow.setObject(width, height, GAME_CONTROL_OBJECT_SIZE, -ratio + 3.4f * GAME_CONTROL_OBJECT_SIZE,
                -1 + GAME_CONTROL_OBJECT_SIZE, projectionMatrix);

        leftCannonButton.setObject(width, height, GAME_CONTROL_OBJECT_SIZE, ratio - 3.2f * GAME_CONTROL_OBJECT_SIZE,
                -1 + GAME_CONTROL_OBJECT_SIZE, projectionMatrix);

        rightCannonButton.setObject(width, height, GAME_CONTROL_OBJECT_SIZE, ratio - 1.1f * GAME_CONTROL_OBJECT_SIZE,
                -1 + GAME_CONTROL_OBJECT_SIZE, projectionMatrix);
    }

    public GameControlObjects getRightArrow() {
        return rightArrow;
    }

    public GameControlObjects getLeftArrow() {
        return leftArrow;
    }

    public GameControlObjects getLeftCannonButton() {
        return leftCannonButton;
    }

    public GameControlObjects getRightCannonButton() {
        return rightCannonButton;
    }

    public void enableLeftRotation() {
        rotateLeft = true;
        rotateRight = false;
    }

    public void enableRightRotation() {
        rotateLeft = false;
        rotateRight = true;
    }

    public void disableRotation() {
        rotateLeft = false;
        rotateRight = false;
    }

    public boolean isLeftCannonLoaded() {
        return isLeftCannonLoaded;
    }

    public boolean isRightCannonLoaded() {
        return isRightCannonLoaded;
    }

    public void fireFromLeft() {

    }

    public void fireFromRight() {

    }

    public void turretStateUpdate() {

        if (rotateLeft) {
            if (rotateLeftSpeedDelta < 0.3f)
                rotateLeftSpeedDelta += 0.005f;
        } else {
            if (rotateLeftSpeedDelta > 0)
                rotateLeftSpeedDelta -= 0.003f;
        }

        if (rotateRight) {
            if (rotateRightSpeedDelta < 0.3f)
                rotateRightSpeedDelta += 0.005f;
        } else {
            if (rotateRightSpeedDelta > 0)
                rotateRightSpeedDelta -= 0.003f;
        }

        turretAngle += rotateLeftSpeedDelta;
        turretAngle -= rotateRightSpeedDelta;

        if (turretAngle > 359.7)
            turretAngle = 0;
        if (turretAngle < 0)
            turretAngle = 359.7f;

        radarAngle -= 2;
        if (radarAngle < -358)
            radarAngle = 0;
    }


}
