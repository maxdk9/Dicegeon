package com.tann.dice.gameplay.phase;

import com.tann.dice.Main;
import com.tann.dice.gameplay.entity.group.Party;
import com.tann.dice.screens.dungeon.DungeonScreen;
import com.tann.dice.screens.dungeon.PhaseManager;

public class TargetingPhase extends Phase {
    @Override
    public void activate() {
        DungeonScreen.get().slideRollButton(false);
        DungeonScreen.get().slideConfirmButton(true);
        DungeonScreen.get().setConfirmText("End Turn");
        Party.get().activateNextTurnEffects();
    }

    @Override
    public void deactivate() {
        Party.get().resetMagic();
        DungeonScreen.get().removeLeftoverDice();
        DungeonScreen.get().spellButt.hide();
        PhaseManager.get().pushPhase(new DamagePhase());
    }

    @Override
    public boolean canTarget() { return true; }

    @Override
    public String describe() {
        return "Use your dice to attack and defend";
    }
}
