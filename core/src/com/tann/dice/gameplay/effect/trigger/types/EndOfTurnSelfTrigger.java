package com.tann.dice.gameplay.effect.trigger.types;

import com.tann.dice.gameplay.effect.Eff;
import com.tann.dice.gameplay.effect.trigger.Trigger;
import com.tann.dice.gameplay.entity.DiceEntity;

public class EndOfTurnSelfTrigger extends Trigger {
    Eff eff;
    public EndOfTurnSelfTrigger(Eff eff) {
        this.eff = eff;
    }

    @Override
    public void endOfTurn(DiceEntity target) {
        target.hit(eff, false);
    }

    @Override
    public Integer getIncomingPoisonDamage() {
        if(eff.type== Eff.EffType.Damage){
            return eff.getValue();
        }
        return 0;
    }

    @Override
    public Integer getRegen() {
        if(eff.type== Eff.EffType.Healing){
            return eff.getValue();
        }
        return 0;
    }

    @Override
    public String describe() {
        return "At the end of each turn, "+eff.toString();
    }
}
