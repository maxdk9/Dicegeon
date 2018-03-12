package com.tann.dice.screens.dungeon.panels;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.tann.dice.gameplay.effect.trigger.sources.Equipment;
import com.tann.dice.gameplay.entity.DiceEntity;
import com.tann.dice.util.Tann;
import com.tann.dice.util.TextWriter;

import java.util.ArrayList;
import java.util.List;

public class LevelEndPanel extends Group {

    private static final String[] congrats = new String[]{
            "Nice!", "Congrats", "You did it!", "Congration", "Hot stuff", "Wowzers", "Spledid", "You believed in yourself!", "I knew you could do it", "They never stood a chance",
            "Imbressive"};

    List<Equipment> gainedEquipment;
    List<DiceEntity> toLevelup;

    public LevelEndPanel(List<Equipment> gainedEquipment, List<DiceEntity> toLevelup) {
        this.gainedEquipment = gainedEquipment;
        this.toLevelup = toLevelup;
        layout();
    }

    public void layout(){
        TextWriter title = new TextWriter("[sin]"+ Tann.getRandom(congrats)+"[sin]");
        TextWriter gained = null;
        List<EquipmentPanel> panels = new ArrayList<>();
        if(gainedEquipment.size()>0){
            gained = new TextWriter("You got: ");
            for(Equipment e:gainedEquipment){
                panels.add(new EquipmentPanel(e, false));
            }
        }
        List<TextWriter> levelled = new ArrayList<>();
        for(DiceEntity de: toLevelup){
            levelled.add(new TextWriter(de.getName()+" levelled up!"));
        }

    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
    }
}
