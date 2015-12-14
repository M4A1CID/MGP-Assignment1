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
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Vibrator;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Space;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

public class GamePanelSurfaceView extends SurfaceView implements SurfaceHolder.Callback {

    // Implement this interface to receive information about changes to the surface.

    String debugText;
    Random r = new Random();
    Paint paint = new Paint();
    ConcurrentHashMap<String, Enemy> cache = new ConcurrentHashMap<String, Enemy>();
    ConcurrentHashMap<String, Bullet> bulletcache = new ConcurrentHashMap<String, Bullet>();
    ConcurrentHashMap<String, SpriteAnimation> animcache = new ConcurrentHashMap<String, SpriteAnimation>();

    // Use of vibration for feedback
    public Vibrator v;


    Enemy theEnemy = new Enemy();
    Bullet theBullet = new Bullet();
    Maths myMath = new Maths();
    PlayerClass thePlayer = new PlayerClass();

    private int theScore = 0;
    private int ScreenWidth, ScreenHeight;
    private int ScreenWidthCenter, ScreenHeightCenter;
    private int theLevel = 1;
    private int theKillCount = 0;
    private int theSpawnCount = 0;
    private int theBulletCount = 0;
    private final int smoke_frame_count = 5;
    private GameThread myThread = null; // Thread to control the rendering
    private Bitmap bg, Cannabis, Cocaine, Ketamine, Ecstasy, Heroin, btn_shop, btn_shopScreen, bullet, smoke_resize, btn_back; //Bitmaps
    private float DPI, AspectRatioX, AspectRatioY;
    private float SpawnTimer = 0;
    private final float SpawnDelay = 2.f;

    public float FPS; // Variables for FPS
    private SpriteAnimation smoke_anim;
    private short GameState;   // Variable for Game State check
    //Variables for shop
    private float btn_shop_Gamescale = 0.3f;
    private float btn_shopScreen_Gamescale = 0.5f;
    private float btn_shop_X, btn_shop_Y;
    private boolean btn_shop_opened = false;

    //Variables for back button
    private float btn_back_X, btn_back_Y;
    private float btn_back_Gamescale = 0.3f;

    private short lastX;
    private short lastY;

    // Use of music for background
    MediaPlayer bgm;
    // Use of sound for game
    private SoundPool sounds;
    private int soundcorrect,soundwrong,soundbonus,shooting,shop,explosion,enemyhurt;
    public void InitSoundEffects(Context context)
    {
        // Variables used for music and sound
        bgm = MediaPlayer.create(context,R.raw.background_music);
        // Define Soundpool will be used
        sounds = new SoundPool(4, AudioManager.STREAM_MUSIC,0);
        // Load the audio file from specified
        soundcorrect = sounds.load(context,R.raw.correct,1);
        soundwrong = sounds.load(context,R.raw.incorrect,1);
        shooting = sounds.load(context,R.raw.lasershoot,1);
        shop = sounds.load(context,R.raw.shop,1);
        explosion = sounds.load(context,R.raw.explosion,1);
        enemyhurt = sounds.load(context,R.raw.enemyhurt,1);
    }
    public void AudioCleanUp()
    {
        //End background music
        bgm.stop();
        bgm.release();

        // End audio file
        sounds.unload(soundcorrect);
        sounds.unload(soundwrong);
        sounds.unload(shop);
        sounds.unload(shooting);
        sounds.unload(explosion);
        sounds.unload(enemyhurt);
        sounds.release();
    }

