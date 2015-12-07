package com.sidm.Senseless;

/**
 * Created by Aloysius Chun on 6/12/2015.
 */
public class Bullet {

    private boolean m_Active;
    private String m_Name;
    private float m_Damage;
    private float m_PosX;
    private float m_PosY;
    private float m_VelocityX;
    private float m_VelocityY;
    private float m_MovementSpeed = 1000.f;
    private float m_TargetPosX;
    private float m_TargetPosY;
    private float m_Rotation;
    private float BulletScaleX;
    private float BulletScaleY;

    public float getBulletScaleY() {
        return BulletScaleY;
    }

    public void setBulletScaleY(float bulletScaleY) {
        BulletScaleY = bulletScaleY;
    }

    public float getBulletScaleX() {
        return BulletScaleX;
    }

    public void setBulletScaleX(float bulletScaleX) {
        BulletScaleX = bulletScaleX;
    }

    Maths myMath = new Maths();

    public void setM_Active(boolean m_Active) {this.m_Active = m_Active;}

    public boolean getM_Active(){return m_Active;}

    public void setM_Damage(float m_Damage){this.m_Damage = m_Damage;}

    public float getM_Damage(){return m_Damage;}

    public float getM_Rotation() {
        return m_Rotation;
    }

    public void setM_Rotation(float m_Rotation) {
        this.m_Rotation = m_Rotation;
    }

    public float getM_PosY() {
        return m_PosY;
    }

    public void setM_PosY(float m_PosY) {
        this.m_PosY = m_PosY;
    }

    public float getM_PosX() {
        return m_PosX;
    }

    public void setM_PosX(float m_PosX) {
        this.m_PosX = m_PosX;
    }

    public void  Init(String name,float newDamage, float newPosX, float newPosY, float newTargetX, float newTargetY, boolean m_Active )
    {
        this.m_Name = name;
        this.m_Damage = newDamage;
        this.m_PosX = newPosX;
        this.m_PosY = newPosY;
        this.m_TargetPosX = newTargetX;
        this.m_TargetPosY = newTargetY;
        this.m_Active = m_Active;
        this.m_VelocityX = this.m_TargetPosX - this.m_PosX;
        this.m_VelocityY = this.m_TargetPosY - this.m_PosY;
    }
    public void  Init(float newDamage, float newPosX, float newPosY, float newTargetX, float newTargetY, boolean m_Active )
    {
        this.m_Damage = newDamage;
        this.m_PosX = newPosX;
        this.m_PosY = newPosY;
        this.m_TargetPosX = newTargetX;
        this.m_TargetPosY = newTargetY;
        this.m_Active = m_Active;
        this.m_VelocityX = this.m_TargetPosX - this.m_PosX;
        this.m_VelocityY = this.m_TargetPosY - this.m_PosY;
    }

    public void update(double dt)
    {
        /*this.m_VelocityX = this.m_TargetPosX - this.m_PosX;
        this.m_VelocityY = this.m_TargetPosY - this.m_PosY;*/

        float theLength = myMath.Length(this.m_VelocityX,this.m_VelocityY);

        //Normalise
        this.m_VelocityX /= theLength;
        this.m_VelocityY /= theLength;

        this.m_PosX += this.m_VelocityX * this.m_MovementSpeed * dt;
        this.m_PosY += this.m_VelocityY * this.m_MovementSpeed * dt;

        this.m_Rotation = (float)Math.toDegrees(Math.atan2(this.m_VelocityY,this.m_VelocityX));
    }
    public String getM_Name(){ return m_Name;}

}
