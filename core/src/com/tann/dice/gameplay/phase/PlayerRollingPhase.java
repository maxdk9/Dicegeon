package com.tann.dice.gameplay.phase;


import com.tann.dice.gameplay.entity.group.Party;
import com.tann.dice.screens.dungeon.DungeonScreen;
import com.tann.dice.screens.dungeon.PhaseManager;

public class PlayerRollingPhase extends Phase {
    @Override
    public void activate() {
        Party.get().resetForRoll();
        Party.get().resetRolls();
        DungeonScreen.get().slideConfirmButton(true);
        DungeonScreen.get().slideRollButton(true);
        DungeonScreen.get().slideTarget(true);
        DungeonScreen.get().setConfirmText("Confirm Dice");
        Party.get().activateNextTurnEffects();
    }

    @Override
    public void deactivate() {
        PhaseManager.get().pushPhase(new TargetingPhase());
    }

    @Override
    public boolean canRoll() {
        return true;
    }

    @Override
    public String describe() {
        return "Reroll or keep your dice";
    }
}
