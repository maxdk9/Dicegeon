package com.tann.dice.gameplay.phase;

import com.tann.dice.Main;
import com.tann.dice.gameplay.entity.DiceEntity;
import com.tann.dice.gameplay.entity.Monster;
import com.tann.dice.gameplay.entity.group.Room;
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
    public long getSwitchingDelay() {
        return 400;
    }

    @Override
    protected boolean doneCheck() {
        for(DiceEntity m: Room.get().getActiveEntities()){
            if(!m.locked) return false;
        }
        return true;
    }
}
