package pmos0011.biox;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.Log;
import android.view.MotionEvent;

public class GamePlaySurfaceView extends GLSurfaceView {

    private final GamePlayRenderer renderer;
    final static String TAG = "Touch point ";

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

        Log.d(TAG,x+", "+y);
        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (y > 500) {
                    if (x < 200) {
                        renderer.rotateLeft = true;
                        renderer.rotateRight = false;
                    } else if (x > 500) {
                        renderer.rotateRight = true;
                        renderer.rotateLeft = false;
                    }
                }
                break;

            case MotionEvent.ACTION_MOVE:
                if (y > 500) {
                    if (x < 200) {
                        renderer.rotateLeft = true;
                        renderer.rotateRight = false;
                    } else if (x > 500) {
                        renderer.rotateRight = true;
                        renderer.rotateLeft = false;
                    }
                }
                else{
                    renderer.rotateRight = false;
                    renderer.rotateLeft = false;
                }
                break;

            case MotionEvent.ACTION_UP:
                renderer.rotateRight = false;
                renderer.rotateLeft = false;
                break;
        }

        return true;
    }
}