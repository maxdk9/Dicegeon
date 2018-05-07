package com.tann.dice.screens.dungeon.panels.entityPanel;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.tann.dice.gameplay.effect.Buff;
import com.tann.dice.gameplay.effect.trigger.Trigger;
import com.tann.dice.gameplay.entity.DiceEntity;
import com.tann.dice.gameplay.entity.DiceEntity.EntitySize;
import com.tann.dice.util.Colours;
import com.tann.dice.util.Draw;
import com.tann.dice.util.Prefs;

import java.util.List;

public class TriggerPanel extends Actor {
    DiceEntity entity;

    private static final int GAP = 1;
    private static final int BUFFSIZE = 5;
    private static final int WIDTH = BUFFSIZE;
    int itemsPerColumn = 2;
    public TriggerPanel(DiceEntity entity) {
        this.entity = entity;
        if(entity.getSize() == EntitySize.smol || entity.getSize() == EntitySize.big){
            itemsPerColumn = 1;
        }
        setSize(WIDTH, itemsPerColumn * BUFFSIZE + (itemsPerColumn-1)*GAP);
        for(Trigger t:entity.getActiveTriggers()){
            if(t.highlightForNewPlayers()) {
                Prefs.setBoolean(t.getClass().getSimpleName(), true);
            }
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        batch.setColor(Colours.z_white);
        List<Trigger> triggerList = entity.getActiveTriggers();
        int drawIndex = 0;
        for(int i=0;i<triggerList.size();i++){
            Trigger t = triggerList.get(i);
            if(!t.showInPanel()) continue;
            int yCo = drawIndex%itemsPerColumn;
            int xCo = drawIndex/itemsPerColumn;
            Draw.drawSize(batch, t.getImage(), getX()+ xCo*(GAP + BUFFSIZE), getY() + getHeight() - (BUFFSIZE+GAP) *(yCo+1) + GAP, BUFFSIZE, BUFFSIZE);
            drawIndex++;
        }
    }
}
