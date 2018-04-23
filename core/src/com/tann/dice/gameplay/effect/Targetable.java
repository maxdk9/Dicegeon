package com.tann.dice.gameplay.effect;

import com.tann.dice.gameplay.effect.Eff.TargetingType;

public interface Targetable {
    Eff[] getEffects();
    boolean use();
    void deselect();
    void select();
    boolean isUsable();
    boolean repeat();
    void afterUse();
}
