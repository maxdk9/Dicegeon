package com.tann.dice.gameplay.phase;

import com.tann.dice.gameplay.entity.Hero;
import com.tann.dice.screens.dungeon.DungeonScreen;

public class LevelUpPhase extends Phase {

    @Override
    public void activate() {
        DungeonScreen.get().showLevelupPanel((Hero)DungeonScreen.get().getRandomHero());
    }

    @Override
    public void deactivate() {
    }
}
