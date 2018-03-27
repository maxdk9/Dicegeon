package com.tann.dice.gameplay.phase;

import com.tann.dice.gameplay.effect.trigger.sources.Equipment;
import com.tann.dice.gameplay.entity.Hero;
import com.tann.dice.gameplay.entity.group.Party;
import com.tann.dice.screens.dungeon.DungeonScreen;
import com.tann.dice.screens.dungeon.panels.LevelEndPanel;
import com.tann.dice.util.InputBlocker;
import com.tann.dice.util.Tann;
import java.util.ArrayList;
import java.util.List;

public class LevelEndPhase extends Phase {
    LevelEndPanel levelEndPanel;
    @Override
    public void activate() {
        List<Equipment> gainedEquipment = new ArrayList<>();
        List<Hero> heroesToLevelUp = new ArrayList<>();
        if(DungeonScreen.get().level%2==0) {
            for (int i = 0; i < 1000; i++) {
                Hero h = (Hero) Tann.getRandom(Party.get().getActiveEntities());
                if (h.getHeroType().getLevelupOptions().size() > 0){
                    heroesToLevelUp.add(h);
                    break;
                }
            }
        }
        else{
            gainedEquipment.add(Equipment.random(DungeonScreen.get().level/4));
        }
        for(Equipment e:gainedEquipment){
            Party.get().addEquipment(e);
        }
        levelEndPanel = new LevelEndPanel(gainedEquipment, heroesToLevelUp);
        DungeonScreen.get().push(levelEndPanel, true, true, false, false, 0, null);
    }

    @Override
    public void deactivate() {

    }

    @Override
    public void refreshPhase() {
        levelEndPanel.layout();
    }
}
