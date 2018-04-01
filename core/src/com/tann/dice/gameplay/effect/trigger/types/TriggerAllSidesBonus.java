package com.tann.dice.gameplay.effect.trigger.types;

import com.tann.dice.gameplay.effect.Eff;
import com.tann.dice.gameplay.effect.trigger.Trigger;
import com.tann.dice.gameplay.entity.die.Side;

public class TriggerAllSidesBonus extends Trigger{
    int amount;

    public TriggerAllSidesBonus(int amount) {
        this.amount = amount;
    }

    @Override
    public void affectSide(Side side) {
        for(Eff e:side.getEffects()){
            if(e.getValue()==0) continue;
            e.justValue(e.getValue()+amount);
        }
    }

    @Override
    public String describe() {
        return "+"+amount+" to all sides";
    }
}
