package pmos0011.biox;

import android.content.Context;
import android.opengl.GLSurfaceView;

public class GamePlaySurfaceView extends GLSurfaceView {

    private final GameLoop renderer;

    public GamePlaySurfaceView(Context context) {
        super(context);

        setEGLContextClientVersion(3);

        renderer = new GameLoop(context);

        setRenderer(renderer);
        //setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }
}