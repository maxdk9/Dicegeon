package com.tann.dice.gameplay.phase;

import com.tann.dice.Main;

public class TargetingPhase extends Phase {
    @Override
    public void activate() {

    }

    @Override
    public void deactivate() {
        Main.pushPhase(new DamagePhase());
    }
}
