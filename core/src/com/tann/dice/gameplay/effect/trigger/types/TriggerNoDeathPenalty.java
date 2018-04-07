package com.tann.dice.gameplay.effect.trigger.types;

import com.tann.dice.gameplay.effect.trigger.Trigger;

public class TriggerNoDeathPenalty extends Trigger {

    @Override
    public boolean avoidDeathPenalty() {
        return true;
    }

    @Override
    public String describe() {
        return "No HP penalty next fight for dying";
    }
}
