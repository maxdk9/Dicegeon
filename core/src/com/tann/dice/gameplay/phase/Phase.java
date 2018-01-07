package com.tann.dice.gameplay.phase;

import com.tann.dice.Main;

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
                Main.popPhase();
            }
        }
    }

    public long getSwitchingDelay(){return 0;}

    protected boolean doneCheck(){
        return false;
    }

    public boolean canRoll() { return false; }

    public boolean canTarget() { return false; }

}
