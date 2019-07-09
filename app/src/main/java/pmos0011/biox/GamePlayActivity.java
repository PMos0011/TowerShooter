package pmos0011.biox;

import android.app.Activity;
import android.os.Bundle;

public class GamePlayActivity extends Activity {

    private GamePlaySurfaceView glView;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        glView = new GamePlaySurfaceView(this);
        setContentView(glView);
    }
}
