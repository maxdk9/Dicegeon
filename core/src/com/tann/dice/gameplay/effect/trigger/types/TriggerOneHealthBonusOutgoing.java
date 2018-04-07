package com.tann.dice.gameplay.effect.trigger.types;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.tann.dice.gameplay.effect.Eff;
import com.tann.dice.gameplay.effect.trigger.Trigger;

public class TriggerOneHealthBonusOutgoing extends Trigger {

    int bonus;

    public TriggerOneHealthBonusOutgoing(int bonus) {
        this.bonus = bonus;
    }

    @Override
    public int alterOutgoingEffect(Eff.EffType type, int value) {
        if(entity.getProfile().getTopHealth()==1){
            return value + bonus;
        }
        return value;
    }

    @Override
    public boolean showInPanel() {
        return entity.getProfile().getTopHealth()==1;
    }

    @Override
    public TextureRegion getImage() {
        return TriggerAllSidesBonus.image;
    }

    @Override
    public String describe() {
        return "If you are on exactly 1 hp, +"+bonus+" to all effects.";
    }
}