package com.tann.dice.gameplay.village.phase;

import com.tann.dice.gameplay.village.villager.Villager;

public abstract class Phase {
    public abstract void activate();
    public abstract void deactivate();
    public boolean allowDieClicking() {return false;}
    public boolean allowBuying() {return false;}
    public boolean canContinue() {return true;}
    public boolean selectVillager(Villager v){ return false; };
    public boolean putOnBottom(){return false;}

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
