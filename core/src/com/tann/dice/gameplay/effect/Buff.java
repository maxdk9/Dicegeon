package com.tann.dice.gameplay.effect;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.tann.dice.gameplay.effect.trigger.Trigger;
import com.tann.dice.gameplay.entity.DiceEntity;

public class Buff implements Cloneable{

    int turns;
    public DiceEntity target;
    public final TextureRegion image;
    public final Trigger trigger;
    public Buff(int turns, Trigger trigger){
        this.image = trigger.getImage();
        this.turns = turns;
        this.trigger = trigger;
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
        return trigger.describe()+" for "+turns+" turn"+(turns==1?"":"s");
    }

    public void setValue(int value) {
        trigger.setValue(value);
    }
}
