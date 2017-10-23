package com.tann.dice.gameplay.island.objective;

import com.tann.dice.gameplay.village.Village;
import com.tann.dice.screens.gameScreen.GameScreen;

public class TimeLimitObjective extends Objective {

    public TimeLimitObjective(int turns) {
            this.required=turns;
    }

    @Override
    public boolean isDeath() {
        return true;
    }

    @Override
    public void init() {
            this.current = Village.get().getDayNum();
    }

    @Override
    protected boolean internalObjectiveProgress(ObjectiveEffect type, int amount) {
        if(type==ObjectiveEffect.Turn){
            current+=amount;
            return true;
        }
        return false;
    }

    @Override
    public String getTitleString() {
        return "Complete all objectives in "+required+" turns.";
    }

    @Override
    public String getProgressString() {
        return getDefaultProgressString();
    }

    public void complete(){
        GameScreen.get().showLoss();
    }
}
