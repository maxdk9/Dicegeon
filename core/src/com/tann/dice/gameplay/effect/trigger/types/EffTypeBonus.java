package com.tann.dice.gameplay.effect.trigger.types;

import com.tann.dice.gameplay.effect.Eff;
import com.tann.dice.gameplay.effect.trigger.Trigger;
import com.tann.dice.gameplay.entity.die.Side;

public class EffTypeBonus extends Trigger {
    Eff.EffType type;
    int bonus;
    public EffTypeBonus(Eff.EffType type, int bonus) {
        this.type = type;
        this.bonus = bonus;
    }

    @Override
    public void affectSide(Side side) {
        boolean found = false;
        for(Eff e:side.getEffects()){
            if(e.type == type){
                found = true;
            }
        }
        if(found) {
            for (Eff e : side.getEffects()) {
                e.justValue(e.getValue() + bonus);
            }
        }
    }

    @Override
    public String describe() {
        return "+"+bonus+" to all "+type+" sides";
    }
}
