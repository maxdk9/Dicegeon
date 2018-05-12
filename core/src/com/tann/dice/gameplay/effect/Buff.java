package com.tann.dice.gameplay.effect;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.tann.dice.gameplay.effect.trigger.Trigger;
import com.tann.dice.gameplay.entity.DiceEntity;

public class Buff {

    public int turns;
    public DiceEntity target;
    public final TextureRegion image;
    public final Trigger trigger;
    public Buff(int turns, Trigger trigger){
        this.image = trigger.getImage();
        this.turns = turns;
        this.trigger = trigger.copy();
    }

    public void turn(){
        if(turns != -1) turns--;
        if(turns == 0) remove();
    }

    private void remove() {
        target.removeBuff(this);
    }

    public Buff copy(){
        return new Buff(turns, trigger);
    }

    public String toNiceString(){
        if(turns == -1) return trigger.describe();
        String result = trigger.describe();
        if(turns == 1){
            return result + " this turn";
        }
        return result + " for "+turns+" turns";
    }

    public void setValue(int value) {
        trigger.setValue(value);
    }

    public boolean isNegative() {
        return trigger.isNegative();
    }
}
