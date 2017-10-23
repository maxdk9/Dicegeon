package com.tann.dice.gameplay.village.inventory;

import com.badlogic.gdx.graphics.Color;
import com.tann.dice.gameplay.effect.Eff;
import com.tann.dice.gameplay.village.Village;

public class MoraleRange {
    public int min, max;
    public Color col;
    public Eff[] effs;
    public MoraleRange(int min, int max, Color col, Eff[] effs) {
        this.min = min;
        this.max = max;
        this.col = col;
        this.effs = effs;
    }

    public MoraleRange(int min, int max, Color col, Eff eff) {
        this(min, max, col, new Eff[]{eff});
    }

    public boolean isActive() {
        int value = Village.getInventory().getResourceAmount(Eff.EffectType.Morale);
        return(value>min && value<=max);
    }

    public void activate() {
        for(Eff e:effs){
            Village.get().activate(e, e.type== Eff.EffectType.Buff);
        }
    }
}
