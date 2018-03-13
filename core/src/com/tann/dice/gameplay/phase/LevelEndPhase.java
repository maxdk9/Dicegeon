package com.tann.dice.gameplay.phase;

import com.tann.dice.gameplay.effect.trigger.sources.Equipment;
import com.tann.dice.gameplay.entity.DiceEntity;
import com.tann.dice.gameplay.entity.group.Party;
import com.tann.dice.screens.dungeon.DungeonScreen;
import com.tann.dice.screens.dungeon.panels.LevelEndPanel;
import java.util.ArrayList;
import java.util.List;

public class LevelEndPhase extends Phase {
    @Override
    public void activate() {
        List<Equipment> gainedEquipment = new ArrayList<>();
        List<DiceEntity> heroesToLevelUp = new ArrayList<>();
        heroesToLevelUp.add(Party.get().getActiveEntities().get(0));
        heroesToLevelUp.add(Party.get().getActiveEntities().get(1));
        heroesToLevelUp.add(Party.get().getActiveEntities().get(2));
        heroesToLevelUp.add(Party.get().getActiveEntities().get(3));
        DungeonScreen.get().push(new LevelEndPanel(gainedEquipment, heroesToLevelUp), true, false, false, null);
    }

    @Override
    public void deactivate() {

    }
}
