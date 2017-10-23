package com.tann.dice.gameplay.island.objective;


import com.tann.dice.gameplay.village.Village;

public class SurviveObjective extends Objective {
    public SurviveObjective(int numTurns) {
        this.required=numTurns;
    }

    @Override
    public boolean isDeath() {
        return false;
    }

    @Override
    public void init() {
        this.current = Village.get().getDayNum();
    }

    @Override
    public boolean internalObjectiveProgress(ObjectiveEffect type, int amount) {
        if(type == ObjectiveEffect.Turn){
            this.current += amount;
            return true;
        }
        return false;
    }

    @Override
    public String getTitleString() {
        return "Survive "+required+" turns";
    }

    @Override
    public String getProgressString() {
        return getDefaultProgressString();
    }
}
