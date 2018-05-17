package com.tann.dice.gameplay.effect.trigger.types;

import com.tann.dice.gameplay.effect.Eff;
import com.tann.dice.gameplay.effect.trigger.Trigger;
import com.tann.dice.gameplay.entity.DiceEntity;
import com.tann.dice.gameplay.entity.die.Side;

public class TriggerSideChange extends Trigger{

    Eff.EffType replace;
    Side replacement;

    public TriggerSideChange(Eff.EffType replace, Side replacement) {
        this.replace = replace;
        this.replacement = replacement;
    }

    @Override
    public void affectSide(Side side, DiceEntity owner) {
        Eff[] effs = side.getEffects();
        for(int i=0;i<effs.length;i++){
            if(effs[i].type==replace){
                side.changeTo(replacement.copy());
                break;
            }
        }
    }

    @Override
    public String describe() {
        return replace+" sides become "+replacement.toString();
    }
}
