package com.sidm.Senseless;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

public class Mainmenu extends Activity implements OnClickListener
{

    private Button btn_start;
    private Button btn_help;
    private Button btn_option;
    private Button btn_druginfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        requestWindowFeature(Window.FEATURE_NO_TITLE);// hide title
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); //hide top bar


        setContentView(R.layout.mainmenu);

        btn_start = (Button)findViewById(R.id.btn_start);
        btn_start.setOnClickListener(this);

        btn_help = (Button)findViewById(R.id.btn_help);
        btn_help.setOnClickListener(this);

        btn_option = (Button)findViewById(R.id.btn_option);
        btn_option.setOnClickListener(this);

        btn_druginfo = (Button)findViewById(R.id.btn_druginfo);
        btn_druginfo.setOnClickListener(this);

    }

    public void onClick(View v)
    {
        Intent intent = new Intent();



        if(v == btn_start)
           intent.setClass(this,Gamescreen.class);
        else if(v == btn_help)
           intent.setClass(this,Helpscreen.class);
        else if(v == btn_option)
            intent.setClass(this,Optionscreen.class);
        else if(v == btn_druginfo)
            intent.setClass(this,Druginfoscreen.class);
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
