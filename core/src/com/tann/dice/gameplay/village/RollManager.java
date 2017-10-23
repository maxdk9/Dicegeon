package com.tann.dice.gameplay.village;

import com.tann.dice.bullet.BulletStuff;
import com.tann.dice.screens.gameScreen.panels.rollStuff.RerollPanel;

public class RollManager {
   private static int maxRolls, rolls;
    public static void setMaximumRolls(int max){
        RollManager.maxRolls=max;
        updateRolls();
    }

    public static void refreshRolls(){
        RollManager.rolls=maxRolls;
        updateRolls();
    }

    public static void spendRoll(){
        RollManager.rolls--;
        updateRolls();
    }

    public static boolean hasRoll(){
        return RollManager.rolls>0;
    }

    public static void updateRolls(){
        getRollPanel().setRolls(RollManager.rolls, RollManager.maxRolls);
        getRollPanel().setDiceStillRolling(BulletStuff.numRollingDice());
        getRollPanel().setAllDiceLocks(BulletStuff.allDiceLocked());
    }

    private static RerollPanel panel;
    public static RerollPanel getRollPanel(){
        if(panel==null){
            panel = new RerollPanel();
        }
        return panel;
    }
}
