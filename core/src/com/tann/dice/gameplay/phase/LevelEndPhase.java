package com.tann.dice.gameplay.phase;

import com.tann.dice.Main;
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
        Party.get().reset();
        int level = DungeonScreen.get().level;
        List<Equipment> gainedEquipment = new ArrayList<>();
        if(level%2==1){
            gainedEquipment.add(Equipment.random(DungeonScreen.get().level/5));
        }
        for(Equipment e:gainedEquipment){
            Party.get().addEquipment(e);
        }
        levelEndPanel = new LevelEndPanel(gainedEquipment, level%2==0);
        DungeonScreen.get().addActor(levelEndPanel);
        Tann.center(levelEndPanel);
        levelEndPanel.setY((int)(Main.height*2/3f-levelEndPanel.getHeight()/2));
        for(DiceEntity de:Party.get().getActiveEntities()){
            de.slide(false);
        }
        DungeonScreen.get().spellButt.setVisible(false);
    }

    @Override
    public void deactivate() {
        DungeonScreen.get().spellButt.setVisible(true);
        Party.get().startOfFight();
    }

    @Override
    public void refreshPhase() {
        levelEndPanel.action();
        levelEndPanel.layout();
    }

    @Override
    public boolean showDiePanelReminder() {
        return false;
    }

    @Override
    public String describe() {
        return "You beat it!";
    }


}
