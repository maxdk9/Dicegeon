package com.tann.dice.screens.dungeon.panels;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Align;
import com.tann.dice.Images;
import com.tann.dice.Main;
import com.tann.dice.gameplay.entity.group.Party;
import com.tann.dice.screens.dungeon.DungeonScreen;
import com.tann.dice.screens.dungeon.PhaseManager;
import com.tann.dice.util.*;

import java.util.ArrayList;
import java.util.List;

public class SpellButt extends Group {
    Group container = new Group();
    SpellHolder sh;
    public SpellButt() {
        float size = Images.magicButt.getRegionHeight();
        setSize(size,size);
        container.setScale(0);
        container.setPosition(getWidth()/2, getHeight()/2);
        addActor(container);
        addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if(Tann.dist(x,y,getWidth()/2, getHeight()/2)>getWidth()/2){
                    return false;
                }
                Main.getCurrentScreen().popAllLight();
                toggleButt();
                event.setBubbles(false);
                return super.touchDown(event, x, y, pointer, button);
            }
        });
    }

    public boolean shown;
    public void toggleButt(){
        float speed = .3f;
        Interpolation terp = Chrono.i;
        if(shown){
            container.addAction(Actions.scaleTo(0,0, speed, terp));
            container.addAction(Actions.moveTo(getWidth()/2, getHeight()/2, speed, terp));
        }
        else{
            container.addAction(Actions.scaleTo(1,1, speed, terp));
            container.addAction(Actions.moveTo((int)(getWidth()/2), (int)(-sh.getHeight()+getHeight()/2), speed, terp));
        }
        shown=!shown;
    }

    public void setSpellHolder(SpellHolder sh){
        this.sh = sh;
        container.addActor(sh);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        batch.setColor(Colours.z_white);
        batch.draw(Images.magicButt, getX(), getY());
        if(PhaseManager.get().getPhase().canTarget()){
            TannFont.font.drawString(batch, Party.get().getAvaliableMagic()+"", (int)(getX()+getWidth()/2), (int) (getY()+getHeight()/2), Align.center);
        }
    }

    public void show() {
        if(!shown){
            toggleButt();
        }
    }

    public void hide(){
        if(shown){
            toggleButt();
        }
    }
}

