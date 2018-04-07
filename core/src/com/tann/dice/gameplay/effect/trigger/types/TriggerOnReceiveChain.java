package com.tann.dice.gameplay.effect.trigger.types;

import com.tann.dice.gameplay.effect.Eff;
import com.tann.dice.gameplay.effect.trigger.Trigger;

public class TriggerOnReceiveChain extends Trigger {

    //TODO this is untested

    Eff.EffType receiveType;
    Eff triggerEffect;

    public TriggerOnReceiveChain(Eff.EffType receiveType) {
        this.receiveType = receiveType;
    }

    @Override
    public void onHitWithEff(Eff e) {
        if(e.type==receiveType){
            entity.hit(triggerEffect, false);
        }
    }

    @Override
    public String describe() {
        String desc= "Whenever you ";
        switch(receiveType){
            case Shield: desc += "get shielded "; break;
            default: desc += "unknown  "; break;

        }
        desc += triggerEffect;
        return desc;
    }
}
