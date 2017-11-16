package com.tann.dice.gameplay.phase;

public abstract class Phase {

    public abstract void activate();

    public abstract void deactivate();

    public String toString(){
        return getClass().getSimpleName();
    }
}
