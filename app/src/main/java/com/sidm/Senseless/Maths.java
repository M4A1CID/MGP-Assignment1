package com.sidm.Senseless;

import java.util.Random;

/**
 * Created by Aloysius Chun on 5/12/2015.
 */
public class Maths {
    Random r = new Random();
    //Random spawn
    float xRandom = 0;
    float yRandom = 0;

    public Maths getRandom(int theOffset, int theScreenW, int theScreenH)
    {
        Maths theMath = new Maths();
        float ScreenClip = 10.f;
//
//        switch ( r.nextInt(4) )
//        {
//            case 0:
//                //Left Spawn x < -10
//
//                // - 10
//                theMath.xRandom = -ScreenClip;
//                // 0 ~ ScreenH
//                theMath.yRandom = r.nextInt(theScreenH);
//                break;
//            case 1:
//                //Top Spawn y < -10
//
//                // 0 ~ ScreenW
//                theMath.xRandom = r.nextInt(theScreenW);
//                // -10
//                theMath.yRandom = -ScreenClip;
//                break;
//            case 2:
//                //Bottom Spawn y > ScreenH + 10
//
//                // 0 ~ ScreenW
//                theMath.xRandom = r.nextInt(theScreenW);
//                // ScreenH + 10
//                theMath.yRandom = theScreenH + ScreenClip;
//                break;
//            case 3:
//                //Right Spawn x > ScreenW + 10
//
//                // ScreenW + 10
//                theMath.xRandom = theScreenW  + ScreenClip;
//                // 0 ~ ScreenH
//                theMath.yRandom = r.nextInt(theScreenH);
//                break;
//        }

        theMath.xRandom = -theOffset;
        theMath.xRandom += r.nextInt(theScreenW + (theOffset * 2));

        //Check if out of screen
        if ( theMath.xRandom < -ScreenClip || theMath.xRandom > theScreenW + ScreenClip)
        {
            theMath.yRandom -= r.nextInt (theScreenH);
        }
        else
        {
            theMath.yRandom = r.nextInt(theOffset);

            if ( r.nextBoolean() )
            {
                theMath.yRandom = -ScreenClip - theMath.yRandom;
            }
            else
            {
                theMath.yRandom = theScreenH + theMath.yRandom + ScreenClip;
            }
        }

        //Attempt to prevent spawn in middle
        if ( theMath.xRandom > 0 && theMath.xRandom < theScreenW)
        {
            theMath.xRandom = -10;
        }
        else if ( theMath.yRandom > 0 && theMath.yRandom < theScreenH)
        {
            theMath.yRandom = -10;
        }

        return theMath;
    }

    public float Length(float x, float y)
    {
        return (float)Math.sqrt(x * x + y * y);
    }
}
