package com.tann.dice.gameplay.island.objective;

import com.tann.dice.screens.gameScreen.GameScreen;

public abstract class Objective {


    public String getVictoryText(){
        return "You win!";
    };

    public abstract boolean isDeath();

    public String toWriterString() {
        return "objective";
    }

    public enum ObjectiveEffect{Building, Turn, Gem};
    int current, required;

    protected String getDefaultProgressString() {
        return current+"/"+required;
    }

    public abstract void init();
    public void objectiveProgress(ObjectiveEffect type, int amount){
        if(internalObjectiveProgress(type, amount)){
            GameScreen.get().objectivePanel.refresh();
        }
    }

    protected abstract boolean internalObjectiveProgress(ObjectiveEffect type, int amount);
    public abstract String getTitleString();
    public abstract String getProgressString();
    public boolean isComplete(){
        return current >= required;
    }
}
