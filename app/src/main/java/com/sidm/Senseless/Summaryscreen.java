package com.sidm.Senseless;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

/**
 * Created by Aloysius Chun on 23/11/2015.
 */

public class  Summaryscreen extends Activity implements View.OnClickListener  {

    private Button btn_restart;
    private Button btn_main;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        requestWindowFeature(Window.FEATURE_NO_TITLE);// hide title
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); //hide top bar
        setContentView(R.layout.summaryscreen);

        btn_restart = (Button)findViewById(R.id.btn_restart);
        btn_restart.setOnClickListener(this);

        btn_main = (Button)findViewById(R.id.btn_main);
        btn_main.setOnClickListener(this);

    }
    public void onClick(View v)
    {
        Intent intent = new Intent();

        if(v ==  btn_restart)
            intent.setClass(this,Gamescreen.class);
        else if(v ==  btn_main)
            intent.setClass(this,Mainmenu.class);
        startActivity(intent);
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
