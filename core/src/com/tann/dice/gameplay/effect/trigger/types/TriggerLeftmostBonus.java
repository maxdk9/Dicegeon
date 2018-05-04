package com.tann.dice.gameplay.effect.trigger.types;

import com.tann.dice.gameplay.effect.Eff;
import com.tann.dice.gameplay.effect.trigger.Trigger;
import com.tann.dice.gameplay.entity.DiceEntity;
import com.tann.dice.gameplay.entity.die.Side;

public class TriggerLeftmostBonus extends Trigger {

    int bonus;

    public TriggerLeftmostBonus(int value) {
        this.bonus = value;
    }

    @Override
    public void affectSide(Side side, DiceEntity owner) {
        if(side == owner.getSides()[2]){
            for(Eff e:side.getEffects()) {
                if(e.getValue()==0) continue;
                e.justValue(e.getValue() + bonus);
            }
        }
        super.affectSide(side, owner);
    }

    @Override
    public String describe() {
        return "+"+ bonus +" to your leftmost side";
    }
}
