package com.tann.dice.gameplay.phase;

import com.tann.dice.Main;
import com.tann.dice.screens.dungeon.DungeonScreen;

public class TargetingPhase extends Phase {
    @Override
    public void activate() {
        DungeonScreen.get().activateAutoEffects();
    }

    @Override
    public void deactivate() {
        DungeonScreen.get().resetMagic();
        DungeonScreen.get().removeLeftoverDice();
        DungeonScreen.get().spellHolder.hide();
        Main.pushPhase(new DamagePhase());
    }

    @Override
    public boolean canTarget() { return true; }
}
