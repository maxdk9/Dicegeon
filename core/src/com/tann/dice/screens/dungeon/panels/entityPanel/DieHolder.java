package com.tann.dice.screens.dungeon.panels.entityPanel;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
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
        if(entity.getDie().flatDraw){
            Side side = entity.getDie().getActualSide();
            if(side!=null){
                side.draw(batch, getX(), getY(), 1, entity.getColour(), entity.get2DLapel(), entity.getDie().getUsed());
            }
            if(entity.getDie().getUsed()){
                batch.setColor(Colours.withAlpha(Colours.dark, .7f));
                Draw.fillActor(batch, this);
            }
        }
        else{
            batch.setColor(Colours.z_white);
            batch.draw(entity.get2DLapel(), getX(), getY());
        }
        super.draw(batch, parentAlpha);
    }

}
