package com.tann.dice.screens.gameScreen.panels.rollStuff;

import com.badlogic.gdx.graphics.g2d.Batch;

import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.utils.Align;
import com.tann.dice.Images;
import com.tann.dice.Main;
import com.tann.dice.util.*;

public class RerollPanel extends Lay{
	
	static final float TICK_SIZE=Main.h(10);

	public RerollPanel() {
	    setTouchable(Touchable.disabled);
	    layout();
	}

    @Override
    public void layout() {
        setSize(Main.h(20), Main.h(20));
    }

	static int rollsLeft,maximumRolls, diceRolling;
	static boolean locked;

	public void setRolls(int rollsLeft, int maximumRolls){
	    this.rollsLeft=rollsLeft; this.maximumRolls=maximumRolls;
    }

    public void setDiceStillRolling(int num){
	    diceRolling = num;
    }

    public void setAllDiceLocks(boolean lockedd){
        locked=lockedd;
    }

	@Override
	public void draw(Batch batch, float parentAlpha) {
	    float rollSize = Main.h(15);
        if(diceRolling>0){
            batch.setColor(Colours.grey);
            Draw.drawLoadingAnimation(batch, getX()+getWidth()/2, getY()+getHeight()/3*2+Main.h(3), Main.h(3), Main.h(2), 2, 3);
        }
        else if(rollsLeft==0 || locked){
            batch.setColor(Colours.green_light);
            Draw.drawSize(batch, Images.tick, getX()+getWidth()/2- TICK_SIZE/2, getY() + getHeight()/2 - TICK_SIZE/2, TICK_SIZE, TICK_SIZE);
        }
        else{
	        if(diceRolling==0) {
                batch.setColor(Colours.light);
                Draw.drawSize(batch, Images.roll, getX()+getWidth()/2-rollSize/2, getY()+Main.h(7), rollSize, rollSize);
            }
            if(rollsLeft > 0 ){
                Fonts.font.setColor(Colours.light);
            }
            else{
                Fonts.font.setColor(Colours.grey);
            }
            Fonts.font.draw(batch, rollsLeft+"/"+maximumRolls, getX()+getWidth()/4, getY()+Main.h(6), getWidth()/2, Align.center, false);
        }
    }
}
