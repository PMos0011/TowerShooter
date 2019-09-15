package pmos0011.biox;

import android.content.Context;
import android.graphics.Point;
import android.graphics.PointF;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

import pmos0011.biox.StaticTextures.StaticTexturesRenderer;

public class GamePlaySurfaceView extends GLSurfaceView {

    private final GameLoopRenderer renderer;
    private StaticTexturesRenderer staticTexturesRenderer;
    private Point leftRotate;
    private Point rightRotate;
    private Point leftCannon;
    private Point rightCanon;
    private int halfDimension;

    int pointerID = -1;

    public GamePlaySurfaceView(Context context) {
        super(context);

        setEGLContextClientVersion(3);

        renderer = new GameLoopRenderer(context);
        setRenderer(renderer);

        //setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);

    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {

        getButtonsSetting();
        int pointerCount = e.getPointerCount();

        if (!GameLoopRenderer.isGamePlay()) {
            GameLoopRenderer.restartGame = true;
        } else {

            for (int i = 0; i < pointerCount; i++) {

                PointF point = new PointF();
                point.x = e.getX(i);
                point.y = e.getY(i);
                switch (e.getActionMasked()) {

                    case MotionEvent.ACTION_DOWN:
                    case MotionEvent.ACTION_POINTER_DOWN:

                        if (isMoveButtonPressed(point.x, point.y))
                            pointerID = e.getPointerId(i);
                        if (pointerID == e.getPointerId(i))
                            moveDetection(point.x, point.y);
                        fireDetection(point.x, point.y);
                        break;

                    case MotionEvent.ACTION_MOVE:
                        if (pointerID == e.getPointerId(i))
                            moveDetection(point.x, point.y);
                        break;

                    case MotionEvent.ACTION_UP:
                        if (pointerID == e.getPointerId(i)) {
                            staticTexturesRenderer.disableRotation();
                            pointerID = -1;
                        }
                }
            }
            invalidate();
        }
        return true;
    }

    boolean isMoveButtonPressed(float x, float y) {
        if (y > leftRotate.y - halfDimension && x < rightRotate.x + halfDimension)
            return true;
        else
            return false;
    }

    void moveDetection(float x, float y) {
        if (isMoveButtonPressed(x, y)) {
            if (x > leftRotate.x - halfDimension && x < leftRotate.x + halfDimension)
                staticTexturesRenderer.enableLeftRotation();
            else if (x > rightRotate.x - halfDimension)
                staticTexturesRenderer.enableRightRotation();
        } else
            staticTexturesRenderer.disableRotation();
    }

    void fireDetection(float x, float y) {
        if (y > leftCannon.y - halfDimension) {
            if (staticTexturesRenderer.isLeftCannonLoaded())
                if (x > leftCannon.x - halfDimension && x < leftCannon.x + halfDimension)
                    staticTexturesRenderer.fireFromLeft();

            if (staticTexturesRenderer.isRightCannonLoaded())
                if (x > rightCanon.x - halfDimension && x < rightCanon.x + halfDimension)
                    staticTexturesRenderer.fireFromRight();
        }
    }

    private void getButtonsSetting() {

        if (staticTexturesRenderer == null) {

            staticTexturesRenderer = renderer.getStaticTexturesRenderer();

            leftRotate = staticTexturesRenderer.getLeftArrow().getPosition();
            rightRotate = staticTexturesRenderer.getRightArrow().getPosition();
            leftCannon = staticTexturesRenderer.getLeftCannonButton().getPosition();
            rightCanon = staticTexturesRenderer.getRightCannonButton().getPosition();

            halfDimension = staticTexturesRenderer.getLeftArrow().getHalfDimension();
        }
    }
}