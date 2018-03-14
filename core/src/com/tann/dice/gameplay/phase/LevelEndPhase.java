package com.tann.dice.gameplay.phase;

import com.tann.dice.gameplay.effect.trigger.sources.Equipment;
import com.tann.dice.gameplay.entity.Hero;
import com.tann.dice.gameplay.entity.group.Party;
import com.tann.dice.screens.dungeon.DungeonScreen;
import com.tann.dice.screens.dungeon.panels.LevelEndPanel;
import com.tann.dice.util.Tann;
import java.util.ArrayList;
import java.util.List;

public class LevelEndPhase extends Phase {
    @Override
    public void activate() {
        List<Equipment> gainedEquipment = new ArrayList<>();
        List<Hero> heroesToLevelUp = new ArrayList<>();
        if(DungeonScreen.get().level%2==0) {
            Hero h = null;
            for (int i = 0; i < 1000; i++) {
                h = (Hero) Tann.getRandom(Party.get().getActiveEntities());
                if (h.getHeroType().getLevelupOptions().size() > 0)
                    break;
            }
        }
        else{
            gainedEquipment.add(Equipment.random());
        }
        DungeonScreen.get().push(new LevelEndPanel(gainedEquipment, heroesToLevelUp), true, false, false, null);
    }

    @Override
    public void deactivate() {

    }
}
