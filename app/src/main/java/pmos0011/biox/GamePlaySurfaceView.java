package pmos0011.biox;

import android.content.Context;
import android.graphics.PointF;
import android.opengl.GLSurfaceView;
import android.util.Log;
import android.view.MotionEvent;

public class GamePlaySurfaceView extends GLSurfaceView {

    private final GamePlayRenderer renderer;
    int pointerID = -1;

    public GamePlaySurfaceView(Context context) {
        super(context);

        setEGLContextClientVersion(3);

        renderer = new GamePlayRenderer(context);

        setRenderer(renderer);
        //setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {

        int test = e.getPointerCount();
        for (int i = 0; i < test; i++) {

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
                        renderer.rotateLeft = false;
                        renderer.rotateRight = false;
                        pointerID = -1;
                    }
            }
        }
        invalidate();
        return true;
    }

    boolean isMoveButtonPressed(float x, float y) {
        if (y > renderer.leftArrow.position.y - renderer.leftArrow.halfDimension
                && x < renderer.rightArrow.position.x + renderer.rightArrow.halfDimension)
            return true;
        else
            return false;
    }

    void moveDetection(float x, float y) {
        if (isMoveButtonPressed(x, y)) {
            if (x > renderer.leftArrow.position.x - renderer.leftArrow.halfDimension
                    && x < renderer.leftArrow.position.x + renderer.leftArrow.halfDimension) {
                renderer.rotateLeft = true;
                renderer.rotateRight = false;
            } else if (x > renderer.rightArrow.position.x - renderer.rightArrow.halfDimension) {
                renderer.rotateLeft = false;
                renderer.rotateRight = true;
            }
        } else {
            renderer.rotateLeft = false;
            renderer.rotateRight = false;
        }
    }

    void fireDetection(float x, float y) {
        if (y > renderer.leftCannonButton.position.y - renderer.leftCannonButton.halfDimension) {
            if (!renderer.isLeftCannonReloading) {
                if (x > renderer.leftCannonButton.position.x - renderer.leftCannonButton.halfDimension
                        && x < renderer.leftCannonButton.position.x + renderer.leftCannonButton.halfDimension) {
                    renderer.isLeftCannonReloading = true;
                }
            }
            if (!renderer.isRightCannonReloading) {
                if (x > renderer.rightCannonButton.position.x - renderer.rightCannonButton.halfDimension
                        && x < renderer.rightCannonButton.position.x + renderer.rightCannonButton.halfDimension) {
                    renderer.isRightCannonReloading = true;
                }
            }
        }
    }
}