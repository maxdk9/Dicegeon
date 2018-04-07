package com.tann.dice.gameplay.effect.trigger.types;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.tann.dice.gameplay.effect.Eff;
import com.tann.dice.gameplay.effect.trigger.Trigger;
import com.tann.dice.gameplay.entity.DiceEntity;

public class TriggerHalfHealthEffTypeBonus extends Trigger {

    private static TextureRegion tr = loadImage("berserk");
    Eff.EffType type;
    int bonus;

    public TriggerHalfHealthEffTypeBonus(Eff.EffType type, int bonus) {
        this.type = type;
        this.bonus = bonus;
    }

    @Override
    public int alterOutgoingEffect(Eff.EffType type, int value) {
        if(entity.aboveHalfHealth()) return value;
        if(this.type != type) return value;
        return value + bonus;
    }

    @Override
    public TextureRegion getImage() {
        return tr;
    }

    @Override
    public boolean showInPanel() {
        return true;
    }

    @Override
    public String describe() {
        return "If at half health or less, all "+type.toString().toLowerCase()+" increased by "+bonus;
    }
}
