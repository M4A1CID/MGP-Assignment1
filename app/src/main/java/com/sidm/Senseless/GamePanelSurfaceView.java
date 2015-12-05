package com.sidm.Senseless;
// Note: Differs with your project name

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Space;

import java.util.Random;

public class GamePanelSurfaceView extends SurfaceView implements SurfaceHolder.Callback{

    // Implement this interface to receive information about changes to the surface.

    String debugText;
    Random r = new Random();
    Paint paint = new Paint();

    int theScore;

    private SpriteAnimation stone_anim;

    private GameThread myThread = null; // Thread to control the rendering

    // 1a) Variables used for background rendering
    private Bitmap bg, scaledbg;
    // 1b) Define Screen width and Screen height as integer
    int ScreenWidth, ScreenHeight;
    private float DPI, AspectRatioX, AspectRatioY;
    //Variables for shop
    private Bitmap btn_shop;
    private float btn_shop_Gamescale = 0.4f;
    private static final int PlayerArraySize = 3;
    //Init player bitmap
    private Bitmap[] PlayerFace = new Bitmap[PlayerArraySize];

   //Player bitmap array count
    private short PlayerIndex = 0;

    private float mX = 0, mY = 0;

    // Variables for FPS
    public float FPS;

    // Variable for Game State check
    private short GameState;

    //constructor for this GamePanelSurfaceView class
    public GamePanelSurfaceView (Context context){

        // Context is the current state of the application/object
        super(context);

        // Adding the callback (this) to the surface holder to intercept events
        getHolder().addCallback(this);

        // 1d) Set information to get screen size
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        ScreenWidth = metrics.widthPixels;
        ScreenHeight = metrics.heightPixels;

        DPI = metrics.density;

        AspectRatioX = ScreenWidth / DPI;
        AspectRatioY = ScreenHeight / DPI;
        // 1e)load the image when this class is being instantiated

        //Init the bg
        bg = BitmapFactory.decodeResource(getResources(), R.drawable.blue);
        //Scale the bg
        //scaledbg = Bitmap.createScaledBitmap(bg, ScreenWidth, ScreenHeight, true);

        // 4c) Load the images of the spaceships
        PlayerFace[0] = BitmapFactory.decodeResource(getResources(), R.drawable.happy);
        PlayerFace[1] = BitmapFactory.decodeResource(getResources(), R.drawable.normal);
        PlayerFace[2] = BitmapFactory.decodeResource(getResources(), R.drawable.sad);

        for ( int i = 0; i < PlayerArraySize; ++i)
        {
            PlayerFace[i] = Bitmap.createScaledBitmap(PlayerFace[i], (int) AspectRatioY, (int) AspectRatioY, true);
        }

        // Create the game loop thread
        myThread = new GameThread(getHolder(), this);

        // Make the GamePanel focusable so it can handle events
        setFocusable(true);

        stone_anim = new SpriteAnimation(BitmapFactory.decodeResource(getResources(), R.drawable.flystone), 320, 64, 5,5);
        stone_anim.setX(300);
        stone_anim.setY(300);

        // Variable for the shop
        btn_shop = BitmapFactory.decodeResource(getResources(), R.drawable.shop);
        btn_shop = Bitmap.createScaledBitmap(btn_shop,(int)( AspectRatioY * btn_shop_Gamescale), (int)( AspectRatioY* btn_shop_Gamescale), true);
    }

    //must implement inherited abstract methods
    public void surfaceCreated(SurfaceHolder holder){
        // Create the thread
        if (!myThread.isAlive()){
            myThread = new GameThread(getHolder(), this);
            myThread.startRun(true);
            myThread.start();
        }
    }

    public void surfaceDestroyed(SurfaceHolder holder){
        // Destroy the thread
        if (myThread.isAlive()){
            myThread.startRun(false);


        }
        boolean retry = true;
        while (retry) {
            try {
                myThread.join();
                retry = false;
            }
            catch (InterruptedException e)
            {
            }
        }
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height){

    }

