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

        theMath.xRandom = -theOffset;
        theMath.xRandom += r.nextInt(theScreenW + (theOffset * 2));

        //Check if out of screen
        if ( theMath.xRandom < 0 || theMath.xRandom > theScreenW)
        {
            theMath.yRandom = r.nextInt (theScreenH);
        }
        else
        {
            theMath.yRandom = r.nextInt(theOffset);

            if ( r.nextBoolean() )
            {
                theMath.yRandom = 0 - theMath.yRandom;
            }
            else
            {
                theMath.yRandom = theScreenH + theMath.yRandom;
            }
        }

        return theMath;
    }

    public float Length(float x, float y)
    {
        return (float)Math.sqrt(x * x + y * y);
    }
}