    //constructor for this GamePanelSurfaceView class
    public GamePanelSurfaceView(Context context) {

        // Context is the current state of the application/object
        super(context);

        // Adding the callback (this) to the surface holder to intercept events
        getHolder().addCallback(this);

        // Init 20 bullets into the bullet hashmap
        for (int i = 0; i < 20; ++i) {
            theBulletCount++;
            theBullet = new Bullet();
            String bulletID = "Bullet_";
            bulletID += theBulletCount;
            theBullet.Init(bulletID, thePlayer.getM_Damage(), thePlayer.getM_PosX(), thePlayer.getM_PosY(), 0, 0, false);
            bulletcache.put(bulletID, theBullet);
        }
        // Init 20 enemies into the hashmap
        for (int i = 0; i < 20; ++i) {
            theSpawnCount++;
            theEnemy = new Enemy();
            String temp = "Unknown_";
            temp += theSpawnCount;

            theEnemy.Init(temp, 20.0f, 0, 0, 0, 0, 0, 0, 0, 0, false);
            cache.put(temp, theEnemy);
        }

        // 1d) Set information to get screen size
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        ScreenWidth = metrics.widthPixels;
        ScreenHeight = metrics.heightPixels;
        ScreenWidthCenter = ScreenWidth / 2;
        ScreenHeightCenter = ScreenHeight / 2;

        DPI = metrics.density;

        AspectRatioX = ScreenWidth / DPI;
        AspectRatioY = ScreenHeight / DPI;
        // 1e)load the image when this class is being instantiated

        //Init the bg
        bg = BitmapFactory.decodeResource(getResources(), R.drawable.blue);

        //Init the enemy
        theEnemy.setEnemyScale( AspectRatioY * 0.25f);
        Cannabis = BitmapFactory.decodeResource(getResources(), R.drawable.cannabis);
        Cannabis = Bitmap.createScaledBitmap(Cannabis, (int) theEnemy.getEnemyScale(), (int) theEnemy.getEnemyScale(), true);
        Cocaine = BitmapFactory.decodeResource(getResources(), R.drawable.cocaine);
        Cocaine = Bitmap.createScaledBitmap(Cocaine, (int) theEnemy.getEnemyScale(), (int) theEnemy.getEnemyScale(), true);
        Ketamine = BitmapFactory.decodeResource(getResources(), R.drawable.ketamine);
        Ketamine = Bitmap.createScaledBitmap(Ketamine, (int) theEnemy.getEnemyScale(), (int) theEnemy.getEnemyScale(), true);
        Ecstasy = BitmapFactory.decodeResource(getResources(), R.drawable.ecstasy);
        Ecstasy = Bitmap.createScaledBitmap(Ecstasy, (int) theEnemy.getEnemyScale(), (int) theEnemy.getEnemyScale(), true);
        Heroin = BitmapFactory.decodeResource(getResources(), R.drawable.heroin);
        Heroin = Bitmap.createScaledBitmap(Heroin, (int) theEnemy.getEnemyScale(), (int)theEnemy.getEnemyScale(), true);

        theBullet.setBulletScaleX(AspectRatioX * 0.025f);
        theBullet.setBulletScaleY(AspectRatioY * 0.2f);
        bullet = BitmapFactory.decodeResource(getResources(), R.drawable.laser);
        bullet = Bitmap.createScaledBitmap(bullet, (int) theBullet.getBulletScaleX(), (int) theBullet.getBulletScaleY(), true);

        float SmokeScaleX = AspectRatioX;
        float SmokeScaleY = AspectRatioY * 0.3f;
        smoke_resize = BitmapFactory.decodeResource(getResources(), R.drawable.smoke);
        smoke_resize = Bitmap.createScaledBitmap(smoke_resize, (int) SmokeScaleX, (int) SmokeScaleY, true);
        smoke_anim = new SpriteAnimation(smoke_resize, 320, 64, 5, smoke_frame_count);

        //Player face init
        thePlayer.PlayerFace[0] = BitmapFactory.decodeResource(getResources(), R.drawable.happy);
        thePlayer.PlayerFace[1] = BitmapFactory.decodeResource(getResources(), R.drawable.normal);
        thePlayer.PlayerFace[2] = BitmapFactory.decodeResource(getResources(), R.drawable.sad);
        thePlayer.setM_PlayerScale( AspectRatioY * 0.5f);

        for ( int i = 0; i < thePlayer.getPlayerArraySize(); ++i)
        {
            thePlayer.PlayerFace[i] = Bitmap.createScaledBitmap(thePlayer.PlayerFace[i], (int)thePlayer.getM_PlayerScale(), (int)thePlayer.getM_PlayerScale(), true);
        }

        // Create the game loop thread
        myThread = new GameThread(getHolder(), this);

        // Make the GamePanel focusable so it can handle events
        setFocusable(true);

/*        stone_anim = new SpriteAnimation(BitmapFactory.decodeResource(getResources(), R.drawable.flystone), 320, 64, 5, 5);
        stone_anim.setX(300);
        stone_anim.setY(300);*/

        // Variable for the shop
        btn_shop = BitmapFactory.decodeResource(getResources(), R.drawable.shop);
        btn_shop = Bitmap.createScaledBitmap(btn_shop, (int) (AspectRatioY * btn_shop_Gamescale), (int) (AspectRatioY * btn_shop_Gamescale), true);
        btn_shopScreen = BitmapFactory.decodeResource(getResources(), R.drawable.shop_screen);
        btn_shopScreen = Bitmap.createScaledBitmap(btn_shopScreen, ScreenWidth, ScreenHeight, true);
        btn_shop_X = ScreenWidth - btn_shop.getWidth();
        btn_shop_Y = 0;

        //Variable for the back button
        btn_back = BitmapFactory.decodeResource(getResources(), R.drawable.back);
        btn_back = Bitmap.createScaledBitmap(btn_back, (int) (AspectRatioY * btn_back_Gamescale), (int) (AspectRatioY * btn_back_Gamescale), true);
        btn_back_X = ScreenWidth - btn_back.getWidth();
        btn_back_Y = ScreenHeight - btn_back.getHeight();

        InitSoundEffects(context);

    }

