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
            e.setValue(e.getValue()+amount);
        }
    }

    @Override
    public void setValue(int value) {
        this.amount = value;
    }

    @Override
    public String describe() {
        return "+"+amount+" to all sides";
    }
}
