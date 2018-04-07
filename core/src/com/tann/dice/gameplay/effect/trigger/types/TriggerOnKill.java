package com.tann.dice.gameplay.effect.trigger.types;

import com.tann.dice.gameplay.effect.Eff;
import com.tann.dice.gameplay.effect.trigger.Trigger;

public class TriggerOnKill extends Trigger {
    Eff eff;

    public TriggerOnKill(Eff eff) {
        this.eff = eff;
    }

    @Override
    public void onKill() {
        entity.hit(eff, false);
    }

    @Override
    public String describe() {
        return "When this hero kills an enemy, "+eff.toString().toLowerCase();
    }
}
