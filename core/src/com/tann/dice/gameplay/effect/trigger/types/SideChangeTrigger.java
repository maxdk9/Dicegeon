package com.tann.dice.gameplay.effect.trigger.types;

import com.tann.dice.gameplay.effect.Eff;
import com.tann.dice.gameplay.effect.trigger.Trigger;
import com.tann.dice.gameplay.entity.die.Side;

public class SideChangeTrigger extends Trigger{

    Eff.EffType replace;
    Side replacement;

    public SideChangeTrigger(Eff.EffType replace, Side replacement) {
        this.replace = replace;
        this.replacement = replacement;
    }

    @Override
    public void affectSide(Side side) {
        Eff[] effs = side.getEffects();
        for(int i=0;i<effs.length;i++){
            if(effs[i].type==replace){
                side.changeTo(replacement);
                break;
            }
        }
    }

    @Override
    public String describe() {
        return replace+" sides become "+replacement.toString();
    }
}
