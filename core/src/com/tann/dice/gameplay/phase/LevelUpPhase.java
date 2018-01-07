package com.tann.dice.gameplay.phase;

import com.tann.dice.gameplay.entity.DiceEntity;
import com.tann.dice.gameplay.entity.Hero;
import com.tann.dice.gameplay.entity.group.Party;
import com.tann.dice.screens.dungeon.DungeonScreen;

public class LevelUpPhase extends Phase {

    @Override
    public void activate() {
        Hero h = (Hero) Party.get().getRandomActive(false);
        h = (Hero) Party.get().getActiveEntities().get(3);
        DungeonScreen.get().showLevelupPanel(h, h.getLevelupOptions());
    }

    @Override
    public void deactivate() {
        DungeonScreen.get().friendly.setEntities(Party.get().getEntities());
    }
}