    public void RenderGameplay(Canvas canvas) {
        // 2) Re-draw 2nd image after the 1st image ends
        if ( canvas == null )
        {
            return;
        }

        //Tile the bg
        for ( int i = 0; i < ScreenWidth; i += bg.getWidth())
        {
            for ( int j = 0; j < ScreenHeight; j += bg.getHeight()) {
                canvas.drawBitmap(bg, i, j, null);
            }
        }



        // 4d) Draw the spaceships
        //(ScreenWidth * 0.5f) - (PlayerFace[PlayerIndex].getWidth() * 0.5f), (ScreenHeight * 0.5f) - (PlayerFace[PlayerIndex].getHeight() * 0.5f) - Shift from top left to middle for render
        // (1,1) (0,0) (0,0)     (0,0) (0,0) (0,0)    1 - co-ords
        // (0,0) (0,0) (0,0) ->  (0,0) (1,1) (0,0)    0 - your image
        // (0,0) (0,0) (0,0)     (0,0) (0,0) (0,0)
        mX = (ScreenWidth * 0.5f) - (PlayerFace[PlayerIndex].getWidth() * 0.5f);
        mY = (ScreenHeight * 0.5f) - (PlayerFace[PlayerIndex].getHeight() * 0.5f);

        canvas.drawBitmap(PlayerFace[PlayerIndex],mX ,mY, null);

        // Bonus) To print FPS on the screen
        paint.setColor(Color.BLACK);
        paint.setTextSize(20);
        String s_FPS = "FPS: " + String.valueOf(FPS);
        canvas.drawText(s_FPS, 10, 25, paint);

        stone_anim.draw(canvas);
        //Draw the shop button
        canvas.drawBitmap(btn_shop,ScreenWidth - btn_shop.getWidth(),0,null);
        if ( CheckCollision(mX,mY, stone_anim.getX(),stone_anim.getY()))
        {
            theScore++;
            stone_anim.setX(r.nextInt(ScreenWidth - stone_anim.getSpriteWidth()));
            stone_anim.setY(r.nextInt(ScreenHeight - stone_anim.getSpriteHeight()));
        }
        else
        {
            debugText = " ";
            debugText += stone_anim.getX();
            debugText += ":";
            debugText += stone_anim.getY();

            canvas.drawText("No Collision", 500, 25, paint);
            //canvas.drawText(debugText, 500, 40, paint);
        }

        String Score = "Score: " + String.valueOf(theScore);
        canvas.drawText(Score, ScreenWidth - 150, 25, paint);
    }


    //Update method to update the game play
    public void update(float dt, float fps) {
        FPS = fps;

        switch (GameState) {
            case 0: {
                // 3) Update the background to allow panning effect

                // 4e) Update the spaceship images / shipIndex so that the animation will occur.

                stone_anim.update(System.currentTimeMillis());
            }
            break;
        }
    }

    // Rendering is done on Canvas
    public void doDraw(Canvas canvas){
        switch (GameState)
        {
            case 0:
                RenderGameplay(canvas);
                break;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){

        // 5) In event of touch on screen, the spaceship will relocate to the point of touch

        short X = (short) event.getX();
        short Y = (short) event.getY();

        if ( event.getAction() == MotionEvent.ACTION_DOWN )
        {
            //tap event - shoot
        }

/*        //Test
        Intent intent = new Intent();
        //if(v==btn_pause)
        //{
        intent.setClass(getContext(),Summaryscreen.class);
        //}
        getContext().startActivity(intent);*/

        return super.onTouchEvent(event);
    }

    public boolean CheckCollision(float x1, float y1, float x2, float y2)
    {
        double xDiff = (x1 + (PlayerFace[PlayerIndex].getWidth()/2) - (x2 + (stone_anim.getSpriteWidth()/2)));
        double yDiff = (y1 + (PlayerFace[PlayerIndex].getHeight()/2) - (y2 + (stone_anim.getSpriteHeight()/2)));
        double distance = Math.sqrt(xDiff * xDiff + yDiff * yDiff);

        double scale = PlayerFace[PlayerIndex].getWidth()/2;
        //double scale = Spaceship[SpaceshipIndex].getWidth()/2;

        if ( scale > distance )
        {
            return true;
        }
        else
        {
            return false;
        }
    }
}
