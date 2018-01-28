package com.tann.dice.screens.dungeon.panels;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.tann.dice.Images;
import com.tann.dice.util.Colours;
import com.tann.dice.util.Draw;
import com.tann.dice.util.Tann;

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
                toggleButt();
                return super.touchDown(event, x, y, pointer, button);
            }
        });
    }

    boolean shown;
    public void toggleButt(){
        float speed = .3f;
        Interpolation terp = Interpolation.pow2Out;
        if(shown){
            container.addAction(Actions.scaleTo(0,0, speed, terp));
            container.addAction(Actions.moveTo(getWidth()/2, getHeight()/2, speed, terp));
        }
        else{
            container.addAction(Actions.scaleTo(1,1, speed, terp));
            container.addAction(Actions.moveTo(getWidth()/2, -sh.getHeight()+getHeight()/2, speed, terp));
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
//        Draw.fillActor(batch, this);
        batch.setColor(Colours.z_white);
        batch.draw(Images.magicButt, getX(), getY());
    }

    List<Actor> spellHovers = new ArrayList<>();
    public void addSpellHover(Actor a){
        float radius = 13f;
        double startAngle = Math.PI*3/4f;
        double increment = Math.PI/5;
        double angle = startAngle + increment * ((spellHovers.size()+1)/2) * ((spellHovers.size()%2==0?1:-1));
        a.addAction(Actions.moveTo(
                (int)(getX()+getWidth()/2f+ Math.cos(angle)*radius - a.getWidth()/2f+.5f),
                (int)(getY()+getHeight()/2f+ Math.sin(angle)*radius - a.getHeight()/2f+.5f),
                .7f, Interpolation.pow2Out
        ));
        spellHovers.add(a);
    }

    public void removeHover(){
        spellHovers.remove(spellHovers.size()-1).remove();
    }
}

