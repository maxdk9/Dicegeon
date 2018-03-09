package com.tann.dice.gameplay.phase;

import com.tann.dice.gameplay.entity.DiceEntity;
import com.tann.dice.gameplay.entity.group.Room;
import com.tann.dice.screens.dungeon.DungeonScreen;
import com.tann.dice.screens.dungeon.PhaseManager;

public class EnemyRollingPhase extends Phase {
    @Override
    public void activate() {
        DungeonScreen.get().enemyCombat();
    }

    @Override
    public void deactivate() {
        PhaseManager.get().pushPhase(new PlayerRollingPhase());
    }

    @Override
    public long getSwitchingDelay() {
        return 1000;
    }

    @Override
    protected boolean doneCheck() {
        for(DiceEntity m: Room.get().getActiveEntities()){
            if(!m.locked) return false;
        }
        return true;
    }
}
