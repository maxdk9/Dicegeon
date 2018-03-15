package com.tann.dice.gameplay.effect.trigger.types;

import com.tann.dice.gameplay.effect.trigger.Trigger;

public class DamageImmunityTrigger extends Trigger{
    @Override
    public Integer alterIncomingDamage(Integer incomingDamage) {
        return 0;
    }

    @Override
    public String describe() {
        return "Immune to damage";
    }
}
