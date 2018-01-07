package com.tann.dice.gameplay.effect.buff;

import com.tann.dice.Images;
import com.tann.dice.gameplay.effect.Eff;

public class DamageMultiplier extends Buff{
    private float multiplier;

    public DamageMultiplier(float multiplier, int turns) {
        super(turns, Images.doubleDamage);
        this.multiplier = multiplier;
    }

    @Override
    public int alterOutgoingDamage(Eff.EffectType type, int amount) {
        switch (type){
            case Damage:
                return (int)(amount * multiplier);
        }
        return amount;
    }

    @Override
    public String toNiceString() {
        return "x"+(int)multiplier+" damage to a friendly target for "+turns+" turns";
    }

}
