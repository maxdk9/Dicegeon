package com.tann.dice.gameplay.phase;

import com.tann.dice.Main;
import com.tann.dice.screens.dungeon.DungeonScreen;

public class EnemyRollingPhase extends Phase {
    @Override
    public void activate() {
        DungeonScreen.get().enemyCombat();
        System.out.println("activating");
        System.out.println(DungeonScreen.get().hashCode());
        System.out.println(DungeonScreen.get().hashCode());
    }

    @Override
    public void deactivate() {
        Main.pushPhase(new PlayerRollingPhase());
    }
}
