package com.tann.dice.gameplay.effect;

public interface Targetable {
    Eff[] getEffects();
    boolean use();
    void deselect();
    void select();
    boolean isUsable();
}
