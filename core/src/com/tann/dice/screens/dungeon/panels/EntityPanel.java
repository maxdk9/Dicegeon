package com.tann.dice.screens.dungeon.panels;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.tann.dice.Images;
import com.tann.dice.gameplay.village.villager.DiceEntity;
import com.tann.dice.util.*;

public class EntityPanel extends Group {

    DiceEntity e;

    public EntityPanel(DiceEntity e) {
        float gapFactor = .9f;
        setSize(BottomPanel.width / 3 * gapFactor, BottomPanel.height / 2 * gapFactor);
        float absHeartGap = 2;
        float heartSize = 18;
        Layoo l = new Layoo(this);
        TextWriter tw = new TextWriter(e.getName(), Fonts.fontSmall);
        l.row(1);
        l.actor(tw);
        l.row(1);
        l.gap(1);
        for(int i=0;i<e.getMaxHp();i++){
            ImageActor ia = new ImageActor(Images.heart, heartSize, heartSize);
            l.actor(ia);
            if(i<e.getMaxHp()-1){
                l.abs(absHeartGap);
            }
        }
        l.gap(1);
        l.row(1);

        l.gap(1);
        l.layoo();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.setColor(Colours.brown_dark);
        Draw.fillActor(batch, this, Colours.dark, Colours.light,  2);
        super.draw(batch, parentAlpha);
    }
}
