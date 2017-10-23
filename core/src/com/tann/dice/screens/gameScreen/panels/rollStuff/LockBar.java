package com.tann.dice.screens.gameScreen.panels.rollStuff;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.tann.dice.Images;
import com.tann.dice.Main;
import com.tann.dice.bullet.BulletStuff;
import com.tann.dice.gameplay.village.Village;
import com.tann.dice.gameplay.village.villager.die.Die;
import com.tann.dice.util.Colours;
import com.tann.dice.util.Draw;
import com.tann.dice.util.Lay;

public class LockBar extends Lay{

    private static LockBar self;
    public static LockBar get(){
        if(self==null) self = new LockBar();
        return self;
    }

    private LockBar() {
        layout();
        addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                addAllDice();
                return super.touchDown(event, x, y, pointer, button);
            }
        });
    }

    private void addAllDice() {
        BulletStuff.moveAllToTop();
    }

    Die[] dice = new Die[Village.STARTING_VILLAGERS];

    public void reset(){
        for(int i=0;i<dice.length;i++){
            dice[i]=null;
        }
    }

    public int addDie(Die d){
        for(int i=0;i<dice.length;i++){
            if(dice[i]==null){
                dice[i]=d;
                return i;
            }
        }
        return -1;
    }

    public void removeDie(Die d) {
        for (int i = 0; i < dice.length; i++) {
            if (dice[i] == d) {
                dice[i] = null;
                break;
            }
        }
    }

    @Override
    public void layout() {
        setSize(Main.h(66), Main.h(11.0f));
    }

    public void moveIn() {
        addAction(Actions.moveTo(getX(), Main.height-getHeight(), .5f, Interpolation.pow2Out));
    }

    public void moveAway(){
        addAction(Actions.moveTo(getX(), Main.height, .5f, Interpolation.pow2Out));
    }

    public void render(Batch batch) {
        batch.setColor(Colours.brown_dark);
        Draw.fillRectangle(batch, getX()+getHeight(), getY(), getWidth()-getHeight()*2,getHeight());
        Draw.fillEllipse(batch, getX()+getHeight(), getY()+getHeight(), getHeight()*2, getHeight()*2);
        Draw.fillEllipse(batch, getX()+getWidth()-getHeight(), getY()+getHeight(), getHeight()*2, getHeight()*2);
        batch.setColor(Colours.brown_light);
        int numLocks = 5;
        for(int x=0;x<numLocks;x++){
            float gap = (getWidth()-getHeight()*2)/numLocks;
            float size = getHeight()*.4f;
            Draw.drawSizeCentered(batch, Images.lock, getX()+getHeight()+gap*x+gap/2, getY()+getHeight()/2, size, size);
        }
    }
}
