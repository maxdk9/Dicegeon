package com.tann.dice.screens.dungeon.panels.entityPanel;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.tann.dice.bullet.BulletStuff;
import com.tann.dice.gameplay.entity.DiceEntity;
import com.tann.dice.gameplay.entity.die.Side;
import com.tann.dice.util.Colours;
import com.tann.dice.util.Draw;

public class DieHolder extends Actor {
    static final int extraGap = 0;
    DiceEntity entity;
    public DieHolder(DiceEntity entity) {
        this.entity = entity;
        setSize(entity.getDie().get2DSize(), entity.getDie().get2DSize());
        setColor(entity.getColour());
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if(!BulletStuff.instances.contains(entity.getDie().physical)){
            Side side = entity.getDie().getActualSide();
            if(side!=null){
                side.draw(batch, getX(), getY());
            }
        }
        super.draw(batch, parentAlpha);
    }

}
