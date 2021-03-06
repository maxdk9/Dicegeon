package com.tann.dice.gameplay.phase;

import com.tann.dice.Main;
import com.tann.dice.gameplay.effect.trigger.sources.Equipment;
import com.tann.dice.gameplay.entity.DiceEntity;
import com.tann.dice.gameplay.entity.group.Party;
import com.tann.dice.screens.dungeon.DungeonScreen;
import com.tann.dice.screens.dungeon.LevelManager;
import com.tann.dice.screens.dungeon.panels.LevelEndPanel;
import com.tann.dice.util.Tann;
import com.tann.dice.util.Tann.TannPosition;
import java.util.ArrayList;
import java.util.List;

public class LevelEndPhase extends Phase {
    LevelEndPanel levelEndPanel;
    @Override
    public void activate() {
        Party.get().reset();
        int level = LevelManager.get().getLevel();
        List<Equipment> gainedEquipment = new ArrayList<>();
        if(level%2==0){
            gainedEquipment.add(Equipment.random(LevelManager.get().getLevel()/5));
        }
        for(Equipment e:gainedEquipment){
            Party.get().addEquipment(e);
        }
        levelEndPanel = new LevelEndPanel(gainedEquipment, level%2==1);
        levelEndPanel.setX(Main.width/2-levelEndPanel.getWidth()/2);
        Tann.slideIn(levelEndPanel, DungeonScreen.get(), TannPosition.Top, 30);
        for(DiceEntity de:Party.get().getActiveEntities()){
            de.slide(false);
        }
        DungeonScreen.get().spellButt.setVisible(false);
        DungeonScreen.get().slideTarget(false);
        DungeonScreen.get().slideConfirmButton(false);
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

    @Override
    public void cleanup() {
        levelEndPanel.slideOff();
    }
}
