package com.tann.dice.gameplay.phase;

import com.tann.dice.gameplay.effect.trigger.sources.Equipment;
import com.tann.dice.gameplay.entity.DiceEntity;
import com.tann.dice.gameplay.entity.group.Party;
import com.tann.dice.screens.dungeon.DungeonScreen;
import com.tann.dice.screens.dungeon.panels.LevelEndPanel;
import com.tann.dice.util.Tann;
import java.util.ArrayList;
import java.util.List;

public class LevelEndPhase extends Phase {
    LevelEndPanel levelEndPanel;
    @Override
    public void activate() {
        int level = DungeonScreen.get().level;
        List<Equipment> gainedEquipment = new ArrayList<>();
        if(level%2==1){
            gainedEquipment.add(Equipment.random(DungeonScreen.get().level/4));
        }
        for(Equipment e:gainedEquipment){
            Party.get().addEquipment(e);
        }
        levelEndPanel = new LevelEndPanel(gainedEquipment, level%2==0);
        DungeonScreen.get().addActor(levelEndPanel);
        Tann.center(levelEndPanel);
        for(DiceEntity de:Party.get().getActiveEntities()){
            de.slide(false);
        }
        DungeonScreen.get().spellButt.setVisible(false);
    }

    @Override
    public void deactivate() {
        DungeonScreen.get().spellButt.setVisible(true);
    }

    @Override
    public void refreshPhase() {
        levelEndPanel.action();
        levelEndPanel.layout();
    }


}
