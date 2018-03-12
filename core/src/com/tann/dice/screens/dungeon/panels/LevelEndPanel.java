package com.tann.dice.screens.dungeon.panels;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.tann.dice.gameplay.effect.trigger.sources.Equipment;
import com.tann.dice.gameplay.entity.DiceEntity;

import java.util.List;

public class LevelEndPanel extends Group {
    List<Equipment> gainedEquipment;
    List<DiceEntity> toLevelup;

    public LevelEndPanel(List<Equipment> gainedEquipment, List<DiceEntity> toLevelup) {
        this.gainedEquipment = gainedEquipment;
        this.toLevelup = toLevelup;
        layout();
    }

    public void layout(){
        
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
    }
}
