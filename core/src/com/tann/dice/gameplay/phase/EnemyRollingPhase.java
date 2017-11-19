package com.tann.dice.gameplay.phase;

import com.tann.dice.Main;
import com.tann.dice.gameplay.entity.Monster;
import com.tann.dice.screens.dungeon.DungeonScreen;

public class EnemyRollingPhase extends Phase {
    @Override
    public void activate() {
        DungeonScreen.get().enemyCombat();
    }

    @Override
    public void deactivate() {
        Main.pushPhase(new PlayerRollingPhase());
    }

    @Override
    protected boolean doneCheck() {
        for(Monster m: DungeonScreen.get().monsters){
            if(!m.locked) return false;
        }
        return true;
    }
}
