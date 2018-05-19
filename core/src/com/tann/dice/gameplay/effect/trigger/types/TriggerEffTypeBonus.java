package com.tann.dice.gameplay.effect.trigger.types;

import com.tann.dice.gameplay.effect.Eff;
import com.tann.dice.gameplay.effect.Eff.EffType;
import com.tann.dice.gameplay.effect.trigger.Trigger;
import com.tann.dice.gameplay.entity.DiceEntity;
import com.tann.dice.gameplay.entity.die.Side;

public class TriggerEffTypeBonus extends Trigger {
    Eff.EffType type;
    int bonus;
    public TriggerEffTypeBonus(Eff.EffType type, int bonus) {
        this.type = type;
        this.bonus = bonus;
    }

    @Override
    public void affectSide(Side side, DiceEntity owner) {
        boolean found = false;
        for(Eff e:side.getEffects()){
            if(e.type == type){
                found = true;
            }
            if(e.type == EffType.Buff && !(e.getBuff().trigger instanceof TriggerAllSidesBonus)){
                return; // don't affect buffs hack for now
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
