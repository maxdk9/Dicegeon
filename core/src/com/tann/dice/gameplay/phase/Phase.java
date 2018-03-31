package com.tann.dice.gameplay.phase;

import com.tann.dice.screens.dungeon.PhaseManager;

public abstract class Phase {

    public abstract void activate();

    public abstract void deactivate();

    public String toString(){
        return getClass().getSimpleName();
    }

    long switchStart=-1;
    public final void checkIfDone() {
        if(doneCheck()){
            if(switchStart == -1){
                switchStart = System.currentTimeMillis();
            }
            if(System.currentTimeMillis() - switchStart >= getSwitchingDelay()) {
                switchStart = -1;
                PhaseManager.get().popPhase();
            }
        }
    }

    public long getSwitchingDelay(){return 0;}

    protected boolean doneCheck(){
        return false;
    }

    public boolean canRoll() { return false; }

    public boolean canTarget() { return false; }

    public void refreshPhase() {}

    public abstract String describe();
}
