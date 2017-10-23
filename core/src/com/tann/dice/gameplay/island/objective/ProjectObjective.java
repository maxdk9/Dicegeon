package com.tann.dice.gameplay.island.objective;

import com.tann.dice.gameplay.village.Village;

public class ProjectObjective extends Objective{


    public ProjectObjective(int numBuildings) {
        this.required = numBuildings;
    }

    @Override
    public boolean isDeath() {
        return false;
    }

    @Override
    public void init() {
        this.current = Village.get().getNumBuildings();
    }

    @Override
    public boolean internalObjectiveProgress(ObjectiveEffect type, int amount) {
        if(type==ObjectiveEffect.Building){
            this.current += amount;
            return true;
        }
        return false;
    }

    @Override
    public String getTitleString() {
        return "Complete projects";
    }

    @Override
    public String getProgressString() {
        return getDefaultProgressString();
    }

}
