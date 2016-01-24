package com.sidm.Senseless;

import android.os.Handler;
import android.os.Looper;

/**
 * Created by User on 14/12/2015.
 */
public class Alert {
    private GamePanelSurfaceView Game;

    public Alert(GamePanelSurfaceView Game) {
        this.Game = Game;
    }

    public void RunAlert() {
        Handler handler = new Handler(Looper.getMainLooper());

        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                Game.alert.show();
            }
        }, 1000);
    }
}
