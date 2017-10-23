package com.tann.dice.gameplay.island.objective;

import com.tann.dice.gameplay.effect.Eff;
import com.tann.dice.gameplay.village.Village;

public class GemsObjective extends  Objective{

    public GemsObjective(int numGems) {
        this.required=numGems;
    }

    @Override
    public boolean isDeath() {
        return false;
    }

    @Override
    public void init() {
        current = Village.get().getInventory().getResourceAmount(Eff.EffectType.Gem);
    }

    @Override
    protected boolean internalObjectiveProgress(ObjectiveEffect type, int amount) {
        if(type==ObjectiveEffect.Gem){
            current+=amount;
            return true;
        }
        return false;
    }

    @Override
    public String getTitleString() {
        return "Collect "+required+" gems.";
    }

    @Override
    public String getProgressString() {
        return getDefaultProgressString();
    }
}
