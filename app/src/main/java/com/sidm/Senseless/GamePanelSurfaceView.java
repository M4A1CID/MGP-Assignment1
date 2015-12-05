package com.sidm.Senseless;
// Note: Differs with your project name

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Space;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class GamePanelSurfaceView extends SurfaceView implements SurfaceHolder.Callback{

    // Implement this interface to receive information about changes to the surface.

    String debugText;
    Random r = new Random();
    Paint paint = new Paint();
    HashMap<String,Enemy> cache = new HashMap<String,Enemy>();

    Enemy enemy_Cannabis = new Enemy();
    Enemy enemy_Cocaine = new Enemy();
    Enemy enemy_Ketamine = new Enemy();
    Enemy enemy_Ecstasy = new Enemy();
    Enemy enemy_Heroin = new Enemy();
    Maths myMath = new Maths();

    private int theScore;
    private int ScreenWidth, ScreenHeight;
    private int theLevel = 0;
    private int theKillCount = 0;
    private int theSpawnCount = 0;
    private SpriteAnimation stone_anim;
    private GameThread myThread = null; // Thread to control the rendering
    private Bitmap bg, scaledbg, Cannabis,Cocaine,Ketamine,Ecstasy,Heroin; //Bitmaps
    private float DPI, AspectRatioX, AspectRatioY;
    private float PlayerX = 0, PlayerY = 0, PlayerXScale = 0, PlayerYScale = 0;
    private float PlayerScale = 0;
    private float EnemyScale = 0;
    private float SpawnTimer = 0;
    private final float SpawnDelay = 2.f;
    public float FPS; // Variables for FPS
    private static final int PlayerArraySize = 3;
    private Bitmap[] PlayerFace = new Bitmap[PlayerArraySize];    //Init player bitmap
    private short PlayerIndex = 0;   //Player bitmap array count
    private short GameState;   // Variable for Game State check

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

        //Init the enemy
        EnemyScale  = AspectRatioY * 0.25f;
        Cannabis = BitmapFactory.decodeResource(getResources(), R.drawable.cannabis);
        Cannabis = Bitmap.createScaledBitmap(Cannabis, (int) EnemyScale, (int) EnemyScale, true);
        Cocaine = BitmapFactory.decodeResource(getResources(), R.drawable.cocaine);
        Cocaine = Bitmap.createScaledBitmap(Cocaine, (int) EnemyScale, (int) EnemyScale, true);
        Ketamine = BitmapFactory.decodeResource(getResources(), R.drawable.ketamine);
        Ketamine = Bitmap.createScaledBitmap(Ketamine, (int) EnemyScale, (int) EnemyScale, true);
        Ecstasy = BitmapFactory.decodeResource(getResources(), R.drawable.ecstasy);
        Ecstasy = Bitmap.createScaledBitmap(Ecstasy, (int) EnemyScale, (int) EnemyScale, true);
        Heroin = BitmapFactory.decodeResource(getResources(), R.drawable.heroin);
        Heroin = Bitmap.createScaledBitmap(Heroin, (int) EnemyScale, (int) EnemyScale, true);

        //Scale the bg
        //scaledbg = Bitmap.createScaledBitmap(bg, ScreenWidth, ScreenHeight, true);

        // 4c) Load the images of the spaceships
        PlayerFace[0] = BitmapFactory.decodeResource(getResources(), R.drawable.happy);
        PlayerFace[1] = BitmapFactory.decodeResource(getResources(), R.drawable.normal);
        PlayerFace[2] = BitmapFactory.decodeResource(getResources(), R.drawable.sad);

        PlayerScale = AspectRatioY * 0.75f;


        for ( int i = 0; i < PlayerArraySize; ++i)
        {
            PlayerFace[i] = Bitmap.createScaledBitmap(PlayerFace[i], (int)PlayerScale, (int)PlayerScale, true);
        }

        // Create the game loop thread
        myThread = new GameThread(getHolder(), this);

        // Make the GamePanel focusable so it can handle events
        setFocusable(true);

        stone_anim = new SpriteAnimation(BitmapFactory.decodeResource(getResources(), R.drawable.flystone), 320, 64, 5, 5);
        stone_anim.setX(300);
        stone_anim.setY(300);
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
        BitmapShader bs = new BitmapShader(bg, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
        Paint bgpaint = new Paint();
        bgpaint.setShader(bs);
        Matrix m = new Matrix();
        Rect rect = new Rect(0, 0, ScreenWidth, ScreenHeight);
        m.postTranslate(rect.left, rect.right);
        bgpaint.getShader().setLocalMatrix(m);
        canvas.drawRect(rect, bgpaint);

        // 4d) Draw the spaceships
        //(ScreenWidth * 0.5f) - (PlayerFace[PlayerIndex].getWidth() * 0.5f), (ScreenHeight * 0.5f) - (PlayerFace[PlayerIndex].getHeight() * 0.5f) - Shift from top left to middle for render
        // (1,1) (0,0) (0,0)     (0,0) (0,0) (0,0)    1 - co-ords
        // (0,0) (0,0) (0,0) ->  (0,0) (1,1) (0,0)    0 - your image
        // (0,0) (0,0) (0,0)     (0,0) (0,0) (0,0)

        PlayerX = (ScreenWidth * 0.5f);
        PlayerXScale = PlayerX  - (PlayerFace[PlayerIndex].getWidth() * 0.5f);
        PlayerY = (ScreenHeight * 0.5f);
        PlayerYScale = PlayerY - (PlayerFace[PlayerIndex].getHeight() * 0.5f);

        canvas.drawBitmap(PlayerFace[PlayerIndex], PlayerXScale, PlayerYScale, null);

        //Debug plane
        //canvas.drawBitmap(Cannabis, (ScreenWidth * 0.5f) - (Cannabis.getWidth() * 0.5f), (ScreenHeight * 0.5f) - (Cannabis.getHeight() * 0.5f), null);

        //stone_anim.draw(canvas);

        for (Map.Entry<String, Enemy> enemyMap: cache.entrySet())
        {
            float theEnemyX;
            float theEnemyY;
            Matrix matrix = new Matrix();

            switch (enemyMap.getValue().getM_ID())
            {
                case 0:
                    theEnemyX = enemyMap.getValue().getM_PosX() - (Cannabis.getWidth() * 0.5f);
                    theEnemyY = enemyMap.getValue().getM_PosY() - (Cannabis.getHeight() * 0.5f);
                    //matrix.setRotate(enemyMap.getValue().getM_Rotation(), theEnemyX, theEnemyY);
                    canvas.save();
                    canvas.rotate(enemyMap.getValue().getM_Rotation() - 90, enemyMap.getValue().getM_PosX(), enemyMap.getValue().getM_PosY());
                    canvas.drawBitmap(Cannabis, theEnemyX, theEnemyY, null);
                    canvas.restore();
                    break;
                case 1:
                    theEnemyX = enemyMap.getValue().getM_PosX() - (Cocaine.getWidth() * 0.5f);
                    theEnemyY = enemyMap.getValue().getM_PosY() - (Cocaine.getHeight() * 0.5f);
                    canvas.save();
                    canvas.rotate(enemyMap.getValue().getM_Rotation() - 90, theEnemyX, theEnemyY);
                    canvas.drawBitmap(Cocaine, theEnemyX, theEnemyY, null);
                    canvas.restore();
                    break;
                case 2:
                    theEnemyX = enemyMap.getValue().getM_PosX() - (Ketamine.getWidth() * 0.5f);
                    theEnemyY = enemyMap.getValue().getM_PosY() - (Ketamine.getHeight() * 0.5f);
                    canvas.save();
                    canvas.rotate(enemyMap.getValue().getM_Rotation() - 90, theEnemyX, theEnemyY);
                    canvas.drawBitmap(Ketamine, theEnemyX, theEnemyY, null);
                    canvas.restore();
                    break;
                case 3:
                    theEnemyX = enemyMap.getValue().getM_PosX() - (Ecstasy.getWidth() * 0.5f);
                    theEnemyY = enemyMap.getValue().getM_PosY() - (Ecstasy.getHeight() * 0.5f);
                    canvas.save();
                    canvas.rotate(enemyMap.getValue().getM_Rotation() - 90, theEnemyX, theEnemyY);
                    canvas.drawBitmap(Ecstasy, theEnemyX, theEnemyY, null);
                    canvas.restore();
                    break;
                case 4:
                    theEnemyX = enemyMap.getValue().getM_PosX() - (Heroin.getWidth() * 0.5f);
                    theEnemyY = enemyMap.getValue().getM_PosY() - (Heroin.getHeight() * 0.5f);
                    canvas.save();
                    canvas.rotate(enemyMap.getValue().getM_Rotation() - 90, theEnemyX, theEnemyY);
                    canvas.drawBitmap(Heroin, theEnemyX, theEnemyY, null);
                    canvas.restore();
                    break;
            }

        }

        //Text init
        paint.setColor(Color.WHITE);
        paint.setTextSize(20);
        String s_FPS = "FPS: " + String.valueOf(FPS);
        canvas.drawText(s_FPS, 25, 25, paint);

        if ( CheckCollision(PlayerX,PlayerY, stone_anim.getX(),stone_anim.getY()))
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
                SpawnTimer += dt;
                String temp;

                if ( SpawnTimer >= SpawnDelay )
                {
                    int theRandom = 0;
                    if ( theLevel != 0) {
                         theRandom = r.nextInt(theLevel);
                    }

                    switch ( theRandom )
                    {
                        case 0:
                            theSpawnCount++;
                            enemy_Cannabis = new Enemy();
                            temp = "Cannabis_";
                            temp += theSpawnCount;

                            enemy_Cannabis.Init(temp,100.0f,theLevel,theLevel,theLevel,0,
                                    myMath.getRandom(50, ScreenWidth,ScreenHeight).xRandom,myMath.getRandom(50, ScreenWidth, ScreenHeight).yRandom,
                                    PlayerX,PlayerY);
                            cache.put(temp,enemy_Cannabis);
                            break;
                        case 1:
                            theSpawnCount++;
                            enemy_Cocaine = new Enemy();
                            temp = "Cocaine_";
                            temp += theSpawnCount;
                            enemy_Cocaine.Init(temp,20.0f,theLevel * 2,theLevel * 2,theLevel * 2,1,
                                    myMath.getRandom(50, ScreenWidth,ScreenHeight).xRandom,myMath.getRandom(50, ScreenWidth, ScreenHeight).yRandom,
                                    PlayerX,PlayerY);
                            cache.put(temp,enemy_Cocaine);
                            break;
                        case 2:
                            theSpawnCount++;
                            enemy_Ketamine = new Enemy();
                            temp = "Ketamine_";
                            temp += theSpawnCount;
                            enemy_Ketamine.Init(temp,10.0f,theLevel * 2.5f,theLevel * 2.5f,theLevel * 2.5f,2,
                                    myMath.getRandom(50, ScreenWidth,ScreenHeight).xRandom,myMath.getRandom(50, ScreenWidth, ScreenHeight).yRandom,
                                    PlayerX,PlayerY);
                            cache.put(temp,enemy_Ketamine);
                            break;
                        case 3:
                            theSpawnCount++;
                            enemy_Ecstasy = new Enemy();
                            temp = "Ecstasy_";
                            temp += theSpawnCount;
                            enemy_Ecstasy.Init(temp,30.0f,theLevel / 2,theLevel / 2,theLevel / 2,3,
                                    myMath.getRandom(50, ScreenWidth,ScreenHeight).xRandom,myMath.getRandom(50, ScreenWidth, ScreenHeight).yRandom,
                                    PlayerX,PlayerY);
                            cache.put(temp,enemy_Ecstasy);
                            break;
                        case 4:
                            theSpawnCount++;
                            enemy_Heroin = new Enemy();
                            temp = "Heroin_";
                            temp += theSpawnCount;
                            enemy_Heroin.Init(temp,25.0f,theLevel * 1.5f,theLevel * 1.5f,theLevel * 1.5f,4,
                                    myMath.getRandom(50, ScreenWidth,ScreenHeight).xRandom,myMath.getRandom(50, ScreenWidth, ScreenHeight).yRandom,
                                    PlayerX,PlayerY);
                            cache.put(temp,enemy_Heroin);
                            break;
                    }

                    SpawnTimer = 0.0f;
                }

                for (Map.Entry<String, Enemy> enemyMap: cache.entrySet())
                {
                    enemyMap.getValue().update(dt);
                }

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
