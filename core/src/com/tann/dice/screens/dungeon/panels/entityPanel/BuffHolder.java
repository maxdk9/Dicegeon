package com.tann.dice.screens.dungeon.panels.entityPanel;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.tann.dice.gameplay.effect.buff.Buff;
import com.tann.dice.gameplay.entity.DiceEntity;
import com.tann.dice.util.Colours;
import com.tann.dice.util.Draw;

public class BuffHolder extends Actor {
    DiceEntity entity;

    private static final int GAP = 1;
    private static final int BUFFSIZE = 5;
    private static final int WIDTH = BUFFSIZE+GAP*2;
    public BuffHolder(DiceEntity entity, int height) {
        this.entity = entity;
        setSize(WIDTH, height);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        batch.setColor(Colours.z_white);
        for(int i=0;i<entity.getBuffs().size();i++){
            Buff b = entity.getBuffs().get(i);
            Draw.drawSize(batch, b.image, getX()+GAP, getY() + getHeight() - BUFFSIZE *(i+1) - i*GAP, BUFFSIZE, BUFFSIZE);
        }
    }
}
