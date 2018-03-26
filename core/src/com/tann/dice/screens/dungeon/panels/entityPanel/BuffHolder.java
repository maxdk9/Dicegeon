package com.tann.dice.screens.dungeon.panels.entityPanel;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.tann.dice.gameplay.effect.buff.Buff;
import com.tann.dice.gameplay.entity.DiceEntity;
import com.tann.dice.gameplay.entity.DiceEntity.EntitySize;
import com.tann.dice.util.Colours;
import com.tann.dice.util.Draw;

public class BuffHolder extends Actor {
    DiceEntity entity;

    private static final int GAP = 1;
    private static final int BUFFSIZE = 5;
    private static final int WIDTH = BUFFSIZE;
    int itemsPerColumn = 2;
    public BuffHolder(DiceEntity entity) {
        this.entity = entity;
        if(entity.getSize() == EntitySize.smol){
            itemsPerColumn = 1;
        }
        setSize(WIDTH, itemsPerColumn * BUFFSIZE + (itemsPerColumn-1)*GAP);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        batch.setColor(Colours.z_white);
//        Draw.fillActor(batch, this);
        for(int i=0;i<entity.getBuffs().size();i++){
            Buff b = entity.getBuffs().get(i);
            int yCo = i%itemsPerColumn;
            int xCo = i/itemsPerColumn;
            Draw.drawSize(batch, b.image, getX()+ xCo*(GAP + BUFFSIZE), getY() + getHeight() - BUFFSIZE *(yCo+1) - yCo*GAP, BUFFSIZE, BUFFSIZE);
        }
    }
}
