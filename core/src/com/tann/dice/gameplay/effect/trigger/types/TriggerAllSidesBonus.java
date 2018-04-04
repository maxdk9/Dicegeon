package com.tann.dice.gameplay.effect.trigger.types;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.tann.dice.gameplay.effect.Eff;
import com.tann.dice.gameplay.effect.trigger.Trigger;
import com.tann.dice.gameplay.entity.die.Side;

public class TriggerAllSidesBonus extends Trigger{
    int amount;
    boolean show;
    private static final TextureRegion image = loadImage("allSidesBonus");
    public TriggerAllSidesBonus(int amount, boolean show) {
        this.amount = amount;
        this.show = show;
    }

    @Override
    public void affectSide(Side side) {
        for(Eff e:side.getEffects()){
            if(e.getValue()==0) continue;
            e.justValue(e.getValue()+amount);
        }
    }

    @Override
    public boolean showInPanel() {
        return show;
    }

    @Override
    public TextureRegion getImage() {
        return image;
    }

    @Override
    public void setValue(int value) {
        this.amount = value;
    }

    @Override
    public String describe() {
        return "+"+amount+" to all sides";
    }
}
