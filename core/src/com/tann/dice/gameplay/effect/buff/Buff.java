package com.tann.dice.gameplay.effect.buff;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.tann.dice.gameplay.effect.Eff;
import com.tann.dice.gameplay.effect.trigger.Trigger;
import com.tann.dice.gameplay.entity.DiceEntity;

public class Buff implements Cloneable{

    int turns;
    public DiceEntity target;
    public TextureRegion image;
    public Trigger trigger;
    public Buff(int turns, TextureRegion image, Trigger trigger){
        this.image = image;
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
        return new Buff(turns, image, trigger);
    }

    public String toNiceString(){
        if(turns == -1) return trigger.describe();
        return trigger.describe()+" for "+turns+" turn"+(turns==1?"":"s");
    }
}
