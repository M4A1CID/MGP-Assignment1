package com.sidm.Senseless;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

public class ScorePage extends Activity implements OnClickListener
{
    private Button btn_back;
    AlertDialog.Builder alert_score = null;
    SharedPreferences SharedPrefScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        requestWindowFeature(Window.FEATURE_NO_TITLE);// hide title
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); //hide top bar


        setContentView(R.layout.mainmenu);

        btn_back = (Button)findViewById(R.id.btn_highscore);
        btn_back.setOnClickListener(this);
    }

    public void onClick(View v)
    {
        Intent intent = new Intent();
        if (v == btn_back) {
            intent.setClass(this, Mainmenu.class);
        }
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
