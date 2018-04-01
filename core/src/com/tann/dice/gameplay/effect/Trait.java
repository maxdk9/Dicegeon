package com.tann.dice.gameplay.effect;

import com.tann.dice.gameplay.effect.trigger.Trigger;

import java.util.Arrays;
import java.util.List;

public class Trait {
    public final List<Trigger> triggers;

    public Trait(Trigger... triggers) {
        this.triggers = Arrays.asList(triggers);
    }

    public String describe(){
        return triggers.get(0).describe();
    }
}
