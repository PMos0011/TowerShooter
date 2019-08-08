package pmos0011.biox;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.Log;
import android.view.MotionEvent;

public class GamePlaySurfaceView extends GLSurfaceView {

    private final GamePlayRenderer renderer;

    public GamePlaySurfaceView(Context context) {
        super(context);

        setEGLContextClientVersion(3);

        renderer = new GamePlayRenderer(context);

        setRenderer(renderer);
        //setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        float x = e.getX();
        float y = e.getY();

        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (y > renderer.leftArrow.position.y-renderer.leftArrow.halfDimension) {
                    if (x > renderer.leftArrow.position.x-renderer.leftArrow.halfDimension
                    && x<renderer.leftArrow.position.x+renderer.leftArrow.halfDimension) {
                        renderer.rotateLeft = false;
                        renderer.rotateRight = true;
                    } else if (x > renderer.rightArrow.position.x-renderer.rightArrow.halfDimension
                            && x<renderer.rightArrow.position.x+renderer.rightArrow.halfDimension) {
                        renderer.rotateLeft = true;
                        renderer.rotateRight = false;
                    }
                }
                break;

            case MotionEvent.ACTION_MOVE:
                if (y > renderer.leftArrow.position.y-renderer.leftArrow.halfDimension) {
                    if (x > renderer.leftArrow.position.x-renderer.leftArrow.halfDimension
                            && x<renderer.leftArrow.position.x+renderer.leftArrow.halfDimension) {
                        renderer.rotateLeft = false;
                        renderer.rotateRight = true;
                    } else if (x > renderer.rightArrow.position.x-renderer.rightArrow.halfDimension
                            && x<renderer.rightArrow.position.x+renderer.rightArrow.halfDimension) {
                        renderer.rotateLeft = true;
                        renderer.rotateRight = false;
                    }else{
                        renderer.rotateLeft = false;
                        renderer.rotateRight = false;
                    }
                }
                else{
                    renderer.rotateLeft = false;
                    renderer.rotateRight = false;
                }
                break;

            case MotionEvent.ACTION_UP:
                renderer.rotateLeft = false;
                renderer.rotateRight = false;
                break;
        }

        return true;
    }
}