    //must implement inherited abstract methods
    public void surfaceCreated(SurfaceHolder holder) {
        // Create the thread
        if (!myThread.isAlive()) {
            myThread = new GameThread(getHolder(), this);
            myThread.startRun(true);
            myThread.start();

            // Start the background music
            bgm.setVolume(0.8f,0.8f);
            bgm.start();
        }
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        // Destroy the thread
        if (myThread.isAlive()) {
            myThread.startRun(false);
        }
        boolean retry = true;
        while (retry) {
            try {
                myThread.join();
                retry = false;
            } catch (InterruptedException e) {
            }
        }

        AudioCleanUp();

    }

    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    public void RenderEnemy(Canvas canvas) {
        for (Map.Entry<String, Enemy> enemyMap : cache.entrySet()) {
            Enemy theEnemy = enemyMap.getValue();
            if (theEnemy.getM_Active()) {
                float theEnemyX = theEnemy.getM_PosX();
                float theEnemyY = theEnemy.getM_PosY();

                switch (enemyMap.getValue().getM_ID()) {
                    case 0:
                        theEnemyX -= (Cannabis.getWidth() * 0.5f);
                        theEnemyY -= (Cannabis.getHeight() * 0.5f);
                        canvas.save();
                        canvas.rotate(theEnemy.getM_Rotation() - 90, theEnemy.getM_PosX(), theEnemy.getM_PosY());
                        canvas.drawBitmap(Cannabis, theEnemyX, theEnemyY, null);
                        canvas.restore();
                        break;
                    case 1:
                        theEnemyX -= (Cocaine.getWidth() * 0.5f);
                        theEnemyY -= (Cocaine.getHeight() * 0.5f);
                        canvas.save();
                        canvas.rotate(theEnemy.getM_Rotation() - 90, theEnemy.getM_PosX(), theEnemy.getM_PosY());
                        canvas.drawBitmap(Cocaine, theEnemyX, theEnemyY, null);
                        canvas.restore();
                        break;
                    case 2:
                        theEnemyX -= (Ketamine.getWidth() * 0.5f);
                        theEnemyY -= (Ketamine.getHeight() * 0.5f);
                        canvas.save();
                        canvas.rotate(theEnemy.getM_Rotation() - 90, theEnemy.getM_PosX(), theEnemy.getM_PosY());
                        canvas.drawBitmap(Ketamine, theEnemyX, theEnemyY, null);
                        canvas.restore();
                        break;
                    case 3:
                        theEnemyX -= (Ecstasy.getWidth() * 0.5f);
                        theEnemyY -= (Ecstasy.getHeight() * 0.5f);
                        canvas.save();
                        canvas.rotate(theEnemy.getM_Rotation() - 90, theEnemy.getM_PosX(), theEnemy.getM_PosY());
                        canvas.drawBitmap(Ecstasy, theEnemyX, theEnemyY, null);
                        canvas.restore();
                        break;
                    case 4:
                        theEnemyX -= (Heroin.getWidth() * 0.5f);
                        theEnemyY -= (Heroin.getHeight() * 0.5f);
                        canvas.save();
                        canvas.rotate(theEnemy.getM_Rotation() - 90, theEnemy.getM_PosX(), theEnemy.getM_PosY());
                        canvas.drawBitmap(Heroin, theEnemyX, theEnemyY, null);
                        canvas.restore();
                        break;
                }
            }
        }
    }

