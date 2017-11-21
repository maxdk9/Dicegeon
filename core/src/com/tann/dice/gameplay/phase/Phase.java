package com.tann.dice.gameplay.phase;

import com.tann.dice.Main;

public abstract class Phase {

    public abstract void activate();

    public abstract void deactivate();

    public String toString(){
        return getClass().getSimpleName();
    }

    public final void checkIfDone() {
        if(doneCheck()){
            Main.popPhase();
        }
    }

    protected boolean doneCheck(){
        return false;
    }

    public boolean canRoll() { return false; }

    public boolean canTarget() { return false; }
}
