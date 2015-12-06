package com.sidm.Senseless;

import android.graphics.Bitmap;

/**
 * Created by Edmund on 5/12/2015.
 */
public class PlayerClass {

    int m_HealthPoints = 3;
    int m_Damage;
    int m_Score;
    int m_Gold;
    int m_Type_1_Upgrade_Level;
    int m_Type_2_Upgrade_Level;
    float m_Time_Last_Attacked;
    float m_Time_Attack_Delay = 0.2f;
    float m_PosX = 0;
    float m_PosY = 0;
    float m_PlayerXScale = 0;
    float m_PlayerYScale = 0;
    float m_PlayerScale;
    private static final int PlayerArraySize = 3;
    private int PlayerIndex = PlayerArraySize - m_HealthPoints ;   //Player bitmap array count


    public Bitmap[] PlayerFace = new Bitmap[PlayerArraySize];    //Init player bitmap

    public float getM_Time_Attack_Delay() {
        return m_Time_Attack_Delay;
    }

    public void setM_Time_Attack_Delay(float m_Time_Attack_Delay) {
        this.m_Time_Attack_Delay = m_Time_Attack_Delay;
    }

    public int getM_HealthPoints() {
        return m_HealthPoints;
    }

    public void setM_HealthPoints(int m_HealthPoints) {
        this.m_HealthPoints = m_HealthPoints;
    }

    public int getM_Damage() {
        return m_Damage;
    }

    public void setM_Damage(int m_Damage) {
        this.m_Damage = m_Damage;
    }

    public int getM_Score() {
        return m_Score;
    }

    public void setM_Score(int m_Score) {
        this.m_Score = m_Score;
    }

    public int getM_Gold() {
        return m_Gold;
    }

    public void setM_Gold(int m_Gold) {
        this.m_Gold = m_Gold;
    }

    public int getM_Type_1_Upgrade_Level() {
        return m_Type_1_Upgrade_Level;
    }

    public void setM_Type_1_Upgrade_Level(int m_Type_1_Upgrade_Level) {
        this.m_Type_1_Upgrade_Level = m_Type_1_Upgrade_Level;
    }

    public int getM_Type_2_Upgrade_Level() {
        return m_Type_2_Upgrade_Level;
    }

    public void setM_Type_2_Upgrade_Level(int m_Type_2_Upgrade_Level) {
        this.m_Type_2_Upgrade_Level = m_Type_2_Upgrade_Level;
    }

    public float getM_Time_Last_Attacked() {
        return m_Time_Last_Attacked;
    }

    public void setM_Time_Last_Attacked(float m_Time_Last_Attacked) {
        this.m_Time_Last_Attacked = m_Time_Last_Attacked;
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

    public float getM_PlayerXScale() {
        return m_PlayerXScale;
    }

    public void setM_PlayerXScale(float m_PlayerXScale) {
        this.m_PlayerXScale = m_PlayerXScale;
    }

    public float getM_PlayerYScale() {
        return m_PlayerYScale;
    }

    public void setM_PlayerYScale(float m_PlayerYScale) {
        this.m_PlayerYScale = m_PlayerYScale;
    }

    public float getM_PlayerScale() {
        return m_PlayerScale;
    }

    public void setM_PlayerScale(float m_PlayerScale) {
        this.m_PlayerScale = m_PlayerScale;
    }

    public static int getPlayerArraySize() {
        return PlayerArraySize;
    }

    public int getPlayerIndex() {
        return PlayerIndex;
    }

    public void setPlayerIndex(int playerIndex) {
        PlayerIndex = playerIndex;
    }


}
