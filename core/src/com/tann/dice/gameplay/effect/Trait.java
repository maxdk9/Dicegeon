package com.tann.dice.gameplay.effect;

import com.tann.dice.gameplay.effect.trigger.Trigger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Trait {
    public final List<Trigger> triggers;

    public Trait(Trigger... triggers) {
        this(Arrays.asList(triggers));
    }

    public Trait(List<Trigger> triggers) {
        this.triggers = triggers;
    }

    public String describe(){
        return triggers.get(0).describe();
    }

    public Trait copy(){
        List<Trigger> copies = new ArrayList<>();
        for(Trigger t:triggers){
            copies.add(t.copy());
        }
        return new Trait(copies);
    }

}
