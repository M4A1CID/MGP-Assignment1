package com.sidm.Senseless;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;

/**
 * Created by Edmund on 22/11/2015.
 */
public class Gamescreen extends Activity implements OnClickListener{

    //private Button btn_pause;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        requestWindowFeature(Window.FEATURE_NO_TITLE);// hide title
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); //hide top bar
        setContentView(new GamePanelSurfaceView(this));

        //btn_pause = (Button)findViewById(R.id.btn_pause);
        //btn_pause.setOnClickListener(this);

    }
    public void onClick(View v)
    {
       Intent intent = new Intent();

        //if(v==btn_pause)
        //{
        //    intent.setClass(this,Summaryscreen.class);
        //}
        //startActivity(intent);
    }

    protected void onPause()
    {
        super.onPause();
    }

    protected  void onStop()
    {
        super.onStop();
    }
    protected void onDestroy()
    {
        super.onDestroy();
    }

}