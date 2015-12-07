package com.sidm.Senseless;

/**
 * Created by Aloysius Chun on 5/12/2015.
 */
public class Enemy {

    Maths myMath = new Maths();
    String m_Name;
    private float m_MovementSpeed; //The movement speed
    private float m_HP; // The Hp
    private float m_ScoreWorth; //How much score its worth
    private float m_Damage; //How much damage it does
    private int m_ID; //The unique ID of this unit
    private float m_PosX;
    private float m_PosY;
    private float m_TargetPosX;
    private float m_TargetPosY;
    private float m_VelocityX;
    private float m_VelocityY;
    private float m_Rotation;
    private float EnemyScale;

    public float getEnemyScale() {
        return EnemyScale;
    }

    public void setEnemyScale(float enemyScale) {
        EnemyScale = enemyScale;
    }

    public float getM_Rotation() {
        return m_Rotation;
    }

    public void setM_Rotation(float m_Rotation) {
        this.m_Rotation = m_Rotation;
    }



    public float getM_TargetPosX() {
        return m_TargetPosX;
    }

    public void setM_TargetPosX(float m_TargetPosX) {
        this.m_TargetPosX = m_TargetPosX;
    }

    public float getM_TargetPosY() {
        return m_TargetPosY;
    }

    public void setM_TargetPosY(float m_TargetPosY) {
        this.m_TargetPosY = m_TargetPosY;
    }

    public float getM_VelocityX() {
        return m_VelocityX;
    }

    public void setM_VelocityX(float m_VelocityX) {
        this.m_VelocityX = m_VelocityX;
    }

    public float getM_VelocityY() {
        return m_VelocityY;
    }

    public void setM_VelocityY(float m_VelocityY) {
        this.m_VelocityY = m_VelocityY;
    }

    public void Init(String theName,float theMS, float theHP, float theScore, float theDamage, int theID, float newPosX, float newPosY, float theTargetX, float theTargetY)
    {
        this.m_Name = theName;
        this.m_MovementSpeed = theMS;
        this.m_HP = theHP;
        this.m_ScoreWorth = theScore;
        this.m_Damage = theDamage;
        this.m_ID = theID;
        this.m_PosX = newPosX;
        this.m_PosY = newPosY;
        this.m_TargetPosX = theTargetX;
        this.m_TargetPosY = theTargetY;
    }

    public float getM_PosX() {
        return m_PosX;
    }

    public void setM_PosX(float m_PosX) {
        this.m_PosX = m_PosX;
    }

    public float getM_PosY() {
        return m_PosY;
    }

    public void setM_PosY(float m_PosY) {
        this.m_PosY = m_PosY;
    }

    public int getM_ID() {
        return m_ID;
    }

    public void setM_ID(int m_ID) {
        this.m_ID = m_ID;
    }

    public String getM_Name() {
        return m_Name;
    }

    public void setM_Name(String m_Name) {
        this.m_Name = m_Name;
    }

    public float getM_MovementSpeed() {
        return m_MovementSpeed;
    }

    public void setM_MovementSpeed(float m_MovementSpeed) {
        this.m_MovementSpeed = m_MovementSpeed;
    }

    public float getM_HP() {
        return m_HP;
    }

    public void setM_HP(float m_HP) {
        this.m_HP = m_HP;
    }

    public float getM_ScoreWorth() {
        return m_ScoreWorth;
    }

    public void setM_ScoreWorth(float m_ScoreWorth) {
        this.m_ScoreWorth = m_ScoreWorth;
    }

    public float getM_Damage() {
        return m_Damage;
    }

    public void setM_Damage(float m_Damage) {
        this.m_Damage = m_Damage;
    }

    public void update(double dt)
    {
        this.m_VelocityX = this.m_TargetPosX - this.m_PosX;
        this.m_VelocityY = this.m_TargetPosY - this.m_PosY;

        float theLength = myMath.Length(this.m_VelocityX,this.m_VelocityY);

        //Normalise
        this.m_VelocityX /= theLength;
        this.m_VelocityY /= theLength;

        this.m_PosX += this.m_VelocityX * this.m_MovementSpeed * dt;
        this.m_PosY += this.m_VelocityY * this.m_MovementSpeed * dt;

        this.m_Rotation = (float)Math.toDegrees(Math.atan2(this.m_VelocityY,this.m_VelocityX));
    }
}
