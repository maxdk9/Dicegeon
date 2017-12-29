package com.tann.dice.gameplay.phase;


import com.badlogic.gdx.utils.Timer;
import com.tann.dice.Main;
import com.tann.dice.gameplay.entity.DiceEntity;
import com.tann.dice.gameplay.entity.Hero;
import com.tann.dice.gameplay.entity.die.Die;
import com.tann.dice.gameplay.entity.group.Party;
import com.tann.dice.screens.dungeon.DungeonScreen;

public class PlayerRollingPhase extends Phase {
    @Override
    public void activate() {
        Party.get().firstRoll();
    }

    @Override
    public void deactivate() {
        Main.pushPhase(new TargetingPhase());
    }

    @Override
    protected boolean doneCheck() {
        for(DiceEntity h:Party.get().getActiveEntities()){
            if(h.getDie().getState() != Die.DieState.Locked && h.getDie().getState() != Die.DieState.Locking){
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean canRoll() {
        return true;
    }
}
