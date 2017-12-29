package com.tann.dice.gameplay.phase;

import com.tann.dice.gameplay.entity.Hero;
import com.tann.dice.gameplay.entity.group.Party;
import com.tann.dice.screens.dungeon.DungeonScreen;

public class LevelUpPhase extends Phase {

    @Override
    public void activate() {
        DungeonScreen.get().showLevelupPanel((Hero) Party.get().getRandomActive());
    }

    @Override
    public void deactivate() {
        DungeonScreen.get().friendly.setEntities(Party.get().getEntities());
    }
}