    public void RenderTiledBackground(Canvas canvas) {
        //Tile the bg
        BitmapShader bs = new BitmapShader(bg, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
        Paint bgpaint = new Paint();
        bgpaint.setShader(bs);
        Matrix m = new Matrix();
        Rect rect = new Rect(0, 0, ScreenWidth, ScreenHeight);
        m.postTranslate(rect.left, rect.right);
        bgpaint.getShader().setLocalMatrix(m);
        canvas.drawRect(rect, bgpaint);
    }

    public void RenderPlayer(Canvas canvas) {
        //(ScreenWidth * 0.5f) - (PlayerFace[PlayerIndex].getWidth() * 0.5f), (ScreenHeight * 0.5f) - (PlayerFace[PlayerIndex].getHeight() * 0.5f) - Shift from top left to middle for render
        // (1,1) (0,0) (0,0)     (0,0) (0,0) (0,0)    1 - co-ords
        // (0,0) (0,0) (0,0) ->  (0,0) (1,1) (0,0)    0 - your image
        // (0,0) (0,0) (0,0)     (0,0) (0,0) (0,0)
        thePlayer.setM_PosX((ScreenWidth * 0.5f));
        thePlayer.setM_PlayerXScale(thePlayer.getM_PosX() - (thePlayer.PlayerFace[thePlayer.getPlayerIndex()].getWidth() * 0.5f));
        thePlayer.setM_PosY((ScreenHeight * 0.5f));
        thePlayer.setM_PlayerYScale(thePlayer.getM_PosY() - (thePlayer.PlayerFace[thePlayer.getPlayerIndex()].getHeight() * 0.5f));

        canvas.drawBitmap(thePlayer.PlayerFace[thePlayer.getPlayerIndex()], thePlayer.getM_PlayerXScale(), thePlayer.getM_PlayerYScale(), null);
    }

    public void RenderBullets(Canvas canvas) {
        for (Map.Entry<String, Bullet> bulletMap2 : bulletcache.entrySet()) {
            if (bulletMap2.getValue().getM_Active()) {
                float BulletX = bulletMap2.getValue().getM_PosX() - (bullet.getWidth() * 0.5f);
                float BulletY = bulletMap2.getValue().getM_PosY() - (bullet.getHeight() * 0.5f);

                canvas.save();
                canvas.rotate(bulletMap2.getValue().getM_Rotation() + 90, bulletMap2.getValue().getM_PosX(), bulletMap2.getValue().getM_PosY());
                canvas.drawBitmap(bullet, BulletX, BulletY, null);
                canvas.restore();
            }
        }
    }

    public void RenderSmoke(Canvas canvas) {
        //The smoke
        for (Map.Entry<String, SpriteAnimation> smokeMap : animcache.entrySet()) {
            smokeMap.getValue().draw(canvas);
        }
    }

    public void RenderGUI(Canvas canvas) {
        //Text init
        paint.setColor(Color.WHITE);
        paint.setTextSize(20);
        String s_FPS = "FPS: " + String.valueOf(FPS);
        canvas.drawText(s_FPS, 25, 25, paint);

        String Score = "Score: " + String.valueOf(thePlayer.getM_Score());
        canvas.drawText(Score, ScreenWidth - 150, 25, paint);

        String Level = "Level: ";
        Level += theLevel;
        canvas.drawText(Level, 500, 25, paint);

        //Draw the player's current amount of Gold
        String Gold = "Gold: " + String.valueOf(thePlayer.getM_Gold());
        canvas.drawText(Gold, ScreenWidth - 150, 50, paint);

        //Draw the player;'s current Gold Multiplyer
        String multiplyer = "G-Multi: x" + String.valueOf(thePlayer.getM_Gold_Multiplyer_Level());
        canvas.drawText(multiplyer, ScreenWidth - 150, 75, paint);

        //Draw the shop button
        if (btn_shop_opened) { // If player pressed the shop button
            // Render the shop screen overlay
            canvas.drawBitmap(btn_shopScreen, 1, 1, null);
            canvas.drawBitmap(btn_back, btn_back_X, btn_back_Y, null);
        }
    }

    public void RenderGameplay(Canvas canvas) {
        // 2) Re-draw 2nd image after the 1st image ends
        if (canvas == null) {
            return;
        }

        RenderTiledBackground(canvas);
        RenderBullets(canvas);
        RenderPlayer(canvas);
        RenderEnemy(canvas);
        RenderSmoke(canvas);
        RenderGUI(canvas);
    }

    public void spawnEnemy(float dt) {
        int theRandom = 0;
        String temp;

        if (theLevel != 0) {
            theRandom = r.nextInt(theLevel);
        }
        for (Map.Entry<String, Enemy> enemyMap : cache.entrySet()) {
            Enemy theIT = enemyMap.getValue();
            if (theIT.getM_Active() == false) // If this enemy is not active
            {
                switch (theRandom) {
                    case 0:
                        theIT.Init("Cannabis", 20.0f, theLevel, theLevel, theLevel, 0,
                                myMath.getRandom(50, ScreenWidth, ScreenHeight).xRandom, myMath.getRandom(50, ScreenWidth, ScreenHeight).yRandom,
                                thePlayer.getM_PosX(), thePlayer.getM_PosY(), true);
                        break;
                    case 1:
                        theIT.Init("Cocaine", 40.0f, theLevel * 2, theLevel * 2, theLevel * 2, 1,
                                myMath.getRandom(50, ScreenWidth, ScreenHeight).xRandom, myMath.getRandom(50, ScreenWidth, ScreenHeight).yRandom,
                                thePlayer.getM_PosX(), thePlayer.getM_PosY(), true);
                        break;
                    case 2:

                        theIT.Init("Ketamine", 20.0f, theLevel * 2.5f, theLevel * 2.5f, theLevel * 2.5f, 2,
                                myMath.getRandom(50, ScreenWidth, ScreenHeight).xRandom, myMath.getRandom(50, ScreenWidth, ScreenHeight).yRandom,
                                thePlayer.getM_PosX(), thePlayer.getM_PosY(), true);
                        break;
                    case 3:
                        theIT.Init("Ecstasy", 60.0f, theLevel / 2, theLevel / 2, theLevel / 2, 3,
                                myMath.getRandom(50, ScreenWidth, ScreenHeight).xRandom, myMath.getRandom(50, ScreenWidth, ScreenHeight).yRandom,
                                thePlayer.getM_PosX(), thePlayer.getM_PosY(), true);
                        break;
                    case 4:
                        theIT.Init("Heroin", 50.0f, theLevel * 1.5f, theLevel * 1.5f, theLevel * 1.5f, 4,
                                myMath.getRandom(50, ScreenWidth, ScreenHeight).xRandom, myMath.getRandom(50, ScreenWidth, ScreenHeight).yRandom,
                                thePlayer.getM_PosX(), thePlayer.getM_PosY(), true);
                        break;
                }
                break; // Break out of For-loop
            }
        }
    }

    //Update method to update the game play
    public void update(float dt, float fps) {
        FPS = fps;

        switch (GameState) {
            case 0: {
                SpawnTimer += dt;
                thePlayer.setM_Time_Last_Attacked(thePlayer.getM_Time_Last_Attacked() + dt);

                if (SpawnTimer >= SpawnDelay) {
                    spawnEnemy(dt);
                    SpawnTimer = 0.0f;
                }

                if ( thePlayer.isM_IsShoot()) {
                    HandleBulletShoot(lastX, lastY);
                }

                //The smoke
                for (Map.Entry<String, SpriteAnimation> smokeMap : animcache.entrySet()) {
                    SpriteAnimation theSmoke = smokeMap.getValue();
                    theSmoke.update(System.currentTimeMillis());

                    if (smokeMap.getValue().getCurrentFrame() >= (smoke_frame_count - 1)) {
                        animcache.remove(smokeMap.getKey());
                    }
                }

                //Update enemy
                for (Map.Entry<String, Enemy> enemyMap : cache.entrySet()) {
                    enemyMap.getValue().update(dt);

                    Enemy theIT = enemyMap.getValue();

                    float xDiff = thePlayer.getM_PosX() - theIT.getM_PosX();
                    float yDiff = thePlayer.getM_PosY() - theIT.getM_PosY();

                    float theScale = thePlayer.getM_PlayerScale() * 0.55f;
                    if (theIT.getM_Active()) // If the enemy is active
                    {
                        if (CheckCollision(xDiff, yDiff, theScale)) { // If hit player
                            theIT.setM_Active(false);
                            startVibrate(); // Player hit, vibrate phone
                            sounds.play(explosion,1.0f,1.0f,0,0,1.5f);
                            if (thePlayer.getM_HealthPoints() > 1) {
                                thePlayer.setM_HealthPoints(thePlayer.getM_HealthPoints() - 1);
                                thePlayer.setPlayerIndex(thePlayer.getPlayerArraySize() - thePlayer.getM_HealthPoints());
                            } else {
                                //Trigger game over
                                //Test
                                Intent intent = new Intent();
                                //if(v==btn_pause)
                                //{
                                intent.setClass(getContext(), Mainmenu.class);
                                //}
                                getContext().startActivity(intent);
                            }
                        }
                    }
                }
                //Update bullets
                for (Map.Entry<String, Bullet> bulletMap : bulletcache.entrySet()) {
                    bulletMap.getValue().update(dt);

                    Bullet theBullet = bulletMap.getValue();
                    if (theBullet.getM_Active()) // If the bullet is active
                    {
                        for (Map.Entry<String, Enemy> enemyMap : cache.entrySet()) {
                            Enemy theIT = enemyMap.getValue();
                            if (theIT.getM_Active()) // If the enemy is active
                            {
                                float xDiff = theIT.getM_PosX() - theBullet.getM_PosX();
                                float yDiff = theIT.getM_PosY() - theBullet.getM_PosY();

                                float theScale = bullet.getHeight();

                                if (theBullet.getM_PosX() > ScreenWidth || theBullet.getM_PosX() < 0) // Handle out of screen XY-Axis
                                {
                                    theBullet.setM_Active(false);
                                } else if (theBullet.getM_PosY() > ScreenHeight || theBullet.getM_PosY() < 0) {
                                    theBullet.setM_Active(false);
                                }
                                if (CheckCollision(xDiff, yDiff, theScale)) {
                                    theIT.setM_HP(theIT.getM_HP() - theBullet.getM_Damage());
                                    theBullet.setM_Active(false); // Set the bullet to false;
                                    sounds.play(enemyhurt,1.0f,1.0f,0,0,1.5f);
                                    if (theIT.getM_HP() <= 0) {
                                        float offsetX = theIT.getM_PosX() - (smoke_anim.getSpriteWidth() * 0.5f);
                                        float offsetY = theIT.getM_PosY() - (smoke_anim.getSpriteHeight() * 0.5f);
                                        smoke_anim.setX((int) offsetX);
                                        smoke_anim.setY((int) offsetY);
                                        animcache.put(theIT.getM_Name(), smoke_anim);

                                        System.out.println(animcache.size());

                                        thePlayer.setM_Score(thePlayer.getM_Score() + (int) theIT.getM_ScoreWorth());
                                        thePlayer.setM_Gold(thePlayer.getM_Gold() + ((int) theIT.getM_ScoreWorth() * thePlayer.getM_Gold_Multiplyer_Level()));
                                        theKillCount++;

                                        theIT.setM_Active(false);
                                        sounds.play(explosion, 1.0f, 1.0f, 0, 0, 1.5f);


                                        //Level increase
                                        if (theKillCount >= 20) {
                                            theLevel++;
                                            theKillCount = 0;
                                        }
                                    }
                                }
                            }
                        }

                    }
                }
            }
            break;
        }
    }

    // Rendering is done on Canvas
    public void doDraw(Canvas canvas) {
        switch (GameState) {
            case 0:
                RenderGameplay(canvas);
                break;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int action = event.getAction();

        // 5) In event of touch on screen, the spaceship will relocate to the point of touch

        short X = (short) event.getX();
        short Y = (short) event.getY();

        lastX = X;
        lastY = Y;

        // Doing a drag event
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                HandleShopDownPress(X, Y);
                HandleBulletShoot(X, Y);
                break;
            case MotionEvent.ACTION_UP:
                thePlayer.setM_IsShoot(false);
                break;
            case MotionEvent.ACTION_MOVE:
                thePlayer.setM_IsShoot(true);
                break;

        }
        return true;
        //return super.onTouchEvent(event);
    }

    public void HandleShopDownPress(short X, short Y) {
        if (!btn_shop_opened)
            if (X > thePlayer.getM_PlayerXScale() && X < thePlayer.getM_PlayerXScale() + thePlayer.PlayerFace[thePlayer.getPlayerIndex()].getWidth()) // Check if within X + width
            {
                if (Y > thePlayer.getM_PlayerYScale() && Y < thePlayer.getM_PlayerYScale() + thePlayer.PlayerFace[thePlayer.getPlayerIndex()].getHeight()) // Check if within Y + height
                {
                    // Shop button is being pressed
                    System.out.println("Shop button pressed!");
                    btn_shop_opened = true;
                    sounds.play(shop,1.0f,1.0f,0,0,1.5f);
                }
            }

        if (btn_shop_opened) // Only allow interaction with back button when shop is open
            if (CheckTouchCollisionImage(X, Y, btn_back, btn_back_X, btn_back_Y)) {
                // Shop button is being pressed
                System.out.println("Back button pressed!");
                btn_shop_opened = false;
                sounds.play(shop,1.0f,1.0f,0,0,1.5f);
            }
    }

    public void HandleBulletShoot(short x, short y) {
        if (thePlayer.getM_Time_Last_Attacked() > thePlayer.getM_Time_Attack_Delay()) {
            System.out.println("IM SHOOTING");
            sounds.play(shooting,1.0f,1.0f,0,0,1.5f);
            /*theBulletCount++;
            theBullet = new Bullet();
            String bulletID = "Bullet_";
            bulletID += theBulletCount;
            theBullet.Init(bulletID, thePlayer.getM_Damage(), thePlayer.getM_PosX(), thePlayer.getM_PosY(), x, y,true);

            Iterator it = bulletcache.entrySet().iterator();
            bulletcache.put(bulletID, theBullet);
            //Reset the attack
            thePlayer.setM_Time_Last_Attacked(0);
            */
            for (Map.Entry<String, Bullet> bulletMap : bulletcache.entrySet()) {
                Bullet theBullet = bulletMap.getValue();
                if (theBullet.getM_Active() == false) {
                    theBullet.Init(theBullet.getM_Name(), thePlayer.getM_Damage(), thePlayer.getM_PosX(), thePlayer.getM_PosY(), x, y, true);
                    thePlayer.setM_Time_Last_Attacked(0);
                    break;
                }
            }
        }

    }

    public boolean CheckTouchCollisionImage(short inputX, short inputY, Bitmap image, float imageX, float imageY) {
        if (inputX > imageX && inputX < imageX + image.getWidth()) // Check if within X + width
        {
            if (inputY > imageY && inputY < imageY + image.getHeight()) // Check if within Y + height
            {
                // image is being pressed
                return true;
            }
        }
        return false;
    }

    public boolean CheckCollision(float xDiff, float yDiff, float theScale) {
        double distance = Math.sqrt(xDiff * xDiff + yDiff * yDiff);

        if (theScale > distance) {
            return true;
        } else {
            return false;
        }
    }

    public void startVibrate() {
        long pattern[] = {0, 200, 100};
        v = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(pattern, -1); // -1 for non-repeat
        System.out.println("Test v");
    }

    public void stopVibrate(){
        v.cancel();
    }
}
