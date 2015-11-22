package com.sidm.Senseless;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;

/**
 * Created by Edmund on 22/11/2015.
 */
public class Gamescreen extends Activity implements OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        requestWindowFeature(Window.FEATURE_NO_TITLE);// hide title
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); //hide top bar
        setContentView(R.layout.gamescreen);

    }
    public void onClick(View v)
    {
       /* Intent intent = new Intent();

        if(v==btn_start)
        {
            intent.setClass(this,Gamescreen.class);
        }
        else if(v==btn_help)
        {
            //TODO intent.setClass(this,HelpPage.class);
        }
        startActivity(intent);*/
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