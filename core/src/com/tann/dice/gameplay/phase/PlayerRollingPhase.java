package com.tann.dice.gameplay.phase;


import com.tann.dice.Main;
import com.tann.dice.gameplay.entity.DiceEntity;
import com.tann.dice.gameplay.entity.die.Die;
import com.tann.dice.gameplay.entity.group.Party;
import com.tann.dice.screens.dungeon.DungeonScreen;
import com.tann.dice.screens.dungeon.PhaseManager;

public class PlayerRollingPhase extends Phase {
    @Override
    public void activate() {
        Party.get().firstRoll();
        DungeonScreen.get().slideConfirmButton(true);
        DungeonScreen.get().slideRollButton(true);
        DungeonScreen.get().setConfirmText("Confirm Dice");
    }

    @Override
    public void deactivate() {
        PhaseManager.get().pushPhase(new TargetingPhase());
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
